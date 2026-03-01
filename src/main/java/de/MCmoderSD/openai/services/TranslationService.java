package de.MCmoderSD.openai.services;

import com.openai.client.OpenAIClient;
import com.openai.models.audio.translations.TranslationCreateParams;

import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.models.TranslationModel;
import de.MCmoderSD.openai.prompts.TranslationPrompt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.UUID;

import static de.MCmoderSD.openai.models.TranslationModel.WHISPER_1;
import static com.openai.models.audio.translations.TranslationCreateParams.ResponseFormat.VERBOSE_JSON;

public class TranslationService {

    // Constants
    private static final int MAX_SIZE = 26214400; // 25MB
    private static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));

    // Attributes
    private final com.openai.services.blocking.audio.TranslationService service;

    // Parameters
    private final TranslationModel model;
    private final String prompt;
    private final double temperature;

    // Constructor
    private TranslationService(OpenAIClient client, TranslationModel model, String prompt, double temperature) {
        service = client.audio().translations();
        this.model = model;
        this.prompt = prompt;
        this.temperature = temperature;
    }

    // Create Temp File from Data
    private static File createTempFile(byte[] data) {

        // Create Temp File
        File tempFile = new File(TEMP_DIR, UUID.randomUUID() + ".wav");

        // Write Data to Temp File
        try (var bos = new BufferedOutputStream(new FileOutputStream(tempFile))) {
            bos.write(data);            // Write Data
            bos.flush();                // Flush Stream
            tempFile.deleteOnExit();    // Delete Temp File on Exit
            return tempFile;            // Return Temp File
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file: " + e.getMessage(), e);
        }
    }

    // Create Temp File from File
    private static File createTempFile(File file) {
        try (var bis = new BufferedInputStream(new FileInputStream(file))) {
            return createTempFile(bis.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file: " + e.getMessage(), e);
        }
    }

    // Builder
    private TranslationCreateParams buildParams(File file) {

        // Init Builder
        var builder = TranslationCreateParams.builder();

        // Set Parameters
        builder.model(model.getName());
        if (!prompt.isBlank()) builder.prompt(prompt);
        builder.temperature(temperature);
        builder.responseFormat(VERBOSE_JSON);
        builder.file(file.toPath());

        // Build and Return
        return builder.build();
    }

    // Create Speech
    public TranslationPrompt create(byte[] data) {

        // Check Data
        if (data == null) throw new IllegalArgumentException("Data must not be null");
        if (data.length == 0) throw new IllegalArgumentException("Data must not be empty");
        if (data.length > MAX_SIZE) throw new IllegalArgumentException("Data must not exceed 25MB");

        // Create Temp File
        File tempFile = createTempFile(data);

        // Build Request
        var request = buildParams(tempFile);

        // Create Response
        var response = service.create(request);

        // Delete Temp File
        if (!tempFile.delete()) throw new RuntimeException("Failed to delete temp file: " + tempFile.getAbsolutePath());

        // Return Translation Prompt
        return new TranslationPrompt(request, response);
    }

    // Create Speech
    public TranslationPrompt create(File file) {

        // Check File
        if (file == null) throw new IllegalArgumentException("File must not be null");
        if (!file.isFile()) throw new IllegalArgumentException("File must be a file");
        if (!file.exists()) throw new IllegalArgumentException("File must exist");
        if (!file.canRead()) throw new IllegalArgumentException("File must be readable");
        if (file.length() == 0) throw new IllegalArgumentException("File must not be empty");
        if (file.length() > MAX_SIZE) throw new IllegalArgumentException("File must not exceed 25MB");

        // Create Temp File
        File tempFile = createTempFile(file);

        // Build Request
        var request = buildParams(tempFile);

        // Create Response
        var response = service.create(request);

        // Delete Temp File
        if (!tempFile.delete()) throw new RuntimeException("Failed to delete temp file: " + tempFile.getAbsolutePath());

        // Return Translation Prompt
        return new TranslationPrompt(request, response);
    }

    // Static Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {

        // Parameter
        private TranslationModel model;
        private String prompt;
        private double temperature;

        // Constructor
        private Builder() {
            model = WHISPER_1;
            prompt = "";
            temperature = 1d;
        }

        // Build
        public TranslationService build(OpenAI openAI) {

            // Check OpenAI
            if (openAI == null) throw new IllegalArgumentException("OpenAI must not be null");

            // Build and Return
            return new TranslationService(openAI.getClient(), model, prompt, temperature);
        }

        // Set Model
        public Builder setModel(TranslationModel model) {

            // Check Model
            if (model == null) throw new IllegalArgumentException("Model must not be null");

            // Set Model
            this.model = model;
            return this;
        }

        // Set Prompt
        public Builder setPrompt(String prompt) {

            // Check Prompt
            if (prompt == null) throw new IllegalArgumentException("Prompt must not be null");

            // Set Prompt
            this.prompt = prompt;
            return this;
        }

        // Set Temperature
        public Builder setTemperature(double temperature) {

            // Check Temperature
            if (temperature < 0 || temperature > 2) throw new IllegalArgumentException("Temperature must be between 0 and 2");

            // Set Temperature
            this.temperature = temperature;
            return this;
        }
    }
}