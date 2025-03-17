package com.ollama.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Text generation response class.
 * Contains the generated text and related performance metrics.
 */
@Data
@NoArgsConstructor
public class GenerateResponse {
    /** Model name */
    private String model;
    /** Creation timestamp */
    @JsonProperty("created_at")
    private String createdAt;
    /** Whether generation is complete */
    private Boolean done;
    /** Reason for completion */
    @JsonProperty("done_reason")
    private String doneReason;
    /** Total processing duration */
    @JsonProperty("total_duration")
    private Long totalDuration;
    /** Model loading duration */
    @JsonProperty("load_duration")
    private Long loadDuration;
    /** Number of prompt evaluations */
    @JsonProperty("prompt_eval_count")
    private Integer promptEvalCount;
    /** Prompt evaluation duration */
    @JsonProperty("prompt_eval_duration")
    private Long promptEvalDuration;
    /** Number of evaluations */
    @JsonProperty("eval_count")
    private Integer evalCount;
    /** Evaluation duration */
    @JsonProperty("eval_duration")
    private Long evalDuration;
    /** Generated response text */
    private String response;
    /** Context information */
    private List<Integer> context;
} 