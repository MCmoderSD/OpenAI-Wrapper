package de.MCmoderSD.openai.models;

import de.MCmoderSD.openai.enums.Modality;
import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static de.MCmoderSD.openai.enums.Performance.*;
import static de.MCmoderSD.openai.enums.Speed.*;
import static de.MCmoderSD.openai.enums.Modality.*;

@SuppressWarnings("unused")
public enum EmbeddingModel {

    // Enum Values
    TEXT_EMBEDDING_ADA_002(LOW, SLOW, 10, List.of(TEXT), List.of(TEXT)),
    TEXT_EMBEDDING_3_SMALL(AVERAGE, MEDIUM, 2, List.of(TEXT), List.of(TEXT)),
    TEXT_EMBEDDING_3_LARGE(HIGH, SLOW, 13, List.of(TEXT), List.of(TEXT));

    // Attributes
    private final Performance performance;
    private final Speed speed;
    private final BigDecimal price;
    private final HashSet<Modality> input;
    private final HashSet<Modality> output;
    private final String name;
    private final com.openai.models.embeddings.EmbeddingModel model;

    // Constructor
    EmbeddingModel(
            Performance performance,    // Performance
            Speed speed,                // Speed
            int CPMT,                   // Cent Per Million Tokens
            List<Modality> input,       // Supported Input Modalities
            List<Modality> output       // Supported Output Modalities
    ) {
        // Set Attributes
        this.performance = performance;
        this.speed = speed;

        // Calculate Cost
        this.price = BigDecimal.valueOf(CPMT).movePointLeft(8);

        // Set Modalities
        this.input = new HashSet<>(input);
        this.output = new HashSet<>(output);

        // Parse Name
        this.name = name().toLowerCase().replace('_', '-');

        // Parse Model
        this.model = com.openai.models.embeddings.EmbeddingModel.of(name());
    }

    // Getter
    public com.openai.models.embeddings.EmbeddingModel getModel() {
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
        return input.containsAll(List.of(modality));
    }

    public boolean hasOutput(Modality... modality) {
        return output.containsAll(List.of(modality));
    }

    public BigDecimal getCost(long tokens) {
        return price.multiply(BigDecimal.valueOf(tokens));
    }

    // Static Methods
    public static EmbeddingModel getModel(String name) {
        for (var model : EmbeddingModel.values()) if (model.getName().equalsIgnoreCase(name)) return model;
        throw new IllegalArgumentException("Invalid embedding model name: " + name);
    }
}