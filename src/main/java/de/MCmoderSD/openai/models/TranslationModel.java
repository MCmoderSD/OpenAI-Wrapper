package de.MCmoderSD.openai.models;

import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;
import de.MCmoderSD.openai.enums.Modality;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static de.MCmoderSD.openai.enums.Performance.*;
import static de.MCmoderSD.openai.enums.Speed.*;
import static de.MCmoderSD.openai.enums.Modality.*;
import static java.math.RoundingMode.HALF_UP;
import static java.util.List.of;

@SuppressWarnings("unused")
public enum TranslationModel {

    WHISPER_1(
            AVERAGE,    // Performance
            MEDIUM,     // Speed
            36,         // Cent Per Hour
            of(AUDIO),  // Supported Input Modalities
            of(TEXT)    // Supported Output Modalities
    );

    // Attributes
    private final Performance performance;
    private final Speed speed;
    private final BigDecimal price;
    private final HashSet<Modality> input;
    private final HashSet<Modality> output;
    private final String name;
    private final com.openai.models.audio.AudioModel model;

    // Constructor
    TranslationModel(
            Performance performance,    // Performance
            Speed speed,                // Speed
            int CPH,                    // Cent Per Hour
            List<Modality> input,       // Supported Input Modalities
            List<Modality> output      // Supported Output Modalities
    ) {
        // Set Attributes
        this.performance = performance;
        this.speed = speed;

        // Calculate Cost per Minute
        this.price = BigDecimal.valueOf(CPH).movePointLeft(2).divide(BigDecimal.valueOf(60), 8, HALF_UP);

        // Set Modalities
        this.input = new HashSet<>(input);
        this.output = new HashSet<>(output);

        // Parse Name
        this.name = name().toLowerCase().replace('_', '-');

        // Parse Model
        this.model = com.openai.models.audio.AudioModel.of(name());
    }

    // Getter
    public com.openai.models.audio.AudioModel getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
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

    public BigDecimal getCost(long tokens) {
        return price.multiply(BigDecimal.valueOf(tokens));
    }

    // Static Methods
    public static TranslationModel getModel(String name) {
        for (var model : TranslationModel.values()) if (model.getName().equalsIgnoreCase(name)) return model;
        throw new IllegalArgumentException("Invalid model name: " + name);
    }
}