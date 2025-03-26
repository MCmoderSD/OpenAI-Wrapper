package de.MCmoderSD.openai.objects;

import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateResponse;
import com.openai.models.moderations.ModerationModel;

import de.MCmoderSD.openai.helper.Helper;

/**
 * Represents a moderation prompt containing input text and the corresponding moderation response.
 */
@SuppressWarnings("unused")
public class ModerationPrompt {

    // Parameters
    private final String input;
    private final ModerationCreateResponse output;

    // Data
    private final String id;
    private final ModerationModel model;

    // Content
    private final Moderation moderation;
    private final Rating rating;

    /**
     * Constructs a new ModerationPrompt with the specified input text and moderation response.
     *
     * @param input  The input text to be moderated.
     * @param output The response from the moderation API.
     */
    public ModerationPrompt(String input, ModerationCreateResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Data
        id = output.id();
        model = Helper.getModerationModel(output.model());

        // Extract Content
        moderation = output.results().getFirst();
        rating = new Rating(moderation);
    }

    /**
     * Gets the input text of this moderation prompt.
     *
     * @return The input text.
     */
    public String getInput() {
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