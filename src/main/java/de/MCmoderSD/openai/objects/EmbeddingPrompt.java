package de.MCmoderSD.openai.objects;

import com.openai.models.embeddings.CreateEmbeddingResponse;

/**
 * Represents a prompt for creating an embedding using the OpenAI API.
 */
@SuppressWarnings("unused")
public class EmbeddingPrompt {

    // Parameters
    private final String input;
    private final CreateEmbeddingResponse output;

    // Data
    private final String model;
    private final CreateEmbeddingResponse.Usage usage;

    // Usage
    private final long promptTokens;
    private final long totalTokens;

    // Content
    private final Embedding embedding;

    /**
     * Constructs an EmbeddingPrompt with the specified input and output.
     *
     * @param input  the input string for the embedding prompt
     * @param output the response from the OpenAI API containing the embedding data
     */
    public EmbeddingPrompt(String input, CreateEmbeddingResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Data
        model = output.model();
        usage = output.usage();

        // Extract Usage
        promptTokens = usage.promptTokens();
        totalTokens = usage.totalTokens();

        // Extract Content
        embedding = new Embedding(output.data().getFirst().embedding());
    }

    /**
     * Returns the input string for the embedding prompt.
     *
     * @return the input string
     */
    public String getInput() {
        return input;
    }

    /**
     * Returns the response from the OpenAI API containing the embedding data.
     *
     * @return the response from the OpenAI API
     */
    public CreateEmbeddingResponse getOutput() {
        return output;
    }

    /**
     * Returns the model used for creating the embedding.
     *
     * @return the model used
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the usage information from the OpenAI API response.
     *
     * @return the usage information
     */
    public CreateEmbeddingResponse.Usage getUsage() {
        return usage;
    }

    /**
     * Returns the number of tokens used for the prompt.
     *
     * @return the number of prompt tokens
     */
    public long getPromptTokens() {
        return promptTokens;
    }

    /**
     * Returns the total number of tokens used.
     *
     * @return the total number of tokens
     */
    public long getTotalTokens() {
        return totalTokens;
    }

    /**
     * Returns the embedding created from the OpenAI API response.
     *
     * @return the embedding
     */
    public Embedding getEmbedding() {
        return embedding;
    }
}