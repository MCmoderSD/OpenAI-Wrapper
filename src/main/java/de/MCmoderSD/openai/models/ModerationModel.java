package de.MCmoderSD.openai.models;

import de.MCmoderSD.openai.enums.Input;
import de.MCmoderSD.openai.enums.Output;
import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

import java.util.HashSet;
import java.util.List;

/**
 * Enum representing different Moderation Models.
 */
@SuppressWarnings("unused")
public enum ModerationModel {

    // Enum Values
    OMNI_MODERATION_LATEST(com.openai.models.moderations.ModerationModel.OMNI_MODERATION_LATEST, Performance.HIGH, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT))),
    OMNI_MODERATION_2024_09_26(com.openai.models.moderations.ModerationModel.OMNI_MODERATION_2024_09_26, Performance.HIGH, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT, Input.IMAGE)), new HashSet<>(List.of(Output.TEXT))),
    TEXT_MODERATION_LATEST(com.openai.models.moderations.ModerationModel.TEXT_MODERATION_LATEST, Performance.AVERAGE, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT))),
    TEXT_MODERATION_STABLE(com.openai.models.moderations.ModerationModel.TEXT_MODERATION_STABLE, Performance.AVERAGE, Speed.MEDIUM, new HashSet<>(List.of(Input.TEXT)), new HashSet<>(List.of(Output.TEXT)));

    // Attributes
    private final com.openai.models.moderations.ModerationModel model;
    private final Performance performance;
    private final Speed speed;
    private final HashSet<Input> inputs;
    private final HashSet<Output> outputs;

    /**
     * Constructs a {@code ModerationModel} with the specified attributes.
     *
     * @param model    The underlying moderation model.
     * @param performance The performance level of the model.
     * @param speed    The speed of the model.
     * @param inputs   The supported input types.
     * @param outputs  The supported output types.
     */
    ModerationModel(com.openai.models.moderations.ModerationModel model, Performance performance, Speed speed, HashSet<Input> inputs, HashSet<Output> outputs) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    /**
     * Gets the underlying model.
     *
     * @return The underlying model.
     */
    public com.openai.models.moderations.ModerationModel getModel() {
        return model;
    }

    /**
     * Gets the name of the model.
     *
     * @return The name of the model.
     */
    public String getName() {
        return model.asString();
    }

    /**
     * Gets the performance level of the model.
     *
     * @return The performance level of the model.
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Gets the speed of the model.
     *
     * @return The speed of the model.
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
     * Gets the ModerationModel enum value based on the model name.
     *
     * @param model The name of the model.
     * @return The corresponding ModerationModel enum value.
     * @throws IllegalArgumentException If the model name is invalid.
     */
    public static ModerationModel getModel(String model) {
        if (model == null || model.isBlank()) return null;
        for (ModerationModel m : ModerationModel.values()) if (m.getName().equalsIgnoreCase(model)) return m;
        throw new IllegalArgumentException("Invalid ModerationModel value: " + model);
    }
}