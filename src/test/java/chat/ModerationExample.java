package chat;

import com.fasterxml.jackson.databind.JsonNode;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ModerationPrompt;
import de.MCmoderSD.openai.objects.Rating;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ModerationExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main Method
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
                null,       // Project (optional)
                null        // Endpoint (optional)
        );

        // Configure OpenAI
        Builder.Moderation.setConfig(config);

        // Setup Chat
        System.out.println("User Input:");

        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Prompt
            ModerationPrompt prompt = openAI.moderate(
                    null,       // Model
                    userInput       // User Message
            );

            // Get Rating
            Rating rating = prompt.getRating();

            System.out.println("\nSize: " + rating.getBytes().length + " bytes\n");

            // Get Data
            String data = rating.getData(Rating.Data.POSITIVE);

            // Print Data
            System.out.println("\nPositive Flags:\n" + data);
            System.out.println("Hit: " + (data.split(":").length - 1) + "/13");

            // User Input
            System.out.println("\n\nUser Input: \n");
        }
    }
}