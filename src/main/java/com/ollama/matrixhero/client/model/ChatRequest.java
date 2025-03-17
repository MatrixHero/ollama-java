package com.ollama.matrixhero.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Request model for chat conversations.
 * This class represents the parameters needed to have a chat conversation with the model.
 */
@Data
@NoArgsConstructor
public class ChatRequest {
    /**
     * The name of the model to use for the chat.
     * This should be a valid model name that has been pulled to the Ollama server.
     */
    private String model;

    /**
     * The list of messages in the conversation.
     * Each message has a role (user/assistant) and content.
     */
    private List<Message> messages;

    /**
     * The list of tools available to the model.
     * This is optional and can be used to provide function calling capabilities.
     */
    private List<Tool> tools;

    /**
     * Generation options to control the behavior of the model.
     * This includes parameters like temperature, top_p, etc.
     */
    private Options options;

    /**
     * The format of the response.
     * This is optional and can be used to specify the desired output format.
     */
    private String format;

    /**
     * Keep-alive duration in seconds.
     * This is optional and can be used to keep the model loaded for subsequent requests.
     */
    @JsonProperty("keep_alive")
    private Integer keepAlive;

    /**
     * Whether to stream the response.
     * When true, the response will be streamed line by line.
     */
    private Boolean stream;
} 