package de.MCmoderSD.openai.model;

import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

/**
 * Enum representing different Moderation Models.
 */
@SuppressWarnings("unused")
public enum ModerationModel {

    // Enum Values
    OMNI_MODERATION_LATEST(com.openai.models.moderations.ModerationModel.OMNI_MODERATION_LATEST, Performance.HIGH, Speed.MEDIUM),
    OMNI_MODERATION_2024_09_26(com.openai.models.moderations.ModerationModel.OMNI_MODERATION_2024_09_26, Performance.HIGH, Speed.MEDIUM),
    TEXT_MODERATION_LATEST(com.openai.models.moderations.ModerationModel.TEXT_MODERATION_LATEST, Performance.AVERAGE, Speed.MEDIUM),
    TEXT_MODERATION_STABLE(com.openai.models.moderations.ModerationModel.TEXT_MODERATION_STABLE, Performance.AVERAGE, Speed.MEDIUM),;

    // Attributes
    private final com.openai.models.moderations.ModerationModel model;
    private final Performance performance;
    private final Speed speed;

    /**
     * Constructor for ModerationModel.
     *
     * @param model       The underlying model.
     * @param performance The performance level of the model.
     * @param speed       The speed of the model.
     */
    ModerationModel(com.openai.models.moderations.ModerationModel model, Performance performance, Speed speed) {
        this.model = model;
        this.performance = performance;
        this.speed = speed;
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