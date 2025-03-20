import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class TranslationExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        Scanner scanner = new Scanner(System.in);
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,             // API Key (required)
                null,               // Organization (optional)
                null                // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);

        // Translation Loop
        System.out.println("Enter the text to translate (type 'exit' to quit):\nYou: ");
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {
            System.out.print("Language: ");
            String devMessage = "Please translate the following text to " + scanner.nextLine();
            String response = openAI.prompt(
                    null,               // Chat Model
                    null,               // User
                    null,               // Max Tokens
                    0d,                 // Temperature
                    null,               // Top P
                    null,               // Frequency Penalty
                    null,               // Presence Penalty
                    null,               // Number of Completions
                    devMessage,         // Developer Message
                    null,               // ID
                    userInput           // User Message
            );
            System.out.println("Response: " + response);
            System.out.print("You: ");
        }
    }
}