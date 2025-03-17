package com.matrixhero.ollama.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Model list response class.
 * Contains information about available models and their details.
 */
@Data
@NoArgsConstructor
public class ListResponse {
    /** List of available models */
    private List<ModelInfo> models;

    /**
     * Model information class.
     * Contains details about a specific model.
     */
    @Data
    @NoArgsConstructor
    public static class ModelInfo {
        /** Model name */
        private String name;
        /** Model identifier */
        @JsonProperty("model")
        private String model;
        /** Model size in bytes */
        private Long size;
        /** Model digest */
        private String digest;
        /** Model modification timestamp */
        @JsonProperty("modified_at")
        private String modifiedAt;
        /** Model details */
        private ModelDetails details;

        /**
         * Model details class.
         * Contains specific information about the model's capabilities and parameters.
         */
        @Data
        @NoArgsConstructor
        public static class ModelDetails {
            /** Parent model name */
            @JsonProperty("parent_model")
            private String parentModel;
            /** Model format */
            private String format;
            /** Model family */
            private String family;
            /** Model families */
            private List<String> families;
            /** Parameter size */
            @JsonProperty("parameter_size")
            private String parameterSize;
            /** Quantization level */
            @JsonProperty("quantization_level")
            private String quantizationLevel;
        }
    }
} 