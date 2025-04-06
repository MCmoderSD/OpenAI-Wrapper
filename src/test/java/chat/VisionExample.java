package chat;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.imageloader.ImageLoader;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class VisionExample {

    // Main method
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
        Builder.Chat.setConfig(config);

        // Setup Prompt
        String devMessage = "Precisely describe the image in detail, so an AI image generation model like DALL-E can recreate it. Then adapt your description to the user's request.";
        String userInput = "Make the cat look like out of LEGO.";

        // Load Image
        BufferedImage image = ImageLoader.loadImage("src/test/resources/assets/katze-mit-hut.png", true);
        ArrayList<BufferedImage> images = new ArrayList<>(Collections.singleton(image));

        // Generate Chat Prompt
        ChatPrompt chatPrompt = openAI.prompt(
                null,       // Chat Model
                null,       // User
                null,       // Max Tokens
                null,       // Temperature
                null,       // Top P
                null,       // Frequency Penalty
                null,       // Presence Penalty
                null,       // Number of Completions
                devMessage, // Developer Message
                null,       // ID
                userInput,  // Prompt
                images      // Image
        );

        // Get Response
        String response = chatPrompt.getText();
        System.out.println("Response: " + response);

        // Print Prompt Data
        System.out.println("Input Tokens: " + chatPrompt.getInputTokens());
        System.out.println("Prompt Cost: " + chatPrompt.getCachedInputTokens());
        System.out.println("Output Tokens: " + chatPrompt.getOutputTokens());
        System.out.println("Total Tokens: " + chatPrompt.getTotalTokens());
    }
}
