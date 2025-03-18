package com.matrixhero.ollama.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
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

    public static Message builder() {
        return new Message();
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<ToolCall> getToolCalls() {
        return toolCalls;
    }

    public void setToolCalls(List<ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }

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