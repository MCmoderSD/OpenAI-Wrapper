package de.MCmoderSD.openai.objects;

import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.CreateEmbeddingResponse.Usage;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.EmbeddingCreateParams.EncodingFormat;
import de.MCmoderSD.openai.models.EmbeddingModel;

import java.math.BigDecimal;

/**
 * Represents an embedding prompt containing input parameters, AI-generated response, and metadata.
 * This class encapsulates all information related to a single embedding interaction with the OpenAI API,
 * including tokens usage statistics, response content, and cost information.
 */
@SuppressWarnings("unused")
public class EmbeddingPrompt {

    // Parameters
    private final EmbeddingCreateParams input;
    private final CreateEmbeddingResponse output;

    // Input
    private final EncodingFormat format;
    private final String user;
    private final long dimensions;
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

    /**
     * Constructs a new EmbeddingPrompt with the specified input and output.
     *
     * @param input  The input parameters for creating the embedding
     * @param output The AI-generated response associated with the input
     */
    public EmbeddingPrompt(EmbeddingCreateParams input, CreateEmbeddingResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Input
        format = input.encodingFormat().orElse(null);
        user = input.user().orElse(null);
        dimensions = input.dimensions().orElse(0L);
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

        // Extract Content
        embedding = new Embedding(output.data().getFirst().embedding());
    }

    /**
     * Gets the input parameters for creating the embedding.
     *
     * @return The input parameters
     */
    public EmbeddingCreateParams getInput() {
        return input;
    }

    /**
     * Gets the AI-generated response associated with the input.
     *
     * @return The AI-generated response
     */
    public CreateEmbeddingResponse getOutput() {
        return output;
    }

    /**
     * Gets the encoding format used for the embedding.
     *
     * @return The encoding format
     */
    public EncodingFormat getEncodingFormat() {
        return format;
    }

    /**
     * Gets the user associated with the embedding request.
     *
     * @return The user
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the number of dimensions of the embedding.
     *
     * @return The number of dimensions
     */
    public long getDimensions() {
        return dimensions;
    }

    /**
     * Gets the input text for the embedding.
     *
     * @return The input text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the embedding model used for the response.
     *
     * @return The embedding model
     */
    public EmbeddingModel getModel() {
        return model;
    }

    /**
     * Gets the usage details of the embedding response.
     *
     * @return The usage details
     */
    public Usage getUsage() {
        return usage;
    }

    /**
     * Gets the number of prompt tokens used in the embedding response.
     *
     * @return The number of prompt tokens
     */
    public long getPromptTokens() {
        return promptTokens;
    }

    /**
     * Gets the total number of tokens used in the embedding response.
     *
     * @return The total number of tokens
     */
    public long getTotalTokens() {
        return totalTokens;
    }

    /**
     * Gets the cost associated with the prompt tokens.
     *
     * @return The prompt cost
     */
    public BigDecimal getPromptCost() {
        return promptCost;
    }

    /**
     * Gets the total cost associated with the embedding response.
     *
     * @return The total cost
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * Gets the embedding content from the response.
     *
     * @return The embedding content
     */
    public Embedding getEmbedding() {
        return embedding;
    }
}