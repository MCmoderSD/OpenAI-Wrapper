package de.MCmoderSD.openai.objects;

import com.openai.models.images.Image;
import de.MCmoderSD.imageloader.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;

/**
 * Represents an image prompt containing user input, AI-generated image output, and metadata.
 */
@SuppressWarnings("unused")
public class ImagePrompt {

    // Parameters
    private final String input;
    private final Image output;
    private final Timestamp timestamp;

    // Attributes
    private final String url;

    // Content
    private final BufferedImage image;

    /**
     * Constructs an ImagePrompt instance with the provided user input and AI-generated image response.
     *
     * @param input   The user's input prompt.
     * @param output  The AI-generated image response associated with the input.
     * @param created The timestamp of when the image was created, in seconds since epoch.
     * @throws IOException        If an I/O error occurs while loading the image.
     * @throws URISyntaxException If the URL of the image is malformed.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public ImagePrompt(String input, Image output, long created) throws IOException, URISyntaxException {

        // Initialize Parameters
        this.input = input;
        this.output = output;
        timestamp = new Timestamp(created * 1000L);

        // Extract Data
        url = output.url().get();

        // Initialize Content
        image = ImageLoader.loadImage(url, false);
    }

    /**
     * Returns the user input.
     *
     * @return The input string.
     */
    public String getInput() {
        return input;
    }

    /**
     * Returns the AI-generated image output.
     *
     * @return The Image output.
     */
    public Image getOutput() {
        return output;
    }

    /**
     * Returns the timestamp of when the image was created.
     *
     * @return The timestamp of the image creation.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the URL of the AI-generated image.
     *
     * @return The image URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the BufferedImage object of the AI-generated image.
     *
     * @return The BufferedImage object.
     */
    public BufferedImage getImage() {
        return image;
    }
}