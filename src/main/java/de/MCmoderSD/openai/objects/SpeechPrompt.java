package de.MCmoderSD.openai.objects;

import com.openai.core.http.HttpResponse;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.audio.speech.SpeechCreateParams.ResponseFormat;
import de.MCmoderSD.openai.models.SpeechModel;

import java.io.IOException;
import java.util.Base64;

import static com.openai.models.audio.speech.SpeechCreateParams.*;

@SuppressWarnings("unused")
public class SpeechPrompt {

    // Parameters
    private final SpeechCreateParams input;
    private final HttpResponse output;

    // Input
    private final SpeechModel model;
    private final Voice voice;
    private final ResponseFormat responseFormat;
    private final double speed;
    private final String instruction;
    private final String text;

    // Output
    private final String id;
    private final byte[] body;
    private final String base64;

    /**
     * Constructs a new SpeechPrompt with the specified input and output.
     *
     * @param input  The input parameters for generating the speech
     * @param output The AI-generated response associated with the input
     * @throws IOException If there is an error reading the response body
     */
    public SpeechPrompt(SpeechCreateParams input, HttpResponse output) throws IOException {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Input
        model = SpeechModel.getModel(input.model().asString());
        voice = input.voice();
        responseFormat = input.responseFormat().orElse(null);
        speed = input.speed().orElse(1d);
        instruction = input.instructions().orElse(null);
        text = input.input();

        // Extract Output
        id = output.requestId().orElse(null);
        body = output.body().readAllBytes();

        // Encode to Base64
        base64 = Base64.getEncoder().encodeToString(body);
    }

    /**
     * Gets the input parameters for generating the speech.
     *
     * @return The input parameters
     */
    public SpeechCreateParams getInput() {
        return input;
    }

    /**
     * Gets the AI-generated response associated with the input.
     *
     * @return The AI-generated response
     */
    public HttpResponse getOutput() {
        return output;
    }

    /**
     * Gets the speech model used for the response.
     *
     * @return The speech model
     */
    public SpeechModel getModel() {
        return model;
    }

    /**
     * Gets the voice used for the speech.
     *
     * @return The voice
     */
    public Voice getVoice() {
        return voice;
    }

    /**
     * Gets the response format used for the speech.
     *
     * @return The response format
     */
    public ResponseFormat getResponseFormat() {
        return responseFormat;
    }

    /**
     * Gets the speed of the generated speech.
     *
     * @return The speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Gets the instruction used for generating the speech.
     *
     * @return The instruction
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * Gets the text used for generating the speech.
     *
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the ID of the generated speech response.
     *
     * @return The ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the body of the generated speech response.
     *
     * @return The body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Gets the Base64-encoded string of the generated speech response.
     *
     * @return The Base64-encoded string
     */
    public String getBase64() {
        return base64;
    }

    /**
     * Gets the audio data of the generated speech response.
     *
     * @return The audio data
     */
    public byte[] getAudioData() {
        return body;
    }
}