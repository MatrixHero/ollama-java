package com.matrixhero.ollama.client.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Represents image data.
 * Supports creating image objects from strings, byte arrays, or file paths.
 */
@Data
@NoArgsConstructor
public class Image {
    /** Base64 encoded image data */
    private String value;

    /**
     * Creates an image object from a Base64 string
     * @param value Base64 encoded image data
     */
    public Image(String value) {
        this.value = value;
    }

    /**
     * Creates an image object from a byte array
     * @param data Image byte array
     */
    public Image(byte[] data) {
        this.value = Base64.getEncoder().encodeToString(data);
    }

    /**
     * Creates an image object from a file path
     * @param path Image file path
     * @throws java.io.IOException if file reading fails
     */
    public Image(Path path) throws java.io.IOException {
        byte[] data = Files.readAllBytes(path);
        this.value = Base64.getEncoder().encodeToString(data);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
} 