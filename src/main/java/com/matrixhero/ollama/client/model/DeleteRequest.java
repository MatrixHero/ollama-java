package com.matrixhero.ollama.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model deletion request class.
 * Contains the name of the model to be deleted.
 */
@Data
@NoArgsConstructor
public class DeleteRequest {
    /** Name of the model to delete */
    @JsonProperty("model")
    private String model;
} 