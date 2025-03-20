import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;

public class SpeechExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Speech.setConfig(config);
        Builder.Transcription.setConfig(config);

        // Speech Synthesis and Transcription Example
        String text = "Hello, this is a test of the speech synthesis feature.";

        // Generate Speech
        byte[] audioData = openAI.speech(
                null,   // Model (optional)
                null,   // Voice (optional)
                null,   // Response Format (optional)
                null,   // Speed (optional)
                text    // Text (required)
        );

        // Transcribe Speech
        String transcription = openAI.transcribe(
                null,       // Model (optional)
                null,       // Language (optional)
                null,       // Prompt (optional)
                null,       // Temperature (optional)
                audioData   // Audio Data (required)
        );

        // Print Transcription
        System.out.println("Transcription: " + transcription);
    }
}