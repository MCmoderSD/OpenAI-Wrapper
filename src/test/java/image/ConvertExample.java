package image;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.imageloader.ImageLoader;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;
import de.MCmoderSD.openai.objects.ImagePrompt;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class ConvertExample {

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

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
        Builder.Chat.setConfig(config);
        Builder.Images.setConfig(config);

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
                16384L,     // Max Tokens
                1d,         // Temperature
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

        // Create Image Prompt
        // Generate Images
        ArrayList<ImagePrompt> imagePrompts = openAI.generateImage(
                null,       // Model
                null,       // Size
                null,       // Response Format
                null,       // Number of Images
                null,       // Style
                null,       // Quality
                response    // Prompt
        );

        // Show images
        for (ImagePrompt imagePrompt : imagePrompts) showImage(imagePrompt);

        // Save images
        for (ImagePrompt imagePrompt : imagePrompts) ImageIO.write(imagePrompt.getImage(), "png", new File("src/test/resources/assets/" + String.join("-", userInput.split(" ")) + ".png"));
    }

    // Show image
    private static void showImage(ImagePrompt imagePrompt) {

        // Get Image
        BufferedImage image = imagePrompt.getImage();
        Dimension size = new Dimension(image.getWidth(), image.getHeight());

        // Create frame
        JFrame frame = new JFrame(imagePrompt.getPrompt());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(size);
        frame.setResizable(false);
        frame.setIconImage(image);

        // Create panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        // Add panel to frame
        panel.setPreferredSize(size);
        frame.add(panel);

        // Center frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Show frame
        frame.setVisible(true);
    }
}