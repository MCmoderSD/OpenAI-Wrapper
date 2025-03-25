package chat;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class TranslationExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String instruction = "Please translate the following text to ";
        String userInput;
        String devMessage;

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
        Builder.Chat.setConfig(config);

        // Setup Chat
        System.out.println("Enter the text to translate (type 'exit' to quit):");
        System.out.print("You: ");

        // Translation Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Get Language Input
            System.out.print("Language: ");
            devMessage = instruction + scanner.nextLine();

            // Generate Chat Prompt
            ChatPrompt chatPrompt = openAI.prompt(
                    null,           // Chat Model
                    null,           // User
                    null,           // Max Tokens
                    0d,             // Temperature (Override Config)
                    null,           // Top P
                    null,           // Frequency Penalty
                    null,           // Presence Penalty
                    null,           // Number of Completions
                    devMessage,     // Developer Message (Override Config)
                    null,           // ID
                    userInput       // User Message
            );

            // Get Response
            String response = chatPrompt.getText();

            // Print Response
            System.out.println("\nResponse: \n" + response + "\n");
            System.out.print("You: \n");
        }
    }
}