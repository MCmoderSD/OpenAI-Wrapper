package de.MCmoderSD.openai.prompts;

import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.core.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class SpeechPrompt {

    // Parameters
    private final SpeechCreateParams input;
    private final HttpResponse output;

    // Data
    private final byte[] data;

    public SpeechPrompt(SpeechCreateParams input, HttpResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Data
        data = readResponse(output);
    }

    // Read Response
    private static byte[] readResponse(HttpResponse response) {
        try {
            return response.body().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read speech response data", e);
        }
    }

    // Getter
    public SpeechCreateParams getInput() {
        return input;
    }

    public HttpResponse getOutput() {
        return output;
    }

    public byte[] getData() {
        return data;
    }

    // Methods
    public File toFile(File file) {

        // Check File
        if (file == null) throw new IllegalArgumentException("File must not be null");
        if (file.isDirectory()) throw new IllegalArgumentException("File must not be a directory");

        // Check File Extension
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        String format = input.responseFormat().orElseThrow().asString();
        if (!format.equalsIgnoreCase(extension)) throw new IllegalArgumentException("File extension must match response format: " + format);

        // Write Data to File
        try (
                var in = new BufferedInputStream(new ByteArrayInputStream(data));
                var out = new BufferedOutputStream(new FileOutputStream(file))
        ) {
            in.transferTo(out);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Failed to write speech data to file", e);
        }
    }
}