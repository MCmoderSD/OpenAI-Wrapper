package audio;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class SpeechExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String userInput;

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

        // Setup Input
        System.out.println("Enter the text to convert to speech:");
        userInput = scanner.nextLine();

        // Generate Speech
        byte[] audioData = openAI.speech(
                null,       // Model
                null,       // Voice
                null,       // Response Format
                null,       // Speed
                userInput   // Text
        );

        // Print Audio Data Size
        System.out.println("\nAudio data size: " + audioData.length + " bytes");

        // Transcribe Speech
        String transcription = openAI.transcribe(
                null,       // Model
                null,       // Language
                null,       // Prompt
                null,       // Temperature
                audioData   // Audio Data
        );

        // Print Transcription
        System.out.println("\nTranscription: \n" + transcription);
    }
}