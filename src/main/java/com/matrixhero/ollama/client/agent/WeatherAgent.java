package com.matrixhero.ollama.client.agent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrixhero.ollama.client.OllamaClient;
import com.matrixhero.ollama.client.model.ChatRequest;
import com.matrixhero.ollama.client.model.Message;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Agent for querying weather information using OpenWeatherMap API.
 */
@Slf4j
public class WeatherAgent implements Agent {
    private static final String API_KEY_ENV = "OPENWEATHERMAP_API_KEY";
    private static final String API_KEY_PROPERTY = "openweathermap.api.key";
    private static final String CONFIG_FILE = "application.properties";
    private static final Pattern WEATHER_PATTERN = Pattern.compile(
            ".*weather.*|.*temperature.*|.*天气.*|.*气温.*|.*温度.*|.*下雨.*|.*晴.*|.*阴.*",
            Pattern.CASE_INSENSITIVE
    );
    private final String apiKey;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final OllamaClient ollamaClient;

    public WeatherAgent(OllamaClient ollamaClient) {
        this.apiKey = getApiKey();
        this.ollamaClient = ollamaClient;
        if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
            throw new IllegalStateException(
                "OpenWeatherMap API key not found. Please set it via:\n" +
                "1. Environment variable: " + API_KEY_ENV + "\n" +
                "2. System property: " + API_KEY_PROPERTY + "\n" +
                "3. Configuration file: " + CONFIG_FILE
            );
        }
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    private String getApiKey() {
        // Try environment variable first
        String apiKey = System.getenv(API_KEY_ENV);
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            return apiKey;
        }

        // Try system property
        apiKey = System.getProperty(API_KEY_PROPERTY);
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            return apiKey;
        }

        // Try configuration file
        try {
            java.util.Properties props = new java.util.Properties();
            java.io.InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (input != null) {
                props.load(input);
                apiKey = props.getProperty(API_KEY_PROPERTY);
                if (apiKey != null && !apiKey.trim().isEmpty()) {
                    return apiKey;
                }
            }
        } catch (IOException e) {
            log.warn("Failed to load configuration file: {}", CONFIG_FILE, e);
        }

        return null;
    }

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public String getDescription() {
        return "Query weather information, supporting both Chinese and English city names";
    }

    @Override
    public boolean canHandle(String input) {
        return WEATHER_PATTERN.matcher(input).matches();
    }

    @Override
    public String execute(String input) throws Exception {
        // Extract city name using LLM
        String city = extractCityWithLLM(input, ollamaClient, "qwen2.5:7b");
        if (city == null) {
            return "Sorry, I couldn't identify the city you want to query. Please specify a city name, for example: 'What's the weather in Beijing?'";
        }

        // Build OpenWeatherMap API request
        String url = String.format(
            "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=zh_cn",
            city, apiKey
        );

        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Sorry, failed to get weather information. Please check if the city name is correct.";
            }

            String responseBody = response.body().string();
            WeatherResponse weatherResponse = objectMapper.readValue(responseBody, WeatherResponse.class);
            
            return formatWeatherResponse(weatherResponse);
        }
    }

    private String extractCityWithLLM(String input, OllamaClient llm, String model) throws Exception {
        // Build prompt
        String prompt = String.format(
            "You are a city name extractor. Follow these steps:\n" +
            "1. Extract the city name from the following text (can be in Chinese or English)\n" +
            "2. If no city name is found, return null\n" +
            "3. If the city name is in Chinese, convert it to its English name\n" +
            "4. Return only the English city name, without any explanation\n\n" +
            "Text: %s", input
        );

        // Create chat request
        ChatRequest request = new ChatRequest();
        request.setModel(model);
        request.setStream(false);
        request.setUseAgents(false);  // Disable agents to prevent recursive calls
        request.setMessages(Arrays.asList(
            new Message(Message.Role.SYSTEM, "You are a city name extractor. Extract city names in Chinese or English, then convert to English names."),
            new Message(Message.Role.USER, prompt)
        ));

        // Call model
        String response = llm.chat(request).getMessage().getContent().trim();
        
        // Return null if response is null or empty
        if ("null".equalsIgnoreCase(response) || response.isEmpty()) {
            return null;
        }

        // Clean up punctuation and extra spaces
        response = response.replaceAll("[.,!?，。！？]", "").trim();
        
        // Return null if cleaned response is empty
        if (response.isEmpty()) {
            return null;
        }

        return response;
    }

    private String formatWeatherResponse(WeatherResponse response) {
        return String.format(
            "Weather in %s:\n" +
            "Temperature: %.1f°C\n" +
            "Humidity: %d%%\n" +
            "Conditions: %s\n" +
            "Wind Speed: %.1f m/s",
            response.name,
            response.main.temp,
            response.main.humidity,
            response.weather[0].description,
            response.wind.speed
        );
    }

    // Inner class for parsing API response
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WeatherResponse {
        public String name;
        public Main main;
        public Weather[] weather;
        public Wind wind;
        public Coord coord;
        public Sys sys;
        public int timezone;
        public int id;
        public int cod;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Main {
            public double temp;
            public double feels_like;
            public double temp_min;
            public double temp_max;
            public int pressure;
            public int humidity;
            public int sea_level;
            public int grnd_level;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Weather {
            public int id;
            public String main;
            public String description;
            public String icon;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Wind {
            public double speed;
            public int deg;
            public double gust;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Coord {
            public double lon;
            public double lat;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Sys {
            public int type;
            public int id;
            public String country;
            public long sunrise;
            public long sunset;
        }
    }
} 