package de.MCmoderSD.openai.objects;

import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.Embedding;

import java.util.ArrayList;

public class EmbeddingPrompt {

    // Parameters
    private final String input;
    private final CreateEmbeddingResponse createEmbeddingResponse;

    // Data
    private final String model;
    private final CreateEmbeddingResponse.Usage usage;

    // Usage
    private final long promptTokens;
    private final long totalTokens;

    // Content
    private final ArrayList<Embedding> embeddings;

    public EmbeddingPrompt(String input, CreateEmbeddingResponse createEmbeddingResponse) {

        // Initialize Parameters
        this.input = input;
        this.createEmbeddingResponse = createEmbeddingResponse;

        // Extract Data
        model = createEmbeddingResponse.model();
        usage = createEmbeddingResponse.usage();

        // Extract Usage
        promptTokens = usage.promptTokens();
        totalTokens = usage.totalTokens();

        // Extract Content
        embeddings = new ArrayList<>(createEmbeddingResponse.data());
    }

}
