package com.ollama.matrixhero.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ollama.matrixhero.client.model.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Iterator;
import java.util.Spliterators;

/**
 * Main client class for interacting with the Ollama API.
 * Provides methods for text generation, chat, embeddings, and model management.
 * This class is thread-safe.
 */
@Slf4j
public class OllamaClient implements AutoCloseable {
    private static final String DEFAULT_HOST = "http://localhost:11434";
    private static final String HOST_PROPERTY = "ollama.host";
    private static final String HOST_ENV = "OLLAMA_HOST";
    private static final String CONFIG_FILE = "ollama.properties";

    /** HTTP客户端 */
    private final OkHttpClient client;
    /** JSON处理器 */
    private final ObjectMapper objectMapper;
    /** 服务器基础URL */
    private final String host;
    /** 是否启用流式响应 */
    private boolean stream = false;

    /**
     * Creates a new OllamaClient with the default host (http://localhost:11434).
     * The host can be configured through:
     * 1. System property: ollama.host
     * 2. Environment variable: OLLAMA_HOST
     * 3. Configuration file: ollama.properties
     */
    public OllamaClient() {
        this.host = getConfiguredHost();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Creates a new OllamaClient with a custom host.
     *
     * @param host The custom host URL
     */
    public OllamaClient(String host) {
        this.host = host;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    private String getConfiguredHost() {
        // Try system property first
        String host = System.getProperty(HOST_PROPERTY);
        if (host != null && !host.trim().isEmpty()) {
            return host;
        }

        // Try environment variable
        host = System.getenv(HOST_ENV);
        if (host != null && !host.trim().isEmpty()) {
            return host;
        }

        // Try configuration file
        try {
            java.util.Properties props = new java.util.Properties();
            java.io.InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (input != null) {
                props.load(input);
                host = props.getProperty(HOST_PROPERTY);
                if (host != null && !host.trim().isEmpty()) {
                    return host;
                }
            }
        } catch (IOException e) {
            log.warn("Failed to load configuration file: {}", CONFIG_FILE, e);
        }

        return DEFAULT_HOST;
    }

    /**
     * 设置是否启用流式响应
     * @param stream 是否启用流式响应
     */
    public void setStream(boolean stream) {
        this.stream = stream;
    }

    /**
     * Generates text based on the provided prompt.
     * @param request The generation request containing model and prompt
     * @return The generation response
     * @throws IOException if there's an error communicating with the server
     */
    public GenerateResponse generate(GenerateRequest request) throws IOException {
        request.setStream(stream);
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
                .url(host + "/api/generate")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        if (stream) {
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                ResponseBody body = response.body();
                if (body == null) {
                    throw new IOException("Empty response body");
                }
                return objectMapper.readValue(body.string(), GenerateResponse.class);
            }
        } else {
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                return objectMapper.readValue(response.body().string(), GenerateResponse.class);
            }
        }
    }

    /**
     * Generates text based on the provided prompt with streaming support.
     * @param request The generation request containing model and prompt
     * @return A stream of generation responses
     * @throws IOException if there's an error communicating with the server
     */
    public Stream<GenerateResponse> generateStream(GenerateRequest request) throws IOException {
        request.setStream(true);
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
                .url(host + "/api/generate")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        Response response = client.newCall(httpRequest).execute();
        if (!response.isSuccessful()) {
            response.close();
            throw new IOException("Unexpected response code: " + response);
        }

        ResponseBody body = response.body();
        if (body == null) {
            response.close();
            throw new IOException("Empty response body");
        }

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<GenerateResponse>() {
            private final String[] lines = body.string().split("\n");
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < lines.length;
            }

            @Override
            public GenerateResponse next() {
                try {
                    return objectMapper.readValue(lines[currentIndex++], GenerateResponse.class);
                } catch (IOException e) {
                    throw new RuntimeException("Error parsing response", e);
                }
            }
        }, 0), false).onClose(() -> {
            try {
                response.close();
            } catch (Exception e) {
                log.error("Error closing response", e);
            }
        });
    }

    /**
     * Performs a chat conversation with the model.
     * @param request The chat request containing model and messages
     * @return The chat response
     * @throws IOException if there's an error communicating with the server
     */
    public ChatResponse chat(ChatRequest request) throws IOException {
        request.setStream(stream);
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
                .url(host + "/api/chat")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        if (stream) {
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                ResponseBody body = response.body();
                if (body == null) {
                    throw new IOException("Empty response body");
                }
                return objectMapper.readValue(body.string(), ChatResponse.class);
            }
        } else {
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                return objectMapper.readValue(response.body().string(), ChatResponse.class);
            }
        }
    }

    /**
     * Performs a chat conversation with the model with streaming support.
     * @param request The chat request containing model and messages
     * @return A stream of chat responses
     * @throws IOException if there's an error communicating with the server
     */
    public Stream<ChatResponse> chatStream(ChatRequest request) throws IOException {
        request.setStream(true);
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
                .url(host + "/api/chat")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        Response response = client.newCall(httpRequest).execute();
        if (!response.isSuccessful()) {
            response.close();
            throw new IOException("Unexpected response code: " + response);
        }

        ResponseBody body = response.body();
        if (body == null) {
            response.close();
            throw new IOException("Empty response body");
        }

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<ChatResponse>() {
            private final String[] lines = body.string().split("\n");
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < lines.length;
            }

            @Override
            public ChatResponse next() {
                try {
                    return objectMapper.readValue(lines[currentIndex++], ChatResponse.class);
                } catch (IOException e) {
                    throw new RuntimeException("Error parsing response", e);
                }
            }
        }, 0), false).onClose(() -> {
            try {
                response.close();
            } catch (Exception e) {
                log.error("Error closing response", e);
            }
        });
    }

    /**
     * Generates embeddings for the input text.
     * @param request The embedding request containing model and input text
     * @return The embedding response
     * @throws IOException if there's an error communicating with the server
     */
    public EmbedResponse embed(EmbedRequest request) throws IOException {
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
                .url(host + "/api/embeddings")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            return objectMapper.readValue(response.body().string(), EmbedResponse.class);
        }
    }

    /**
     * Lists all available models.
     * @return The list response containing model information
     * @throws IOException if there's an error communicating with the server
     */
    public ListResponse list() throws IOException {
        Request httpRequest = new Request.Builder()
                .url(host + "/api/tags")
                .get()
                .build();

        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            return objectMapper.readValue(response.body().string(), ListResponse.class);
        }
    }

    /**
     * Delete a model.
     *
     * @param request DeleteRequest containing the model name to delete
     * @throws IOException if the request fails
     */
    public void delete(DeleteRequest request) throws IOException {
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
            .url(host + "/api/delete")
            .delete(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
        }
    }

    /**
     * Closes the client and releases resources.
     */
    @Override
    public void close() {
        client.dispatcher().executorService().shutdown();
    }
} 