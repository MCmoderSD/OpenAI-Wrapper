package de.MCmoderSD.openai.objects;

import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateParams;
import com.openai.models.moderations.ModerationCreateResponse;
import de.MCmoderSD.openai.model.ModerationModel;

@SuppressWarnings("unused")
public class ModerationPrompt {

    // Parameters
    private final ModerationCreateParams input;
    private final ModerationCreateResponse output;

    // Data
    private final String id;
    private final ModerationModel model;
    private final String text;

    // Content
    private final Moderation moderation;
    private final Rating rating;

    public ModerationPrompt(ModerationCreateParams input, ModerationCreateResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Data
        id = output.id();
        model = ModerationModel.getModel(output.model());
        text = input.input().asString();

        // Extract Content
        moderation = output.results().getFirst();
        rating = new Rating(moderation);
    }

    /**
     * Gets the input parameters of this moderation prompt.
     *
     * @return The input parameters.
     */
    public ModerationCreateParams getInput() {
        return input;
    }

    /**
     * Gets the moderation response of this moderation prompt.
     *
     * @return The moderation response.
     */
    public ModerationCreateResponse getOutput() {
        return output;
    }

    /**
     * Gets the ID of this moderation prompt.
     *
     * @return The ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the moderation model used for this moderation prompt.
     *
     * @return The moderation model.
     */
    public ModerationModel getModel() {
        return model;
    }

    /**
     * Gets the input text of this moderation prompt.
     *
     * @return The input text.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the moderation details of this moderation prompt.
     *
     * @return The moderation details.
     */
    public Moderation getModeration() {
        return moderation;
    }

    /**
     * Gets the rating details of this moderation prompt.
     *
     * @return The rating details.
     */
    public Rating getRating() {
        return rating;
    }
}