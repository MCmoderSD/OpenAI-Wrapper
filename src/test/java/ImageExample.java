import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ImagePrompt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class ImageExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(config);

        System.out.println("Enter your prompt \nYou: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Configure OpenAI
        Builder.Images.setConfig(config);

        // Prompt
        ArrayList<ImagePrompt> response = openAI.generateImage(
                null,   // Model (optional)
                null,   // Size (optional)
                null,   // Response Format (optional)
                null,   // Number of Images (optional)
                null,   // Style (optional)
                null,   // Quality (optional)
                input   // Prompt (required)
        );

        // Print Response
        for (ImagePrompt imagePrompt : response) showImage(imagePrompt);

        // Save image
        for (ImagePrompt imagePrompt : response) saveImage(imagePrompt);
    }

    // Show image
    public static void showImage(ImagePrompt imagePrompt) {

        // Create frame
        JFrame frame = new JFrame(imagePrompt.getInput());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(256, 256);
        frame.setResizable(false);
        frame.setIconImage(imagePrompt.getImage());

        // Create panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagePrompt.getImage(), 0, 0, null);
            }
        };

        // Add panel to frame
        frame.add(panel);

        // Center frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Show frame
        frame.setVisible(true);
    }

    // Save image to file
    public static void saveImage(ImagePrompt imagePrompt) {
        try {
            ImageIO.write(imagePrompt.getImage(), "png", new File(String.join("-", imagePrompt.getInput().split(" ")) + ".png"));
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }
}