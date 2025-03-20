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
    private final String revisedPrompt;
    private final String url;
    private final String base64;

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
    public ImagePrompt(String input, Image output, long created) throws IOException, URISyntaxException {

        // Initialize Parameters
        this.input = input;
        this.output = output;
        timestamp = new Timestamp(created * 1000L);

        // Extract Data
        revisedPrompt = output.revisedPrompt().orElse(null);
        url = output.url().orElse(null);
        base64 = output.b64Json().orElse(null);

        // Validate URL or Base64
        String imageData = url == null ? base64 : url;
        if (imageData == null || imageData.isEmpty()) throw new IOException("Image URL or Base64 data is missing.");

        // Initialize Content
        image = ImageLoader.loadImage(imageData, false);
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
     * Returns the revised prompt used to generate the image.
     *
     * @return The revised prompt string.
     */
    public String getRevisedPrompt() {
        return revisedPrompt;
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
     * Returns the Base64-encoded string of the AI-generated image.
     *
     * @return The Base64-encoded image string.
     */
    public String getBase64() {
        return base64;
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