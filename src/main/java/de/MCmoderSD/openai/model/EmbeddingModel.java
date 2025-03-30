package de.MCmoderSD.openai.model;

import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Enum representing different embedding models with their respective performance, speed, cost, and dimensions.
 */
@SuppressWarnings("unused")
public enum EmbeddingModel {

    // Enum Values
    TEXT_EMBEDDING_ADA_002(com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_ADA_002, Performance.LOW, Speed.SLOW, 10, 1536, 1536),
    TEXT_EMBEDDING_3_SMALL(com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_3_SMALL, Performance.AVERAGE, Speed.MEDIUM, 2, 1536, 1536),
    TEXT_EMBEDDING_3_LARGE(com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_3_LARGE, Performance.HIGH, Speed.SLOW, 13, 1, 3072);

    // Attributes
    private final com.openai.models.embeddings.EmbeddingModel model;
    private final Performance performance;
    private final Speed speed;
    private final BigDecimal cost;
    private final int minDimensions;
    private final int maxDimensions;

    /**
     * Constructor for EmbeddingModel enum.
     *
     * @param model The embedding model.
     * @param performance The performance level.
     * @param speed The speed level.
     * @param centPerMillionTokens The cost in cents per million tokens.
     * @param minDimensions The minimum dimensions.
     * @param maxDimensions The maximum dimensions.
     */
    EmbeddingModel(com.openai.models.embeddings.EmbeddingModel model, Performance performance, Speed speed, int centPerMillionTokens, int minDimensions, int maxDimensions) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
        this.minDimensions = minDimensions;
        this.maxDimensions = maxDimensions;
        this.cost = BigDecimal.valueOf(centPerMillionTokens).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
    }

    /**
     * Gets the embedding model.
     *
     * @return The embedding model.
     */
    public com.openai.models.embeddings.EmbeddingModel getModel() {
        return model;
    }

    /**
     * Gets the name of the embedding model.
     *
     * @return The name of the embedding model.
     */
    public String getName() {
        return model.asString();
    }

    /**
     * Gets the performance level.
     *
     * @return The performance level.
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Gets the speed level.
     *
     * @return The speed level.
     */
    public Speed getSpeed() {
        return speed;
    }

    /**
     * Gets the cost per token.
     *
     * @return The cost per token.
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Gets the cost for a given number of tokens.
     *
     * @param tokens The number of tokens.
     * @return The cost for the given number of tokens.
     */
    public BigDecimal getCost(long tokens) {
        return cost.multiply(BigDecimal.valueOf(tokens));
    }

    /**
     * Gets the minimum dimensions.
     *
     * @return The minimum dimensions.
     */
    public int getMinDimensions() {
        return minDimensions;
    }

    /**
     * Gets the maximum dimensions.
     *
     * @return The maximum dimensions.
     */
    public int getMaxDimensions() {
        return maxDimensions;
    }

    /**
     * Checks if the given dimensions are valid.
     *
     * @param dimensions The dimensions to check.
     * @return True if the dimensions are valid, false otherwise.
     */
    public boolean isValidDimensions(int dimensions) {
        return dimensions >= minDimensions && dimensions <= maxDimensions;
    }

    /**
     * Gets the EmbeddingModel enum corresponding to the given model name.
     *
     * @param model The model name.
     * @return The corresponding EmbeddingModel enum.
     * @throws IllegalArgumentException If the model name is invalid.
     */
    public static EmbeddingModel getModel(String model) {
        if (model == null || model.isBlank()) return null;
        for (EmbeddingModel m : EmbeddingModel.values()) if (m.getName().equalsIgnoreCase(model)) return m;
        throw new IllegalArgumentException("Invalid SpeechModel value: " + model);
    }
}