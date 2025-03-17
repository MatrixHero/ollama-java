package com.ollama.client;

import com.ollama.client.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OllamaClient的单元测试类。
 */
class OllamaClientTest {

    private OllamaClient client;

    @BeforeEach
    void setUp() {
        client = new OllamaClient();
    }

    @Test
    void testGenerate() throws IOException {
        // Prepare test data
        GenerateRequest request = new GenerateRequest();
        request.setModel("qwen2.5:7b");
        request.setPrompt("hello world");
        // Execute test
        GenerateResponse response = client.generate(request);

        // Print results
        System.out.println("Generate Response:");
        System.out.println("Model: " + response.getModel());
        System.out.println("Response: " + response.getResponse());
        System.out.println("Done: " + response.getDone());
        System.out.println("Total Duration: " + response.getTotalDuration());
        System.out.println("Load Duration: " + response.getLoadDuration());
        System.out.println("Eval Count: " + response.getEvalCount());
        System.out.println("Eval Duration: " + response.getEvalDuration());
        System.out.println("----------------------------------------");

        // Verify results
        assertNotNull(response);
        assertEquals("qwen2.5:7b", response.getModel());
        assertNotNull(response.getResponse());
    }

    @Test
    void testGenerateStream() throws IOException {
        // Prepare test data
        GenerateRequest request = new GenerateRequest();
        request.setModel("qwen2.5:7b");
        request.setPrompt("你是谁");
        request.setStream(true);

        // Execute test and collect results
        List<GenerateResponse> responses = client.generateStream(request)
            .peek(response -> {
                // Print results
                System.out.println("Generate Stream Response:");
                System.out.println("Model: " + response.getModel());
                System.out.println("Response: " + response.getResponse());
                System.out.println("Done: " + response.getDone());
                if (response.getDone()) {
                    System.out.println("Total Duration: " + response.getTotalDuration());
                    System.out.println("Load Duration: " + response.getLoadDuration());
                    System.out.println("Eval Count: " + response.getEvalCount());
                    System.out.println("Eval Duration: " + response.getEvalDuration());
                }
                System.out.println("----------------------------------------");
            })
            .collect(Collectors.toList());

        // Verify results
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        responses.forEach(response -> {
            assertNotNull(response);
            assertEquals("qwen2.5:7b", response.getModel());
            assertNotNull(response.getResponse());
            if (response.getDone()) {
                assertTrue(response.getResponse().isEmpty());
            }
        });
    }

    @Test
    void testChat() throws IOException {
        // Prepare test data
        ChatRequest request = new ChatRequest();
        request.setModel("qwen2.5:7b");
        request.setMessages(Arrays.asList(
            new Message(Message.Role.USER, "Hello, how are you?")
        ));

        // Execute test
        ChatResponse response = client.chat(request);

        // Print results
        System.out.println("Chat Response:");
        System.out.println("Model: " + response.getModel());
        System.out.println("Message Role: " + response.getMessage().getRole());
        System.out.println("Message Content: " + response.getMessage().getContent());
        System.out.println("Done: " + response.getDone());
        System.out.println("Total Duration: " + response.getTotalDuration());
        System.out.println("Load Duration: " + response.getLoadDuration());
        System.out.println("Eval Count: " + response.getEvalCount());
        System.out.println("Eval Duration: " + response.getEvalDuration());
        System.out.println("----------------------------------------");

        // Verify results
        assertNotNull(response);
        assertEquals("qwen2.5:7b", response.getModel());
        assertNotNull(response.getMessage());
        assertTrue(response.getDone());
    }

    @Test
    void testChatStream() throws IOException {
        // Prepare test data
        ChatRequest request = new ChatRequest();
        request.setModel("qwen2.5:7b");
        request.setMessages(Arrays.asList(
            new Message(Message.Role.USER, "你好，最近怎么样？")
        ));
        request.setStream(true);

        // Execute test and collect results
        List<ChatResponse> responses = client.chatStream(request)
            .peek(response -> {
                // Print results
                System.out.println("Chat Stream Response:");
                System.out.println("Model: " + response.getModel());
                System.out.println("Message Role: " + response.getMessage().getRole());
                System.out.println("Message Content: " + response.getMessage().getContent());
                System.out.println("Done: " + response.getDone());
                if (response.getDone()) {
                    System.out.println("Total Duration: " + response.getTotalDuration());
                    System.out.println("Load Duration: " + response.getLoadDuration());
                    System.out.println("Eval Count: " + response.getEvalCount());
                    System.out.println("Eval Duration: " + response.getEvalDuration());
                }
                System.out.println("----------------------------------------");
            })
            .collect(Collectors.toList());

        // Verify results
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        responses.forEach(response -> {
            assertNotNull(response);
            assertEquals("qwen2.5:7b", response.getModel());
            assertNotNull(response.getMessage());
            if (response.getDone()) {
                assertTrue(response.getMessage().getContent().isEmpty());
            }
        });
    }

    @Test
    void testList() throws IOException {
        // Execute test
        ListResponse response = client.list();

        // Print results
        System.out.println("List Response:");
        System.out.println("Models:");
        response.getModels().forEach(model -> {
            System.out.println("- Name: " + model.getName());
            System.out.println("  Size: " + model.getSize());
            System.out.println("  Digest: " + model.getDigest());
            System.out.println("  Modified At: " + model.getModifiedAt());
        });
        System.out.println("----------------------------------------");

        // Verify results
        assertNotNull(response);
        assertNotNull(response.getModels());
        assertFalse(response.getModels().isEmpty());
    }

//    @Test
//    void testDelete() throws IOException {
//        // Prepare test data
//        DeleteRequest request = new DeleteRequest();
//        request.setModel("test-model");
//
//        // Execute test
//        assertDoesNotThrow(() -> client.delete(request));
//        System.out.println("Delete Response: Success");
//        System.out.println("----------------------------------------");
//    }
} 