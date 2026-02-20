package de.MCmoderSD.openai.models;

import de.MCmoderSD.openai.enums.Modality;
import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

import java.util.HashSet;
import java.util.List;

import static de.MCmoderSD.openai.enums.Performance.*;
import static de.MCmoderSD.openai.enums.Speed.*;
import static de.MCmoderSD.openai.enums.Modality.*;
import static java.util.List.of;

@SuppressWarnings("unused")
public enum ModerationModel {

    OMNI_MODERATION_LATEST(
            HIGH,               // Performance
            MEDIUM,             // Speed
            of(TEXT, IMAGE),    // Supported Input Modalities
            of(TEXT)            // Supported Output Modalities
    ),

    OMNI_MODERATION_2024_09_26(
            HIGH,               // Performance
            MEDIUM,             // Speed
            of(TEXT, IMAGE),    // Supported Input Modalities
            of(TEXT)            // Supported Output Modalities
    ),

    @Deprecated TEXT_MODERATION_LATEST(
            AVERAGE,            // Performance
            MEDIUM,             // Speed
            of(TEXT),           // Supported Input Modalities
            of(TEXT)            // Supported Output Modalities
    ),

    @Deprecated TEXT_MODERATION_STABLE(
            AVERAGE,            // Performance
            MEDIUM,             // Speed
            of(TEXT),           // Supported Input Modalities
            of(TEXT)            // Supported Output Modalities
    );

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
        return input.containsAll(of(modality));
    }

    public boolean hasOutput(Modality... modality) {
        return output.containsAll(of(modality));
    }

    // Static Methods
    public static ModerationModel getModel(String name) {
        for (var model : ModerationModel.values()) if (model.getName().equalsIgnoreCase(name)) return model;
        throw new IllegalArgumentException("Invalid model name: " + name);
    }
}