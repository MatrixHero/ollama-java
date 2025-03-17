package com.ollama.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Represents a chat message.
 * Contains information about the message role, content, images, and tool calls.
 */
@Data
@NoArgsConstructor
public class Message {
    public Message(Role role, String content) {
        this.role = role;
        this.content = content;
    }

    /**
     * Message role enumeration
     */
    public enum Role {
        @JsonProperty("user")
        USER,
        @JsonProperty("assistant")
        ASSISTANT,
        @JsonProperty("system")
        SYSTEM,
        @JsonProperty("tool")
        TOOL
    }

    /** Message role */
    private Role role;
    /** Message content */
    private String content;
    /** List of images in the message */
    private List<Image> images;
    /** List of tool calls */
    private List<ToolCall> toolCalls;

    /**
     * Represents a tool call in the message
     */
    @Data
    @NoArgsConstructor
    public static class ToolCall {
        /** Tool call ID */
        private String id;
        /** Tool type */
        private String type;
        /** Tool function information */
        private Function function;

        /**
         * Represents a function in a tool call
         */
        @Data
        @NoArgsConstructor
        public static class Function {
            /** Function name */
            private String name;
            /** Function arguments */
            private Map<String, Object> arguments;
        }
    }
} 