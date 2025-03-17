package com.ollama.client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Text embedding request class.
 * Contains various parameters required for text embedding generation.
 */
@Data
@NoArgsConstructor
public class EmbedRequest {
    /** Model name */
    private String model;
    /** Input text */
    private String input;
    /** Embedding options */
    private Options options;
} 