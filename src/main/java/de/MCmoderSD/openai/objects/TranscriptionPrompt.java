package de.MCmoderSD.openai.objects;

import com.openai.models.audio.AudioResponseFormat;
import com.openai.models.audio.transcriptions.Transcription;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;
import de.MCmoderSD.openai.enums.Language;
import de.MCmoderSD.openai.model.AudioModel;

/**
 * Represents a prompt for audio transcription.
 * This class holds the input parameters, the output transcriptions, and provides methods to access them.
 */
@SuppressWarnings("unused")
public class TranscriptionPrompt {

    // Parameters
    private final TranscriptionCreateParams[] input;
    private final Transcription[] output;

    // Input
    private final AudioModel model;
    private final Language language;
    private final double temperature;
    private final String prompt;
    private final AudioResponseFormat format;
    private final byte[] file;
    private final int chunks;

    // Output
    private final String text;

    /**
     * Constructs a new TranscriptionPrompt with the specified input parameters, audio data, and output transcriptions.
     *
     * @param input   The input parameters for creating the transcription
     * @param data    The audio data to be transcribed
     * @param output  The transcriptions generated from the audio data
     */
    public TranscriptionPrompt(TranscriptionCreateParams[] input, byte[] data, Transcription[] output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Input
        model = AudioModel.getModel(input[0].model().asString());
        language = Language.getLanguage(input[0].language().orElse(null));
        temperature = input[0].temperature().orElse(1d);
        prompt = input[0].prompt().orElse(null);
        format = input[0].responseFormat().orElse(null);
        file = data;
        chunks = input.length;

        // Extract Text
        var combinedText = new StringBuilder();
        for (var transcription : output) if (transcription != null && !transcription.text().isBlank()) combinedText.append(transcription.text());
        text = combinedText.toString();
    }

    /**
     * Gets the input parameters for creating the transcription.
     *
     * @return The input parameters
     */
    public TranscriptionCreateParams[] getInput() {
        return input;
    }

    /**
     * Gets the transcriptions generated from the audio data.
     *
     * @return The transcriptions
     */
    public Transcription[] getOutput() {
        return output;
    }

    /**
     * Gets the audio model used for the transcription.
     *
     * @return The audio model
     */
    public AudioModel getModel() {
        return model;
    }

    /**
     * Gets the language of the audio data.
     *
     * @return The language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Gets the temperature setting for the transcription.
     *
     * @return The temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Gets the prompt used for the transcription.
     *
     * @return The prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Gets the response format of the transcription.
     *
     * @return The response format
     */
    public AudioResponseFormat getFormat() {
        return format;
    }

    /**
     * Gets the audio data to be transcribed.
     *
     * @return The audio data
     */
    public byte[] getFile() {
        return file;
    }

    /**
     * Gets the number of chunks in the input parameters.
     *
     * @return The number of chunks
     */
    public int getChunks() {
        return chunks;
    }

    /**
     * Gets the combined text of the transcriptions.
     *
     * @return The combined text
     */
    public String getText() {
        return text;
    }
}