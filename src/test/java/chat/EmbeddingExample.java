package chat;

import com.fasterxml.jackson.databind.JsonNode;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.Embedding;
import de.MCmoderSD.openai.objects.EmbeddingPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class EmbeddingExample {

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
        Builder.Embeddings.setConfig(config);

        // Setup Chat
        System.out.println("User Input A: ");

        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Prompt
            EmbeddingPrompt promptA = openAI.embedding(
                    null,
                    null,
                    null,
                    userInput
            );

            // Get Embedding
            Embedding vectorA = promptA.getEmbedding();

            // Print Prompt A Data
            System.out.println("\nPrompt A Data:");
            System.out.println("Prompt Tokens : " + promptA.getPromptTokens());
            System.out.println("Total Tokens: " + promptA.getTotalTokens());
            System.out.println("Prompt Cost: " + promptA.getPromptCost());
            System.out.println("Total Cost: " + promptA.getTotalCost());
            System.out.println("Size: " + vectorA.getBytes().length + " bytes\n");

            System.out.println("User Input B: ");
            userInput = scanner.nextLine();

            // Prompt again
            EmbeddingPrompt promptB = openAI.embedding(userInput);

            // Get Embedding
            Embedding vectorB = promptB.getEmbedding();

            // Print Prompt B Data
            System.out.println("\nPrompt B Data:");
            System.out.println("Prompt Tokens : " + promptB.getPromptTokens());
            System.out.println("Total Tokens: " + promptB.getTotalTokens());
            System.out.println("Prompt Cost: " + promptB.getPromptCost());
            System.out.println("Total Cost: " + promptB.getTotalCost());
            System.out.println("Size: " + vectorB.getBytes().length + " bytes\n");

            // Calculate Cosine Similarity
            double similarity = vectorA.cosineSimilarity(vectorB);
            System.out.println("Cosine Similarity: " + similarity);

            // Calculate Euclidean Distance
            double distance = vectorA.euclideanDistance(vectorB);
            System.out.println("Euclidean Distance: " + distance);

            // Calculate Manhattan Distance
            double manhattan = vectorA.manhattanDistance(vectorB);
            System.out.println("Manhattan Distance: " + manhattan);

            // Calculate Angle
            double angle = vectorA.angleBetween(vectorB);
            System.out.println("Angle: " + angle);

            // User Input
            System.out.println("\n\nUser Input A: ");
        }
    }
}