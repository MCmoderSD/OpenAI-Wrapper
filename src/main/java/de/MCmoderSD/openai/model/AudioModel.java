package de.MCmoderSD.openai.model;

import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Enum representing different audio models with their performance, speed, and cost.
 */
@SuppressWarnings("unused")
public enum AudioModel {

    // Enum Values
    WHISPER_1(com.openai.models.audio.AudioModel.WHISPER_1, Performance.AVERAGE, Speed.MEDIUM, 0.006f),
    GPT_4O_MINI_TRANSCRIBE(com.openai.models.audio.AudioModel.GPT_4O_MINI_TRANSCRIBE, Performance.HIGH, Speed.FAST, 300),
    GPT_4O_TRANSCRIBE(com.openai.models.audio.AudioModel.GPT_4O_TRANSCRIBE, Performance.HIGHER, Speed.MEDIUM, 600);

    // Attributes
    private final com.openai.models.audio.AudioModel model;
    private final Performance performance;
    private final Speed speed;
    private final BigDecimal cost;

    /**
     * Constructor for the AudioModel enum.
     *
     * @param model the audio model
     * @param performance the performance level
     * @param speed the speed level
     * @param centPerMillionTokens the cost in cents per million tokens
     */
    AudioModel(com.openai.models.audio.AudioModel model, Performance performance, Speed speed, float centPerMillionTokens) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
        this.cost = BigDecimal.valueOf(centPerMillionTokens).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
    }

    /**
     * Constructor for the AudioModel enum.
     *
     * @param model the audio model
     * @param performance the performance level
     * @param speed the speed level
     * @param centPerMillionTokens the cost in cents per million tokens
     */
    AudioModel(com.openai.models.audio.AudioModel model, Performance performance, Speed speed, int centPerMillionTokens) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
        this.cost = BigDecimal.valueOf(centPerMillionTokens).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
    }

    /**
     * Gets the audio model.
     *
     * @return the audio model
     */
    public com.openai.models.audio.AudioModel getModel() {
        return model;
    }

    /**
     * Gets the name of the audio model.
     *
     * @return the name of the audio model
     */
    public String getName() {
        return model.asString();
    }

    /**
     * Gets the performance level.
     *
     * @return the performance level
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Gets the speed level.
     *
     * @return the speed level
     */
    public Speed getSpeed() {
        return speed;
    }

    /**
     * Gets the cost per token.
     *
     * @return the cost per token
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Gets the cost for a given number of tokens.
     *
     * @param tokens the number of tokens
     * @return the cost for the given number of tokens
     */
    public BigDecimal getCost(long tokens) {
        return cost.multiply(BigDecimal.valueOf(tokens));
    }

    /**
     * Gets the AudioModel enum value corresponding to the given model name.
     *
     * @param model the model name
     * @return the corresponding AudioModel enum value
     * @throws IllegalArgumentException if the model name is invalid
     */
    public static AudioModel getModel(String model) {
        if (model == null || model.isBlank()) return null;
        for (AudioModel m : AudioModel.values()) if (m.getName().equalsIgnoreCase(model)) return m;
        throw new IllegalArgumentException("Invalid SpeechModel value: " + model);
    }
}