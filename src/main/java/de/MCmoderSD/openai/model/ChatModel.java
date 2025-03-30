package de.MCmoderSD.openai.model;

import de.MCmoderSD.openai.enums.Intelligence;
import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;
import de.MCmoderSD.openai.helper.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

public enum ChatModel {

    // Enum Values

    // Attributes
    private final com.openai.models.ChatModel model;
    private final Intelligence intelligence;
    private final Speed speed;
    private final BigDecimal inputTokenCost;
    private final BigDecimal cachedInputTokenCost;
    private final BigDecimal outputTokenCost;
    private final int contextWindow;
    private final int maxOutputTokens;
    private final Timestamp knowledgeCutoff;

    // Constructor
    ChatModel(com.openai.models.ChatModel model, Intelligence intelligence, Speed speed, int inputTokenCost, int cachedInputTokenCost, int outputTokenCost, int contextWindow, int maxOutputTokens, Timestamp knowledgeCutoff) {

        // Set the values
        this.model = model;
        this.intelligence = intelligence;
        this.speed = speed;
        this.contextWindow = contextWindow;
        this.maxOutputTokens = maxOutputTokens;
        this.knowledgeCutoff = knowledgeCutoff;

        // Calculate the cost per token
        this.inputTokenCost = BigDecimal.valueOf(inputTokenCost).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
        this.cachedInputTokenCost = BigDecimal.valueOf(cachedInputTokenCost).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
        this.outputTokenCost = BigDecimal.valueOf(outputTokenCost).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
    }

    // Getters
    public com.openai.models.ChatModel getModel() {
        return model;
    }

    public String getName() {
        return model.asString();
    }

    public Intelligence getIntelligence() {
        return intelligence;
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

    public Timestamp getKnowledgeCutoff() {
        return knowledgeCutoff;
    }

    public static ChatModel getModel(String model) {
        for (ChatModel m : ChatModel.values()) if (m.getName().equalsIgnoreCase(model)) return m;
        throw new IllegalArgumentException("Invalid SpeechModel value: " + model);
    }
}
