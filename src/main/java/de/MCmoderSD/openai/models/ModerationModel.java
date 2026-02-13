package de.MCmoderSD.openai.models;

import de.MCmoderSD.openai.enums.Modality;
import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

import java.util.HashSet;
import java.util.List;

import static de.MCmoderSD.openai.enums.Performance.*;
import static de.MCmoderSD.openai.enums.Speed.*;
import static de.MCmoderSD.openai.enums.Modality.*;

@SuppressWarnings("unused")
public enum ModerationModel {

    // Enum Values
    //@Deprecated TEXT_MODERATION_STABLE(AVERAGE, MEDIUM, List.of(TEXT), List.of(TEXT)),
    //@Deprecated TEXT_MODERATION_LATEST(AVERAGE, MEDIUM, List.of(TEXT), List.of(TEXT)),
    OMNI_MODERATION_2024_09_26(HIGH, MEDIUM, List.of(TEXT, IMAGE), List.of(TEXT)),
    OMNI_MODERATION_LATEST(HIGH, MEDIUM, List.of(TEXT, IMAGE), List.of(TEXT));

    // Attributes
    private final Performance performance;
    private final Speed speed;
    private final HashSet<Modality> input;
    private final HashSet<Modality> output;
    private final String name;
    private final com.openai.models.moderations.ModerationModel model;

    // Constructor
    ModerationModel(
            Performance performance,    // Performance
            Speed speed,                // Speed
            List<Modality> input,       // Supported Input Modalities
            List<Modality> output       // Supported Output Modalities
    ) {
        // Set Attributes
        this.performance = performance;
        this.speed = speed;

        // Set Modalities
        this.input = new HashSet<>(input);
        this.output = new HashSet<>(output);

        // Parse Name
        this.name = name().toLowerCase().replace('_', '-');

        // Parse Model
        this.model = com.openai.models.moderations.ModerationModel.of(name());
    }

    // Getter
    public com.openai.models.moderations.ModerationModel getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public Performance getPerformance() {
        return performance;
    }

    public Speed getSpeed() {
        return speed;
    }

    public HashSet<Modality> getInput() {
        return input;
    }

    public HashSet<Modality> getOutput() {
        return output;
    }

    public boolean hasInput(Modality... modality) {
        return input.containsAll(List.of(modality));
    }

    public boolean hasOutput(Modality... modality) {
        return output.containsAll(List.of(modality));
    }

    // Static Methods
    public static ModerationModel getModel(String name) {
        for (var model : ModerationModel.values()) if (model.getName().equalsIgnoreCase(name)) return model;
        throw new IllegalArgumentException("Invalid embedding model name: " + name);
    }
}