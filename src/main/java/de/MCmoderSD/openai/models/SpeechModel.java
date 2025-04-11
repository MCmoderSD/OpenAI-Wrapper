package de.MCmoderSD.openai.models;

import de.MCmoderSD.openai.enums.Input;
import de.MCmoderSD.openai.enums.Output;
import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;

/**
 * Enum representing different speech models with their attributes and costs.
 */
@SuppressWarnings("unused")
public enum SpeechModel {

    // Enum Values
    TTS_1(com.openai.models.audio.speech.SpeechModel.TTS_1, Performance.AVERAGE, Speed.FAST, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.AUDIO)), 1500),
    TTS_1_HD(com.openai.models.audio.speech.SpeechModel.TTS_1_HD, Performance.HIGH, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.AUDIO)), 3000),
    GPT_4O_MINI_TTS(com.openai.models.audio.speech.SpeechModel.GPT_4O_MINI_TTS, Performance.HIGHER, Speed.FAST, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.AUDIO)), 1200);

    // Attributes
    private final com.openai.models.audio.speech.SpeechModel model;
    private final Performance performance;
    private final Speed speed;
    private final HashSet<Input> inputs;
    private final HashSet<Output> outputs;
    private final BigDecimal cost;

    /**
     * Constructs a {@code SpeechModel} with the specified attributes.
     *
     * @param model                      The underlying speech model.
     * @param performance                The performance level of the model.
     * @param speed                      The speed of the model.
     * @param inputs                     The supported input types.
     * @param outputs                    The supported output types.
     * @param centPerMillionTokens       The cost per million tokens in cents.
     */
    SpeechModel(com.openai.models.audio.speech.SpeechModel model, Performance performance, Speed speed, HashSet<Input> inputs, HashSet<Output> outputs, int centPerMillionTokens) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
        this.inputs = inputs;
        this.outputs = outputs;
        this.cost = BigDecimal.valueOf(centPerMillionTokens).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
    }

    /**
     * Gets the underlying speech model.
     *
     * @return The underlying speech model.
     */
    public com.openai.models.audio.speech.SpeechModel getModel() {
        return model;
    }

    /**
     * Gets the name of the speech model.
     *
     * @return The name of the speech model.
     */
    public String getName() {
        return model.asString();
    }

    /**
     * Gets the performance level of the speech model.
     *
     * @return The performance level of the speech model.
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Gets the speed of the speech model.
     *
     * @return The speed of the speech model.
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
     * Gets the cost per token of the speech model.
     *
     * @return The cost per token of the speech model.
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Calculates the cost for a given number of tokens.
     *
     * @param tokens The number of tokens.
     * @return The cost for the given number of tokens.
     */
    public BigDecimal getCost(long tokens) {
        return cost.multiply(BigDecimal.valueOf(tokens));
    }

    /**
     * Gets the SpeechModel enum value corresponding to the given model name.
     *
     * @param model The name of the model.
     * @return The corresponding SpeechModel enum value.
     * @throws IllegalArgumentException If the model name is invalid.
     */
    public static SpeechModel getModel(String model) {
        if (model == null || model.isBlank()) return null;
        for (SpeechModel m : SpeechModel.values()) if (m.getName().equalsIgnoreCase(model)) return m;
        throw new IllegalArgumentException("Invalid SpeechModel value: " + model);
    }
}