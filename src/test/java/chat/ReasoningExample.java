package chat;


import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ReasoningExample {

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
        Builder.Chat.setConfig(config);
        var maxTokens = Builder.Chat.getMaxTokens() * 3; // 3x for reasoning

        // Setup Chat
        System.out.println("Enter your prompt (type 'exit' to quit):");
        System.out.println("You: ");

        // Prompt Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Generate Chat Prompt
            ChatPrompt chatPrompt = openAI.reasoning(
                    null,       // Chat Model
                    null,       // User
                    maxTokens,  // Max Tokens
                    null,       // Temperature
                    null,       // Top P
                    null,       // Frequency Penalty
                    null,       // Presence Penalty
                    null,       // Number of Completions
                    null,       // Reasoning Effort
                    null,       // ID
                    userInput,  // User Message
                    null        // System Message
            );

            // Get Response
            String response = chatPrompt.getText();

            // Print Response
            System.out.println("\nResponse: \n" + response + "\n");
            System.out.print("You: \n");
        }
    }
}