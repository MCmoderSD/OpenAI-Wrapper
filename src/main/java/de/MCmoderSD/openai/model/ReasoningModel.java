package de.MCmoderSD.openai.model;

import com.openai.models.ChatModel;
import de.MCmoderSD.openai.enums.Intelligence;
import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Reasoning;
import de.MCmoderSD.openai.enums.Speed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;

public enum ReasoningModel {

    // Enum Values
    O1(ChatModel.O1, Reasoning.HIGHER, Speed.SLOWEST, 1500, 750, 6000, 200000, 100000, new Date(2023, 10, 1), true),
    O1_PREVIEW_2024_09_12(ChatModel.O1_PREVIEW_2024_09_12, Reasoning.HIGH, Speed.SLOWEST, 1500, 750, 6000, 128000, 32768, new Date(2023, 10, 1), true);


    // Attributes
    private final ChatModel model;
    private final Reasoning reasoning;
    private final Speed speed;
    private final BigDecimal inputTokenCost;
    private final BigDecimal cachedInputTokenCost;
    private final BigDecimal outputTokenCost;
    private final int contextWindow;
    private final int maxOutputTokens;
    private final Date knowledgeCutoff;

    // Features
    private final boolean reasoningTokenSupport;

    // Constructor
    ReasoningModel(ChatModel model, Reasoning reasoning, Speed speed, int inputTokenCost, int cachedInputTokenCost, int outputTokenCost, int contextWindow, int maxOutputTokens, Date knowledgeCutoff, boolean reasoningTokenSupport) {

        // Set the values
        this.model = model;
        this.reasoning = reasoning;
        this.speed = speed;
        this.contextWindow = contextWindow;
        this.maxOutputTokens = maxOutputTokens;
        this.knowledgeCutoff = knowledgeCutoff;

        // Calculate the cost per token
        this.inputTokenCost = BigDecimal.valueOf(inputTokenCost).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
        this.cachedInputTokenCost = BigDecimal.valueOf(cachedInputTokenCost).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
        this.outputTokenCost = BigDecimal.valueOf(outputTokenCost).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);

        // Features
        this.reasoningTokenSupport = reasoningTokenSupport;
    }

    // Getters
    public ChatModel getModel() {
        return model;
    }

    public String getName() {
        return model.asString();
    }

    public Reasoning getReasoning() {
        return reasoning;
    }

    public Speed getSpeed() {
        return speed;
    }

    public BigDecimal getInputTokenCost() {
        return inputTokenCost;
    }

    public BigDecimal getCachedInputTokenCost() {
        return cachedInputTokenCost;
    }

    public BigDecimal getOutputTokenCost() {
        return outputTokenCost;
    }

    public BigDecimal getInputCost(int tokens) {
        return inputTokenCost.multiply(BigDecimal.valueOf(tokens));
    }

    public BigDecimal getCachedInputCost(int tokens) {
        return cachedInputTokenCost.multiply(BigDecimal.valueOf(tokens));
    }

    public BigDecimal getOutputCost(int tokens) {
        return outputTokenCost.multiply(BigDecimal.valueOf(tokens));
    }

    public int getContextWindow() {
        return contextWindow;
    }

    public int getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public Date getKnowledgeCutoff() {
        return knowledgeCutoff;
    }

    public static ReasoningModel getModel(String model) {
        for (ReasoningModel m : ReasoningModel.values()) if (m.getName().equalsIgnoreCase(model)) return m;
        throw new IllegalArgumentException("Invalid SpeechModel value: " + model);
    }
}