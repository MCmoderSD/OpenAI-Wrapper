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
 * Enum representing different audio models with their performance, speed, and cost.
 */
@SuppressWarnings("unused")
public enum AudioModel {

    // Enum Values
    WHISPER_1(com.openai.models.audio.AudioModel.WHISPER_1, Performance.AVERAGE, Speed.MEDIUM, new HashSet<>(List.of(Input.AUDIO)), new HashSet<>(List.of(Output.TEXT)), 0.006f),
    GPT_4O_MINI_TRANSCRIBE(com.openai.models.audio.AudioModel.GPT_4O_MINI_TRANSCRIBE, Performance.HIGH, Speed.FAST, new HashSet<>(List.of(Input.TEXT, Input.AUDIO)), new HashSet<>(List.of(Output.TEXT)), 300),
    GPT_4O_TRANSCRIBE(com.openai.models.audio.AudioModel.GPT_4O_TRANSCRIBE, Performance.HIGHER, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT, Input.AUDIO)), new HashSet<>(List.of(Output.TEXT)), 600);

    // Attributes
    private final com.openai.models.audio.AudioModel model;
    private final Performance performance;
    private final Speed speed;
    private final HashSet<Input> inputs;
    private final HashSet<Output> outputs;
    private final BigDecimal cost;

    /**
     * Constructs an {@code AudioModel} with the specified attributes.
     *
     * @param model                      The underlying audio model.
     * @param performance                The performance level of the model.
     * @param speed                      The speed of the model.
     * @param inputs                     The supported input types.
     * @param outputs                    The supported output types.
     * @param centPerMillionTokens       The cost per million tokens in cents.
     */
    AudioModel(com.openai.models.audio.AudioModel model, Performance performance, Speed speed, HashSet<Input> inputs, HashSet<Output> outputs, float centPerMillionTokens) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
        this.inputs = inputs;
        this.outputs = outputs;
        this.cost = BigDecimal.valueOf(centPerMillionTokens).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP);
    }

    /**
     * Constructs an {@code AudioModel} with the specified attributes.
     *
     * @param model                      The underlying audio model.
     * @param performance                The performance level of the model.
     * @param speed                      The speed of the model.
     * @param inputs                     The supported input types.
     * @param outputs                    The supported output types.
     * @param centPerMillionTokens       The cost per million tokens in cents.
     */
    AudioModel(com.openai.models.audio.AudioModel model, Performance performance, Speed speed, HashSet<Input> inputs, HashSet<Output> outputs, int centPerMillionTokens) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
        this.inputs = inputs;
        this.outputs = outputs;
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
        throw new IllegalArgumentException("Invalid AudioModel value: " + model);
    }
}