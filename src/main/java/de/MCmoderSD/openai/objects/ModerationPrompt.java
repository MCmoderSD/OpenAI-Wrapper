package de.MCmoderSD.openai.objects;

import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateResponse;
import com.openai.models.moderations.ModerationModel;

import de.MCmoderSD.openai.helper.Helper;

import java.util.ArrayList;

/**
 * Represents a moderation prompt with input text and output response.
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
    private final ArrayList<Moderation> moderations;
    private final ArrayList<Rating> ratings;

    /**
     * Constructs a new ModerationPrompt with the specified input and output.
     *
     * @param input  The input text for moderation
     * @param output The response from the moderation API
     */
    public ModerationPrompt(String input, ModerationCreateResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Data
        id = output.id();
        model = Helper.getModerationModel(output.model());

        // Extract Content
        moderations = new ArrayList<>(output.results());
        ratings = new ArrayList<>();
        moderations.forEach(moderation -> ratings.add(new Rating(moderation)));
    }

    /**
     * Gets the input text for moderation.
     *
     * @return The input text
     */
    public String getInput() {
        return input;
    }

    /**
     * Gets the response from the moderation API.
     *
     * @return The moderation response
     */
    public ModerationCreateResponse getOutput() {
        return output;
    }

    /**
     * Gets the ID of the moderation response.
     *
     * @return The response ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the moderation model used for the response.
     *
     * @return The moderation model
     */
    public ModerationModel getModel() {
        return model;
    }

    /**
     * Gets the list of moderations from the response.
     *
     * @return The list of moderations
     */
    public ArrayList<Moderation> getModerations() {
        return moderations;
    }

    /**
     * Gets the list of ratings derived from the moderations.
     *
     * @return The list of ratings
     */
    public ArrayList<Rating> getRatings() {
        return ratings;
    }
}