import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ChatExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        Scanner scanner = new Scanner(System.in);
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();
        int userId = 1; // Example user ID

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,             // API Key (required)
                null,               // Organization (optional)
                null                // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);

        // ChatPrompt Loop
        System.out.println("Enter your prompt (type 'exit' to quit):\nYou: ");
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {
            String response = openAI.prompt(userId, userInput);
            System.out.println("Response: " + response);
            System.out.print("You: ");
        }
    }
}
