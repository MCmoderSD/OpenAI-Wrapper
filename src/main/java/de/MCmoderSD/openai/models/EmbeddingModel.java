package de.MCmoderSD.openai.models;

import de.MCmoderSD.openai.enums.Input;
import de.MCmoderSD.openai.enums.Output;
import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

/**
 * Enum representing different embedding models with their respective performance, speed, cost, and dimensions.
 */
@SuppressWarnings("unused")
public enum EmbeddingModel {

    // Enum Values
    TEXT_EMBEDDING_ADA_002(com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_ADA_002, Performance.LOW, Speed.SLOW, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 10, 1536, 1536),
    TEXT_EMBEDDING_3_SMALL(com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_3_SMALL, Performance.AVERAGE, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 2, 1536, 1536),
    TEXT_EMBEDDING_3_LARGE(com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_3_LARGE, Performance.HIGH, Speed.SLOW, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 13, 1, 3072);

    // Attributes
    private final com.openai.models.embeddings.EmbeddingModel model;
    private final Performance performance;
    private final Speed speed;
    private final HashSet<Input> inputs;
    private final HashSet<Output> outputs;
    private final BigDecimal cost;
    private final int minDimensions;
    private final int maxDimensions;

    /**
     * Constructs an {@code EmbeddingModel} with the specified attributes.
     *
     * @param model          The underlying embedding model.
     * @param performance    The performance level of the model.
     * @param speed          The speed of the model.
     * @param inputs         The supported input types.
     * @param outputs        The supported output types.
     * @param centPerMillionTokens The cost per million tokens in cents.
     * @param minDimensions  The minimum dimensions for the embedding.
     * @param maxDimensions  The maximum dimensions for the embedding.
     */
    EmbeddingModel(com.openai.models.embeddings.EmbeddingModel model, Performance performance, Speed speed, HashSet<Input> inputs, HashSet<Output> outputs, int centPerMillionTokens, int minDimensions, int maxDimensions) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
        this.inputs = inputs;
        this.outputs = outputs;
        this.minDimensions = minDimensions;
        this.maxDimensions = maxDimensions;
        this.cost = BigDecimal.valueOf(centPerMillionTokens).movePointLeft(8);
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
     * Checks if the model supports the specified input type.
     *
     * @param input The input type to check.
     * @return {@code true} if the input type is supported, {@code false} otherwise.
     */
    public boolean hasInput(Input input) {
        return inputs.contains(input);
    }

    /**
     * Checks if the model supports the specified output type.
     *
     * @param output The output type to check.
     * @return {@code true} if the output type is supported, {@code false} otherwise.
     */
    public boolean hasOutput(Output output) {
        return outputs.contains(output);
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
        throw new IllegalArgumentException("Invalid EmbeddingModel value: " + model);
    }
}