package de.MCmoderSD.openai.services;

import com.openai.client.OpenAIClient;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.audio.speech.SpeechCreateParams.ResponseFormat;

import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.enums.Voice;
import de.MCmoderSD.openai.models.SpeechModel;
import de.MCmoderSD.openai.prompts.SpeechPrompt;

import static de.MCmoderSD.openai.models.SpeechModel.*;
import static de.MCmoderSD.openai.enums.Voice.*;
import static com.openai.models.audio.speech.SpeechCreateParams.ResponseFormat.*;

public class SpeechService {

    // Attributes
    private final com.openai.services.blocking.audio.SpeechService service;

    // Parameters
    private final SpeechModel model;
    private final String instructions;
    private final double speed;
    private final Voice voice;
    private final ResponseFormat format;

    // Constructor
    private SpeechService(OpenAIClient client, SpeechModel model, String instructions, double speed, Voice voice, ResponseFormat format) {
        service = client.audio().speech();
        this.model = model;
        this.instructions = instructions;
        this.speed = speed;
        this.voice = voice;
        this.format = format;
    }

    // Builder
    private SpeechCreateParams buildParams(String prompt) {

        // Init Builder
        var builder = SpeechCreateParams.builder();

        // Set Parameters
        builder.model(model.getName());
        if (!instructions.isBlank()) builder.instructions(instructions);
        builder.speed(speed);
        builder.voice(voice.getName());
        builder.responseFormat(format);
        builder.input(prompt);

        // Build and Return
        return builder.build();
    }

    // Create Speech
    public SpeechPrompt create(String prompt) {

        // Check Parameters
        if (prompt == null || prompt.isBlank()) throw new IllegalArgumentException("Prompt must not be null or blank");

        // Create
        var request = buildParams(prompt);

        // Create Response
        var response = service.create(request);

        // Check Response Status
        var status = response.statusCode();
        if (status != 200) throw new RuntimeException("Failed to create speech, HTTP status code: " + status);

        // Return Speech Prompt
        return new SpeechPrompt(request, response);
    }

    // Static Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {

        // Parameter
        private SpeechModel model;
        private String instructions;
        private double speed;
        private Voice voice;
        private ResponseFormat format;

        // Constructor
        private Builder() {
            model = GPT_4O_MINI_TTS;
            instructions = "";
            speed = 1d;
            voice = CEDAR;
            format = WAV;
        }

        // Build
        public SpeechService build(OpenAI openAI) {

            // Check OpenAI
            if (openAI == null) throw new IllegalArgumentException("OpenAI must not be null");
            if (!instructions.isBlank() && model != GPT_4O_MINI_TTS) throw new IllegalArgumentException("Instructions are only supported for GPT-4o-mini-tts");
            if (!model.hasVoice(voice)) throw new IllegalArgumentException("Selected voice is not supported by the selected model");

            // Build and Return
            return new SpeechService(openAI.getClient(), model, instructions, speed, voice, format);
        }

        // Set Model
        public Builder setModel(SpeechModel model) {

            // Check Model
            if (model == null) throw new IllegalArgumentException("Model must not be null");

            // Set Model
            this.model = model;
            return this;
        }

        // Set Instructions
        public Builder setInstructions(String instructions) {

            // Check Instruction
            if (instructions == null || instructions.isBlank()) throw new IllegalArgumentException("Instructions must not be null or blank");

            // Set Instruction
            this.instructions = instructions;
            return this;
        }

        // Set Speed
        public Builder setSpeed(double speed) {

            // Check Speed
            if (speed < 0.25 || speed > 4) throw new IllegalArgumentException("Speed must be between 0.25 and 4");

            // Set Speed
            this.speed = speed;
            return this;
        }

        // Set Voice
        public Builder setVoice(Voice voice) {

            // Check Voice
            if (voice == null) throw new IllegalArgumentException("Voice must not be null");

            // Set Voice
            this.voice = voice;
            return this;
        }

        // Set Format
        public Builder setFormat(ResponseFormat format) {

            // Check Format
            if (format == null) throw new IllegalArgumentException("Format must not be null");

            // Set Format
            this.format = format;
            return this;
        }
    }
}