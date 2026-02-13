package de.MCmoderSD.openai.prompts;

import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.CreateEmbeddingResponse.Usage;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.EmbeddingCreateParams.EncodingFormat;
import de.MCmoderSD.openai.models.EmbeddingModel;
import de.MCmoderSD.openai.objects.Embedding;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class EmbeddingPrompt {

    // Parameters
    private final EmbeddingCreateParams input;
    private final CreateEmbeddingResponse output;

    // Input
    private final EncodingFormat format;
    private final String user;
    private final int dimension;
    private final String text;

    // Output
    private final EmbeddingModel model;
    private final Usage usage;

    // Usage
    private final long promptTokens;
    private final long totalTokens;

    // Cost
    private final BigDecimal promptCost;
    private final BigDecimal totalCost;

    // Content
    private final Embedding embedding;

    // Constructor
    public EmbeddingPrompt(EmbeddingCreateParams input, CreateEmbeddingResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Content
        embedding = new Embedding(Embedding.convertToFloatArray(output.data().getFirst().embedding()));

        // Extract Input
        format = input.encodingFormat().orElse(null);
        user = input.user().orElse(null);
        dimension = Math.toIntExact(input.dimensions().orElse((long) embedding.getDimension()));
        text = input.input().asString();

        // Extract Output
        model = EmbeddingModel.getModel(output.model());
        usage = output.usage();

        // Extract Usage
        promptTokens = usage.promptTokens();
        totalTokens = usage.totalTokens();

        // Calculate Cost
        promptCost = model.getCost(promptTokens);
        totalCost = model.getCost(totalTokens);
    }

    // Getter
    public EmbeddingCreateParams getInput() {
        return input;
    }

    public CreateEmbeddingResponse getOutput() {
        return output;
    }

    public EncodingFormat getEncodingFormat() {
        return format;
    }

    public String getUser() {
        return user;
    }

    public int getDimension() {
        return dimension;
    }

    public String getText() {
        return text;
    }

    public EmbeddingModel getModel() {
        return model;
    }

    public Usage getUsage() {
        return usage;
    }

    public long getPromptTokens() {
        return promptTokens;
    }

    public long getTotalTokens() {
        return totalTokens;
    }

    public BigDecimal getPromptCost() {
        return promptCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public Embedding getEmbedding() {
        return embedding;
    }
}