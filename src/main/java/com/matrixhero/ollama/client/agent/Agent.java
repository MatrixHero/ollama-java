package com.matrixhero.ollama.client.agent;

/**
 * Interface for external API agents that can be integrated with the Ollama client.
 */
public interface Agent {
    /**
     * Get the name of the agent.
     * @return The name of the agent
     */
    String getName();

    /**
     * Get the description of what the agent can do.
     * @return The description of the agent's capabilities
     */
    String getDescription();

    /**
     * Check if the agent can handle the given input.
     * @param input The input to check
     * @return true if the agent can handle the input, false otherwise
     */
    boolean canHandle(String input);

    /**
     * Execute the agent with the given input.
     * @param input The input to process
     * @return The result of the agent's execution
     * @throws Exception if there's an error executing the agent
     */
    String execute(String input) throws Exception;
} 