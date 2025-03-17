package com.matrixhero.ollama.client.exception;

/**
 * Exception thrown when an Ollama API request times out.
 */
public class OllamaTimeoutException extends RuntimeException {
    
    public OllamaTimeoutException(String message) {
        super(message);
    }

    public OllamaTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
} 