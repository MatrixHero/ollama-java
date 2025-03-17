package com.matrixhero.ollama.client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Text embedding response class.
 * Contains the generated embedding vector and related performance metrics.
 */
@Data
@NoArgsConstructor
public class EmbedResponse {
    /** Model name */
    private String model;
    /** Generated embedding vector */
    private double[] embedding;
    /** Total processing duration */
    private Long totalDuration;
    /** Model loading duration */
    private Long loadDuration;
    /** Number of prompt evaluations */
    private Integer promptEvalCount;
    /** Prompt evaluation duration */
    private Long promptEvalDuration;
    /** Number of evaluations */
    private Integer evalCount;
    /** Evaluation duration */
    private Long evalDuration;
} 