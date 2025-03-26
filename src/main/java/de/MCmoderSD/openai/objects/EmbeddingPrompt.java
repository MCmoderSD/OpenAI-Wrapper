package de.MCmoderSD.openai.objects;

import com.openai.models.embeddings.CreateEmbeddingResponse;

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

    public String getInput() {
        return input;
    }

    public CreateEmbeddingResponse getOutput() {
        return output;
    }

    public String getModel() {
        return model;
    }

    public CreateEmbeddingResponse.Usage getUsage() {
        return usage;
    }

    public long getPromptTokens() {
        return promptTokens;
    }

    public long getTotalTokens() {
        return totalTokens;
    }

    public Embedding getEmbedding() {
        return embedding;
    }
}