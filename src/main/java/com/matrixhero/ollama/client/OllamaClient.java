package com.matrixhero.ollama.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrixhero.ollama.client.agent.Agent;
import com.matrixhero.ollama.client.model.*;
import com.matrixhero.ollama.client.exception.OllamaTimeoutException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Iterator;
import java.util.Spliterators;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Spliterator;

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
    private static final String CONFIG_FILE = "application.properties";

    /** HTTP client */
    private final OkHttpClient client;
    /** JSON processor */
    private final ObjectMapper objectMapper;
    /** Server base URL */
    private final String host;
    /** Whether to enable streaming responses */
    private boolean stream = false;

    private final List<Agent> agents = new ArrayList<>();

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

    /**
     * Creates a new OllamaClient with custom timeouts.
     *
     * @param host The custom host URL
     * @param connectTimeout Connection timeout in seconds
     * @param readTimeout Read timeout in seconds
     * @param writeTimeout Write timeout in seconds
     */
    public OllamaClient(String host, long connectTimeout, long readTimeout, long writeTimeout) {
        this.host = host;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Sets the connection timeout for the client.
     *
     * @param timeout Timeout in seconds
     * @return A new OllamaClient instance with the updated timeout
     */
    public OllamaClient withConnectTimeout(long timeout) {
        OkHttpClient newClient = client.newBuilder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
        return new OllamaClient(host, newClient, objectMapper);
    }

    /**
     * Sets the read timeout for the client.
     *
     * @param timeout Timeout in seconds
     * @return A new OllamaClient instance with the updated timeout
     */
    public OllamaClient withReadTimeout(long timeout) {
        OkHttpClient newClient = client.newBuilder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();
        return new OllamaClient(host, newClient, objectMapper);
    }

    /**
     * Sets the write timeout for the client.
     *
     * @param timeout Timeout in seconds
     * @return A new OllamaClient instance with the updated timeout
     */
    public OllamaClient withWriteTimeout(long timeout) {
        OkHttpClient newClient = client.newBuilder()
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();
        return new OllamaClient(host, newClient, objectMapper);
    }

    /**
     * Creates a new OllamaClient with the specified host, client, and object mapper.
     * This is a private constructor used internally for creating new instances with modified settings.
     */
    private OllamaClient(String host, OkHttpClient client, ObjectMapper objectMapper) {
        this.host = host;
        this.client = client;
        this.objectMapper = objectMapper;
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
     * Add an agent to the client.
     * @param agent The agent to add
     * @return This client instance for method chaining
     */
    public OllamaClient withAgent(Agent agent) {
        this.agents.add(agent);
        return this;
    }

    /**
     * Generates text based on the provided prompt.
     * @param request The generation request containing model and prompt
     * @return The generation response
     * @throws IOException if there's an error communicating with the server
     * @throws OllamaTimeoutException if the request times out
     */
    public GenerateResponse generate(GenerateRequest request) throws IOException {
        request.setStream(stream);
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
                .url(host + "/api/generate")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try {
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
        } catch (SocketTimeoutException e) {
            throw new OllamaTimeoutException("Request timed out while generating text", e);
        }
    }

    /**
     * Generates text based on the provided prompt with streaming support.
     * @param request The generation request containing model and prompt
     * @return A stream of generation responses
     * @throws IOException if there's an error communicating with the server
     * @throws OllamaTimeoutException if the request times out
     */
    public Stream<GenerateResponse> generateStream(GenerateRequest request) throws IOException {
        request.setStream(true);
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
                .url(host + "/api/generate")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try {
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

            return StreamSupport.stream(
                Spliterators.<GenerateResponse>spliteratorUnknownSize(
                    new Iterator<GenerateResponse>() {
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
                    },
                    0
                ),
                false
            ).onClose(() -> {
                try {
                    response.close();
                } catch (Exception e) {
                    log.error("Error closing response", e);
                }
            });
        } catch (SocketTimeoutException e) {
            throw new OllamaTimeoutException("Request timed out while streaming text generation", e);
        }
    }

    /**
     * Chat with the model, with agent support.
     * @param request The chat request
     * @return The chat response
     * @throws IOException if there's an error communicating with the server
     * @throws IllegalArgumentException if the request is invalid
     * @throws OllamaTimeoutException if the request times out
     */
    public ChatResponse chat(ChatRequest request) throws Exception {
        if (request == null || request.getMessages() == null || request.getMessages().isEmpty()) {
            throw new IllegalArgumentException("Chat request and messages cannot be null or empty");
        }

        request.setStream(false);  // Ensure non-streaming mode
        Message lastMessage = getLastMessage(request);
        
        // Try to use agent if enabled
        ChatResponse agentResponse = tryUseAgent(request, lastMessage);
        if (agentResponse != null) {
            return agentResponse;
        }

        // If no suitable agent found or agents disabled, use model
        return callModel(request);
    }

    private Message getLastMessage(ChatRequest request) {
        return request.getMessages().get(request.getMessages().size() - 1);
    }

    private ChatResponse tryUseAgent(ChatRequest request, Message lastMessage) throws Exception {
        if (!request.isUseAgents() || agents.isEmpty()) {
            return null;
        }

        for (Agent agent : agents) {
            if (agent.canHandle(lastMessage.getContent())) {
                try {
                    log.debug("Using agent: {} for message: {}", agent.getName(), lastMessage.getContent());
                    String agentResponse = agent.execute(lastMessage.getContent());
                    Message responseMessage = new Message(Message.Role.ASSISTANT, agentResponse);
                    request.getMessages().add(responseMessage);  // Add to conversation history
                    return new ChatResponse(responseMessage);
                } catch (Exception e) {
                    log.warn("Agent {} failed to handle message: {}", agent.getName(), lastMessage.getContent(), e);
                }
            }
        }
        return null;
    }

    private ChatResponse callModel(ChatRequest request) throws IOException {
        String url = host + "/api/chat";
        String json = objectMapper.writeValueAsString(request);
        
        log.debug("Sending chat request to model: {}", request.getModel());
        Request httpRequest = new Request.Builder()
            .url(url)
            .post(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new IOException(String.format("Chat request failed with code %d: %s", 
                    response.code(), errorBody));
            }

            String responseBody = response.body().string();
            ChatResponse chatResponse = objectMapper.readValue(responseBody, ChatResponse.class);
            request.getMessages().add(chatResponse.getMessage());  // Add model response to conversation history
            return chatResponse;
        } catch (SocketTimeoutException e) {
            throw new OllamaTimeoutException("Request timed out while chatting", e);
        } catch (IOException e) {
            log.error("Error during chat request: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Chat with the model with streaming support, with agent support.
     * @param request The chat request
     * @return A stream of chat responses
     * @throws IOException if there's an error communicating with the server
     */
    public Stream<ChatResponse> chatStream(ChatRequest request) throws IOException {

        // If no suitable agent found or agent execution failed, use model directly
        request.setStream(true);
        // Check if there's a suitable agent to handle the request
        for (Agent agent : agents) {
            if (request.isUseAgents() && agent.canHandle(request.getMessages().get(request.getMessages().size() - 1).getContent())) {
                try {
                    // Execute agent
                    String agentResponse = agent.execute(request.getMessages().get(request.getMessages().size() - 1).getContent());
                    
                    // Add agent's response to conversation history
                    request.getMessages().add(new Message(Message.Role.ASSISTANT, agentResponse));
                    return Stream.of(new ChatResponse(new Message(Message.Role.ASSISTANT, agentResponse)));
                } catch (Exception e) {
                    log.error("Error executing agent: " + agent.getName(), e);
                    // If agent execution fails, continue with model processing
                }
            }
        }
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

        BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream()));
        Iterator<ChatResponse> iterator = new Iterator<ChatResponse>() {
            private String nextLine = null;

            @Override
            public boolean hasNext() {
                try {
                    nextLine = reader.readLine();
                    return nextLine != null;
                } catch (IOException e) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        // Ignore
                    }
                    return false;
                }
            }

            @Override
            public ChatResponse next() {
                if (nextLine == null) {
                    throw new NoSuchElementException();
                }
                try {
                    return objectMapper.readValue(nextLine, ChatResponse.class);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to parse response", e);
                }
            }
        };

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
            false
        ).onClose(() -> {
            try {
                reader.close();
            } catch (IOException e) {
                // Ignore
            }
            response.close();
        });
    }

    /**
     * Generates embeddings for the input text.
     * @param request The embedding request containing model and input text
     * @return The embedding response
     * @throws IOException if there's an error communicating with the server
     * @throws OllamaTimeoutException if the request times out
     */
    public EmbedResponse embed(EmbedRequest request) throws IOException {
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
                .url(host + "/api/embeddings")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try {
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                return objectMapper.readValue(response.body().string(), EmbedResponse.class);
            }
        } catch (SocketTimeoutException e) {
            throw new OllamaTimeoutException("Request timed out while generating embeddings", e);
        }
    }

    /**
     * Lists all available models.
     * @return The list response containing model information
     * @throws IOException if there's an error communicating with the server
     * @throws OllamaTimeoutException if the request times out
     */
    public ListResponse list() throws IOException {
        Request httpRequest = new Request.Builder()
                .url(host + "/api/tags")
                .get()
                .build();

        try {
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
                return objectMapper.readValue(response.body().string(), ListResponse.class);
            }
        } catch (SocketTimeoutException e) {
            throw new OllamaTimeoutException("Request timed out while listing models", e);
        }
    }

    /**
     * Delete a model.
     *
     * @param request DeleteRequest containing the model name to delete
     * @throws IOException if the request fails
     * @throws OllamaTimeoutException if the request times out
     */
    public void delete(DeleteRequest request) throws IOException {
        String json = objectMapper.writeValueAsString(request);
        Request httpRequest = new Request.Builder()
            .url(host + "/api/delete")
            .delete(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try {
            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }
            }
        } catch (SocketTimeoutException e) {
            throw new OllamaTimeoutException("Request timed out while deleting model", e);
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