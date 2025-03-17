package com.ollama.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat response class.
 * Contains the chat conversation result and related performance metrics.
 */
@Data
@NoArgsConstructor
public class ChatResponse {
    /** Model name */
    private String model;
    /** Creation timestamp */
    @JsonProperty("created_at")
    private String createdAt;
    /** Message content */
    private Message message;
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
} 