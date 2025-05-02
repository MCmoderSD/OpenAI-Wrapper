package de.MCmoderSD.openai.models;

import com.openai.models.ChatModel;
import de.MCmoderSD.openai.enums.Input;
import de.MCmoderSD.openai.enums.Output;
import de.MCmoderSD.openai.enums.Reasoning;
import de.MCmoderSD.openai.enums.Speed;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static de.MCmoderSD.openai.other.Dates.*;

/**
 * The {@code SearchModel} enum represents various search models available in the OpenAI API.
 * Each model is characterized by its attributes such as intelligence, speed, input/output types,
 * cost, context window size, and knowledge cutoff date.
 *
 * <p>This enum provides utility methods to retrieve model details, calculate costs, and check
 * supported input/output types.
 */
@SuppressWarnings("unused")
public enum ReasoningModel {

    // Enum Values
    O4_MINI(ChatModel.O4_MINI, Reasoning.HIGHER, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 110, 27.5f, 440, 200000L, 100000L, KNOWLEDGE_CUTOFF_2024_06_01),
    O4_MINI_2025_04_16(ChatModel.O4_MINI_2025_04_16, Reasoning.HIGHER, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 110, 27.5f, 440, 200000L, 100000L, KNOWLEDGE_CUTOFF_2024_06_01),
    O3(ChatModel.O3, Reasoning.HIGHEST, Speed.SLOWEST, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 1000, 250, 4000, 200000L, 100000L, KNOWLEDGE_CUTOFF_2024_06_01),
    O3_2025_04_16(ChatModel.O3_2025_04_16, Reasoning.HIGHEST, Speed.SLOWEST, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 1000, 250, 4000, 200000L, 100000L, KNOWLEDGE_CUTOFF_2024_06_01),
    O3_MINI(ChatModel.O3_MINI, Reasoning.HIGHER, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 110, 55, 440, 200000L, 100000L, KNOWLEDGE_CUTOFF_2023_10_01),
    O3_MINI_2025_01_31(ChatModel.O3_MINI_2025_01_31, Reasoning.HIGHER, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 110, 55, 440, 200000L, 100000L, KNOWLEDGE_CUTOFF_2023_10_01),
    O1(ChatModel.O1, Reasoning.HIGHER, Speed.SLOWEST, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 1500, 750, 6000, 200000L, 100000L, KNOWLEDGE_CUTOFF_2023_10_01),
    O1_2024_12_17(ChatModel.O1_2024_12_17, Reasoning.HIGHER, Speed.SLOWEST, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 1500, 750, 6000, 200000L, 100000L, KNOWLEDGE_CUTOFF_2023_10_01),
    O1_PREVIEW(ChatModel.O1_PREVIEW, Reasoning.HIGH, Speed.SLOWEST, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 1500, 750, 6000, 128000L, 32768L, KNOWLEDGE_CUTOFF_2023_10_01),
    O1_PREVIEW_2024_09_12(ChatModel.O1_PREVIEW_2024_09_12, Reasoning.HIGH, Speed.SLOWEST, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 1500, 750, 6000, 128000L, 32768L, KNOWLEDGE_CUTOFF_2023_10_01),
    O1_MINI(ChatModel.O1_MINI, Reasoning.HIGH, Speed.SLOW, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 110, 55, 440, 128000L, 65536L, KNOWLEDGE_CUTOFF_2023_10_01),
    O1_MINI_2024_09_12(ChatModel.O1_MINI_2024_09_12, Reasoning.HIGH, Speed.SLOW, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)), 110, 55, 440, 128000L, 65536L, KNOWLEDGE_CUTOFF_2023_10_01);

    // Attributes
    private final ChatModel model;
    private final Reasoning reasoning;
    private final Speed speed;
    private final HashSet<Input> inputs;
    private final HashSet<Output> outputs;
    private final BigDecimal inputCost;
    private final BigDecimal cachedInputCost;
    private final BigDecimal outputCost;
    private final long contextWindow;
    private final long maxOutputTokens;
    private final Date knowledgeCutoff;

    /**
     * Constructs a new {@code ReasoningModel} with the specified attributes.
     *
     * @param model                           The underlying chat model.
     * @param reasoning                       The reasoning capability of the model.
     * @param speed                           The speed of the model.
     * @param inputs                          The supported input types.
     * @param outputs                         The supported output types.
     * @param inputCentPerMillionTokens       The cost per million tokens for input in cents.
     * @param cachedInputCentPerMillionTokens The cost per million tokens for cached input in cents.
     * @param outputCentPerMillionTokens      The cost per million tokens for output in cents.
     * @param contextWindow                   The context window size.
     * @param maxOutputTokens                 The maximum number of output tokens.
     * @param knowledgeCutoff                 The knowledge cutoff date.
     */
    ReasoningModel(ChatModel model, Reasoning reasoning, Speed speed, HashSet<Input> inputs, HashSet<Output> outputs, int inputCentPerMillionTokens, int cachedInputCentPerMillionTokens, int outputCentPerMillionTokens, long contextWindow, long maxOutputTokens, Date knowledgeCutoff) {
        this.model = model;
        this.reasoning = reasoning;
        this.speed = speed;
        this.inputs = inputs;
        this.outputs = outputs;
        this.inputCost = BigDecimal.valueOf(inputCentPerMillionTokens).movePointLeft(8);
        this.cachedInputCost = BigDecimal.valueOf(cachedInputCentPerMillionTokens).movePointLeft(8);
        this.outputCost = BigDecimal.valueOf(outputCentPerMillionTokens).movePointLeft(8);
        this.contextWindow = contextWindow;
        this.maxOutputTokens = maxOutputTokens;
        this.knowledgeCutoff = knowledgeCutoff;
    }

    /**
     * Constructs a new {@code ReasoningModel} with the specified attributes.
     *
     * @param model                           The underlying chat model.
     * @param reasoning                       The reasoning capability of the model.
     * @param speed                           The speed of the model.
     * @param inputs                          The supported input types.
     * @param outputs                         The supported output types.
     * @param inputCentPerMillionTokens       The cost per million tokens for input in cents.
     * @param cachedInputCentPerMillionTokens The cost per million tokens for cached input in cents.
     * @param outputCentPerMillionTokens      The cost per million tokens for output in cents.
     * @param contextWindow                   The context window size.
     * @param maxOutputTokens                 The maximum number of output tokens.
     * @param knowledgeCutoff                 The knowledge cutoff date.
     */
    ReasoningModel(ChatModel model, Reasoning reasoning, Speed speed, HashSet<Input> inputs, HashSet<Output> outputs, float inputCentPerMillionTokens, float cachedInputCentPerMillionTokens, float outputCentPerMillionTokens, long contextWindow, long maxOutputTokens, Date knowledgeCutoff) {
        this.model = model;
        this.reasoning = reasoning;
        this.speed = speed;
        this.inputs = inputs;
        this.outputs = outputs;
        this.inputCost = BigDecimal.valueOf(inputCentPerMillionTokens).movePointLeft(8);
        this.cachedInputCost = BigDecimal.valueOf(cachedInputCentPerMillionTokens).movePointLeft(8);
        this.outputCost = BigDecimal.valueOf(outputCentPerMillionTokens).movePointLeft(8);
        this.contextWindow = contextWindow;
        this.maxOutputTokens = maxOutputTokens;
        this.knowledgeCutoff = knowledgeCutoff;
    }

    /**
     * Gets the underlying chat model.
     *
     * @return The chat model.
     */
    public ChatModel getModel() {
        return model;
    }

    /**
     * Gets the name of the model.
     *
     * @return The model name.
     */
    public String getName() {
        return model.asString();
    }

    /**
     * Gets the reasoning capability of the model.
     *
     * @return The reasoning capability.
     */
    public Reasoning getReasoning() {
        return reasoning;
    }

    /**
     * Gets the speed of the model.
     *
     * @return The speed.
     */
    public Speed getSpeed() {
        return speed;
    }

    /**
     * Gets the supported input types.
     *
     * @return The input types.
     */
    public HashSet<Input> getInputs() {
        return inputs;
    }

    /**
     * Gets the supported output types.
     *
     * @return The output types.
     */
    public HashSet<Output> getOutputs() {
        return outputs;
    }

    /**
     * Gets the cost per million tokens for input.
     *
     * @return The input cost.
     */
    public BigDecimal getInputCost() {
        return inputCost;
    }

    /**
     * Gets the cost per million tokens for cached input.
     *
     * @return The cached input cost.
     */
    public BigDecimal getCachedInputCost() {
        return cachedInputCost;
    }

    /**
     * Gets the cost per million tokens for output.
     *
     * @return The output cost.
     */
    public BigDecimal getOutputCost() {
        return outputCost;
    }

    /**
     * Gets the context window size.
     *
     * @return The context window size.
     */
    public long getContextWindow() {
        return contextWindow;
    }

    /**
     * Gets the maximum number of output tokens supported by the model.
     *
     * @return The maximum output tokens.
     */
    public long getMaxOutputTokens() {
        return maxOutputTokens;
    }

    /**
     * Gets the knowledge cutoff date for the model.
     *
     * @return The knowledge cutoff date.
     */
    public Date getKnowledgeCutoff() {
        return knowledgeCutoff;
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
     * Calculates the cost for the specified number of input tokens.
     *
     * @param tokens The number of input tokens.
     * @return The calculated input cost.
     */
    public BigDecimal getInputCost(long tokens) {
        return inputCost.multiply(BigDecimal.valueOf(tokens));
    }

    /**
     * Calculates the cost for the specified number of output tokens.
     *
     * @param tokens The number of output tokens.
     * @return The calculated output cost.
     */
    public BigDecimal getOutputCost(long tokens) {
        return outputCost.multiply(BigDecimal.valueOf(tokens));
    }

    /**
     * Retrieves a {@code ReasoningModel} by its name.
     *
     * @param model The name of the model.
     * @return The corresponding {@code ReasoningModel}.
     * @throws IllegalArgumentException If the model name is invalid.
     */
    public static ReasoningModel getModel(String model) {
        if (model == null || model.isBlank()) return null;
        for (ReasoningModel m : ReasoningModel.values()) if (m.getName().equalsIgnoreCase(model)) return m;
        throw new IllegalArgumentException("Invalid ReasoningModel value: " + model);
    }
}