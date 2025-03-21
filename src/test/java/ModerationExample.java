import com.fasterxml.jackson.databind.JsonNode;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.objects.ModerationPrompt;
import de.MCmoderSD.openai.objects.Rating;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ModerationExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        
        // Initialize Scanner
        Scanner scanner = new Scanner(System.in);

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(config);

        String input;
        System.out.println("User Input:");
        while (!(input = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Prompt
            ModerationPrompt prompt = openAI.moderate(input);

            // Print Response
            Rating rating = new Rating(prompt.getModerations().getFirst());
            String data = rating.getData(Rating.Data.POSITIVE);
            System.out.println("\nPositive Flags:\n" + data);
            System.out.println("Hit: " + (data.split(":").length - 1) + "/13");

            // User Input
            System.out.println("\n\nUser Input:");
        }
    }
}