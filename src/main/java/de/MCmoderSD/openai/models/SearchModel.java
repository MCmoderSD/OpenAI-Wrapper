package de.MCmoderSD.openai.models;

import com.openai.models.ChatModel;
import de.MCmoderSD.openai.enums.Input;
import de.MCmoderSD.openai.enums.Output;
import de.MCmoderSD.openai.enums.Intelligence;
import de.MCmoderSD.openai.enums.Speed;
import de.MCmoderSD.openai.enums.SearchContextSize;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static de.MCmoderSD.openai.other.Dates.KNOWLEDGE_CUTOFF_2023_10_01;

/**
 * The {@code SearchModel} enum represents various search models available in the OpenAI API.
 * Each model is characterized by its attributes such as intelligence, speed, input/output types,
 * cost, context window size, and knowledge cutoff date.
 *
 * <p>This enum provides utility methods to retrieve model details, calculate costs, and check
 * supported input/output types.
 */
@SuppressWarnings("unused")
public enum SearchModel {

    // Enum Values
    GPT_4O_SEARCH_PREVIEW(ChatModel.GPT_4O_SEARCH_PREVIEW, Intelligence.HIGH, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 250, 1000, 3000, 3500, 5000, 128000L, 16384L, KNOWLEDGE_CUTOFF_2023_10_01),
    GPT_4O_SEARCH_PREVIEW_2025_03_11(ChatModel.GPT_4O_SEARCH_PREVIEW_2025_03_11, Intelligence.HIGH, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 250, 1000, 3000, 3500, 5000, 128000L, 16384L, KNOWLEDGE_CUTOFF_2023_10_01),
    GPT_4O_MINI_SEARCH_PREVIEW(ChatModel.GPT_4O_MINI_SEARCH_PREVIEW, Intelligence.AVERAGE, Speed.FAST, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 15, 60, 2500, 2750, 3000, 128000L, 16384L, KNOWLEDGE_CUTOFF_2023_10_01),
    GPT_4O_MINI_SEARCH_PREVIEW_2025_03_11(ChatModel.GPT_4O_MINI_SEARCH_PREVIEW_2025_03_11, Intelligence.AVERAGE, Speed.FAST, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT)), 15, 60, 2500, 2750, 3000, 128000L, 16384L, KNOWLEDGE_CUTOFF_2023_10_01);

    // Attributes
    private final ChatModel model;
    private final Intelligence intelligence;
    private final Speed speed;
    private final HashSet<Input> inputs;
    private final HashSet<Output> outputs;
    private final BigDecimal inputCost;
    private final BigDecimal outputCost;
    private final BigDecimal lowSearchCost;
    private final BigDecimal mediumSearchCost;
    private final BigDecimal highSearchCost;
    private final long contextWindow;
    private final long maxOutputTokens;
    private final Date knowledgeCutoff;

    /**
     * Constructs a {@code SearchModel} with the specified attributes.
     *
     * @param model The underlying chat model.
     * @param intelligence The intelligence level of the model.
     * @param speed The speed of the model.
     * @param inputs The supported input types.
     * @param outputs The supported output types.
     * @param inputCentPerMillionTokens The cost per million input tokens in cents.
     * @param outputCentPerMillionTokens The cost per million output tokens in cents.
     * @param lowSearchCentCostPerThousand The low search cost per thousand calls in cents.
     * @param mediumSearchCentCostPerThousand The medium search cost per thousand calls in cents.
     * @param highSearchCentCostPerThousand The high search cost per thousand calls in cents.
     * @param contextWindow The maximum context window size in tokens.
     * @param maxOutputTokens The maximum number of output tokens supported by the model.
     * @param knowledgeCutoff The knowledge cutoff date for the model.
     */
    SearchModel(ChatModel model, Intelligence intelligence, Speed speed, HashSet<Input> inputs, HashSet<Output> outputs, int inputCentPerMillionTokens, int outputCentPerMillionTokens, int lowSearchCentCostPerThousand, int mediumSearchCentCostPerThousand, int highSearchCentCostPerThousand, long contextWindow, long maxOutputTokens, Date knowledgeCutoff) {
        this.model = model;
        this.intelligence = intelligence;
        this.speed = speed;
        this.inputs = inputs;
        this.outputs = outputs;
        this.inputCost = BigDecimal.valueOf(inputCentPerMillionTokens).movePointLeft(8);
        this.outputCost = BigDecimal.valueOf(outputCentPerMillionTokens).movePointLeft(8);
        this.lowSearchCost = BigDecimal.valueOf(lowSearchCentCostPerThousand).movePointLeft(5);
        this.mediumSearchCost = BigDecimal.valueOf(mediumSearchCentCostPerThousand).movePointLeft(5);
        this.highSearchCost = BigDecimal.valueOf(highSearchCentCostPerThousand).movePointLeft(5);
        this.contextWindow = contextWindow;
        this.maxOutputTokens = maxOutputTokens;
        this.knowledgeCutoff = knowledgeCutoff;
    }

    /**
     * Returns the underlying chat model.
     *
     * @return The chat model.
     */
    public ChatModel getModel() {
        return model;
    }

    /**
     * Returns the name of the model.
     *
     * @return The model name.
     */
    public String getName() {
        return model.asString();
    }

    /**
     * Returns the intelligence level of the model.
     *
     * @return The intelligence level.
     */
    public Intelligence getIntelligence() {
        return intelligence;
    }

    /**
     * Returns the speed of the model.
     *
     * @return The speed.
     */
    public Speed getSpeed() {
        return speed;
    }

    /**
     * Returns the supported input types for the model.
     *
     * @return A set of supported input types.
     */
    public HashSet<Input> getInputs() {
        return inputs;
    }

    /**
     * Returns the supported output types for the model.
     *
     * @return A set of supported output types.
     */
    public HashSet<Output> getOutputs() {
        return outputs;
    }

    /**
     * Returns the cost per input token.
     *
     * @return The input cost.
     */
    public BigDecimal getInputCost() {
        return inputCost;
    }

    /**
     * Returns the cost per output token.
     *
     * @return The output cost.
     */
    public BigDecimal getOutputCost() {
        return outputCost;
    }

    /**
     * Returns the cost of search operations based on the specified context size.
     *
     * @param searchContextSize The context size for the search operation.
     * @return The cost of the search operation.
     */
    public BigDecimal getSearchCost(SearchContextSize searchContextSize) {
        return switch (searchContextSize) {
            case LOW -> lowSearchCost;
            case MEDIUM -> mediumSearchCost;
            case HIGH -> highSearchCost;
        };
    }

    /**
     * Returns the maximum context window size in tokens.
     *
     * @return The context window size.
     */
    public long getContextWindow() {
        return contextWindow;
    }

    /**
     * Returns the maximum number of output tokens supported by the model.
     *
     * @return The maximum output tokens.
     */
    public long getMaxOutputTokens() {
        return maxOutputTokens;
    }

    /**
     * Returns the knowledge cutoff date for the model.
     *
     * @return The knowledge cutoff date.
     */
    public Date getKnowledgeCutoff() {
        return knowledgeCutoff;
    }

    /**
     * Checks if the model supports a specific input type.
     *
     * @param input The input type to check.
     * @return True if the input type is supported, false otherwise.
     */
    public boolean hasInput(Input input) {
        return inputs.contains(input);
    }

    /**
     * Checks if the model supports a specific output type.
     *
     * @param output The output type to check.
     * @return True if the output type is supported, false otherwise.
     */
    public boolean hasOutput(Output output) {
        return outputs.contains(output);
    }

    /**
     * Calculates the cost for a given number of input tokens and search context size.
     *
     * @param tokens The number of input tokens.
     * @param searchContextSize The search context size.
     * @return The total cost for the input tokens and search operation.
     */
    public BigDecimal getInputCost(long tokens, SearchContextSize searchContextSize) {
        return inputCost.multiply(BigDecimal.valueOf(tokens)).add(getSearchCost(searchContextSize));
    }

    /**
     * Calculates the cost for a given number of output tokens and search context size.
     *
     * @param tokens The number of output tokens.
     * @param searchContextSize The search context size.
     * @return The total cost for the output tokens and search operation.
     */
    public BigDecimal getOutputCost(long tokens, SearchContextSize searchContextSize) {
        return outputCost.multiply(BigDecimal.valueOf(tokens)).add(getSearchCost(searchContextSize));
    }

    /**
     * Retrieves a `SearchModel` instance by its name.
     *
     * @param model The name of the model.
     * @return The corresponding `SearchModel` instance, or null if the name is blank.
     * @throws IllegalArgumentException If the model name is invalid.
     */
    public static SearchModel getModel(String model) {
        if (model == null || model.isBlank()) return null;
        for (SearchModel m : SearchModel.values()) if (m.getName().equalsIgnoreCase(model)) return m;
        throw new IllegalArgumentException("Invalid SearchModel value: " + model);
    }
}