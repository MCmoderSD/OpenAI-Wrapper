package image;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ImagePrompt;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class ImageExample {

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
        Builder.Images.setConfig(config);

        // Setup Chat
        System.out.println("Enter your prompt (type 'exit' to quit):");
        System.out.println("You: ");

        // Image Generation Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Generate Images
            ArrayList<ImagePrompt> response = openAI.generateImage(
                    null,       // Model
                    null,       // Size
                    null,       // Response Format
                    null,       // Number of Images
                    null,       // Style
                    null,       // Quality
                    userInput   // Prompt
            );

            // Show images
            for (ImagePrompt imagePrompt : response) showImage(imagePrompt);

            // Save images
            for (ImagePrompt imagePrompt : response) ImageIO.write(imagePrompt.getImage(), "png", new File("src/test/resources/assets/" + String.join("-", imagePrompt.getInput().split(" ")) + ".png"));

            // Wait for user to press 'Enter'
            System.out.println("\n\nPress 'Enter' to continue...");
            scanner.nextLine();
            System.out.print("You: \n");

            // Close all open frames
            for (Window window : Window.getWindows()) if (window instanceof JFrame) window.dispose();
        }
    }

    // Show image
    private static void showImage(ImagePrompt imagePrompt) {

        // Get Image
        BufferedImage image = imagePrompt.getImage();
        Dimension size = new Dimension(image.getWidth(), image.getHeight());

        // Create frame
        JFrame frame = new JFrame(imagePrompt.getInput());
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