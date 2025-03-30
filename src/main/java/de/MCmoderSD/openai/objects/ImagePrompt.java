package de.MCmoderSD.openai.objects;

import com.openai.models.images.Image;
import com.openai.models.images.ImageGenerateParams;
import com.openai.models.images.ImageGenerateParams.Quality;
import com.openai.models.images.ImageGenerateParams.ResponseFormat;
import com.openai.models.images.ImageGenerateParams.Size;
import com.openai.models.images.ImageGenerateParams.Style;
import com.openai.models.images.ImageModel;
import de.MCmoderSD.imageloader.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;

/**
 * Represents an image prompt containing input parameters, AI-generated response, and metadata.
 * This class encapsulates all information related to a single image generation interaction with the OpenAI API,
 * including response content and metadata.
 */
@SuppressWarnings("unused")
public class ImagePrompt {

    // Parameters
    private final ImageGenerateParams input;
    private final Image output;
    private final Timestamp timestamp;

    // Input
    private final ImageModel model;
    private final String user;
    private final ResponseFormat responseFormat;
    private final Size size;
    private final Quality quality;
    private final Style style;
    private final long n;
    private final String prompt;

    // Attributes
    private final String revisedPrompt;
    private final String url;
    private final String base64;

    // Content
    private final BufferedImage image;

    /**
     * Constructs a new ImagePrompt with the specified input and output.
     *
     * @param input   The input parameters for generating the image
     * @param output  The AI-generated response associated with the input
     * @param created The timestamp when the image was created
     * @throws IOException        If there is an error loading the image
     * @throws URISyntaxException If the URL syntax is incorrect
     */
    public ImagePrompt(ImageGenerateParams input, Image output, long created) throws IOException, URISyntaxException {

        // Initialize Parameters
        this.input = input;
        this.output = output;
        timestamp = new Timestamp(created * 1000L);

        // Extract Input
        model = input.model().orElse(null);
        user = input.user().orElse(null);
        responseFormat = input.responseFormat().orElse(null);
        size = input.size().orElse(null);
        quality = input.quality().orElse(null);
        style = input.style().orElse(null);
        n = input.n().orElse(1L);
        prompt = input.prompt();

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
     * Gets the input parameters for generating the image.
     *
     * @return The input parameters
     */
    public ImageGenerateParams getInput() {
        return input;
    }

    /**
     * Gets the AI-generated response associated with the input.
     *
     * @return The AI-generated response
     */
    public Image getOutput() {
        return output;
    }

    /**
     * Gets the timestamp when the image was created.
     *
     * @return The timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the image model used for the response.
     *
     * @return The image model
     */
    public ImageModel getModel() {
        return model;
    }

    /**
     * Gets the user associated with the image generation request.
     *
     * @return The user
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the response format used for the image.
     *
     * @return The response format
     */
    public ResponseFormat getResponseFormat() {
        return responseFormat;
    }

    /**
     * Gets the size of the generated image.
     *
     * @return The size
     */
    public Size getSize() {
        return size;
    }

    /**
     * Gets the quality of the generated image.
     *
     * @return The quality
     */
    public Quality getQuality() {
        return quality;
    }

    /**
     * Gets the style of the generated image.
     *
     * @return The style
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Gets the number of images generated.
     *
     * @return The number of images
     */
    public long getN() {
        return n;
    }

    /**
     * Gets the prompt used for generating the image.
     *
     * @return The prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Gets the revised prompt from the AI-generated response.
     *
     * @return The revised prompt
     */
    public String getRevisedPrompt() {
        return revisedPrompt;
    }

    /**
     * Gets the URL of the generated image.
     *
     * @return The URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the Base64-encoded string of the generated image.
     *
     * @return The Base64-encoded string
     */
    public String getBase64() {
        return base64;
    }

    /**
     * Gets the BufferedImage object of the generated image.
     *
     * @return The BufferedImage object
     */
    public BufferedImage getImage() {
        return image;
    }
}