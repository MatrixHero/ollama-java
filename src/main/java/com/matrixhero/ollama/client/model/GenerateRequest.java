package com.matrixhero.ollama.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Request model for text generation.
 * This class represents the parameters needed to generate text using the Ollama API.
 */
@Data
@NoArgsConstructor
public class GenerateRequest {
    /**
     * The name of the model to use for generation.
     * This should be a valid model name that has been pulled to the Ollama server.
     */
    private String model;

    /**
     * The input prompt for text generation.
     * This is the main text that will be used to generate the response.
     */
    private String prompt;

    /**
     * System prompt to set the behavior of the model.
     * This is optional and can be used to provide context or instructions to the model.
     */
    private String system;

    /**
     * Template to use for formatting the prompt.
     * This is optional and can be used to customize how the prompt is formatted.
     */
    private String template;

    /**
     * Context information for the generation.
     * This can be used to maintain conversation history or provide additional context.
     */
    private List<Integer> context;

    /**
     * Whether to use raw mode for generation.
     * In raw mode, the model will not apply any formatting or special handling.
     */
    private Boolean raw;

    /**
     * List of images to include in the generation.
     * This is optional and can be used for multimodal models that support image input.
     */
    private List<Image> images;

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