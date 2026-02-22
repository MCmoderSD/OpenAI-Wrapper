package de.MCmoderSD.openai.models;

import de.MCmoderSD.openai.enums.Performance;
import de.MCmoderSD.openai.enums.Speed;
import de.MCmoderSD.openai.enums.Modality;
import de.MCmoderSD.openai.enums.Voice;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static de.MCmoderSD.openai.enums.Performance.*;
import static de.MCmoderSD.openai.enums.Speed.*;
import static de.MCmoderSD.openai.enums.Modality.*;
import static de.MCmoderSD.openai.enums.Voice.*;
import static java.util.List.of;

@SuppressWarnings("unused")
public enum SpeechModel {

    TTS_1(
            AVERAGE,    // Performance
            FAST,       // Speed
            15_00,      // Cent Per Million Tokens
            of(TEXT),   // Supported Input Modalities
            of(AUDIO),   // Supported Output Modalities
            of(ALLOY, ASH, CORAL, ECHO, FABLE, ONYX, NOVA, SAGE, SHIMMER)
    ),

    TTS_1_HD(
            HIGH,       // Performance
            MEDIUM,     // Speed
            30_00,      // Cent Per Million Tokens
            of(TEXT),   // Supported Input Modalities
            of(AUDIO),  // Supported Output Modalities
            of(ALLOY, ASH, CORAL, ECHO, FABLE, ONYX, NOVA, SAGE, SHIMMER)
    ),

    GPT_4O_MINI_TTS(
            HIGHER,     // Performance
            FAST,       // Speed
            12_00,      // Cent Per Million Tokens
            of(TEXT),   // Supported Input Modalities
            of(AUDIO),  // Supported Output Modalities
            of(ALLOY, ASH, BALLAD, CORAL, ECHO, FABLE, NOVA, ONYX, SAGE, SHIMMER, VERSE, MARIN, CEDAR)
    );

    // Attributes
    private final Performance performance;
    private final Speed speed;
    private final BigDecimal price;
    private final HashSet<Modality> input;
    private final HashSet<Modality> output;
    private final HashSet<Voice> voices;
    private final String name;
    private final com.openai.models.audio.speech.SpeechModel model;

    // Constructor
    SpeechModel(
            Performance performance,    // Performance
            Speed speed,                // Speed
            int CPMT,                   // Cent Per Million Tokens
            List<Modality> input,       // Supported Input Modalities
            List<Modality> output,      // Supported Output Modalities
            List<Voice> voices          // Supported Voices
    ) {
        // Set Attributes
        this.performance = performance;
        this.speed = speed;

        // Calculate Cost
        this.price = BigDecimal.valueOf(CPMT).movePointLeft(8);

        // Set Modalities
        this.input = new HashSet<>(input);
        this.output = new HashSet<>(output);

        // Set Voices
        this.voices = new HashSet<>(voices);

        // Parse Name
        this.name = name().toLowerCase().replace('_', '-');

        // Parse Model
        this.model = com.openai.models.audio.speech.SpeechModel.of(name());
    }

    // Getter
    public com.openai.models.audio.speech.SpeechModel getModel() {
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

    public HashSet<Voice> getVoices() {
        return voices;
    }

    public boolean hasInput(Modality... modality) {
        return input.containsAll(of(modality));
    }

    public boolean hasOutput(Modality... modality) {
        return output.containsAll(of(modality));
    }

    public boolean hasVoice(Voice... voice) {
        return voices.containsAll(of(voice));
    }

    public BigDecimal getCost(long tokens) {
        return price.multiply(BigDecimal.valueOf(tokens));
    }

    // Static Methods
    public static SpeechModel getModel(String name) {
        name = name.replaceAll("-\\d{4}-\\d{2}-\\d{2}", "").toLowerCase().trim();
        for (var model : SpeechModel.values()) if (model.getName().equalsIgnoreCase(name)) return model;
        throw new IllegalArgumentException("Invalid model name: " + name);
    }
}