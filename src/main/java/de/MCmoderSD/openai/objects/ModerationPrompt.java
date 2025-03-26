package de.MCmoderSD.openai.objects;

import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateResponse;
import com.openai.models.moderations.ModerationModel;

import de.MCmoderSD.openai.helper.Helper;

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

    // Constructor
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

    // Getters
    public String getInput() {
        return input;
    }

    public ModerationCreateResponse getOutput() {
        return output;
    }

    public String getId() {
        return id;
    }

    public ModerationModel getModel() {
        return model;
    }

    public Moderation getModeration() {
        return moderation;
    }

    public Rating getRating() {
        return rating;
    }
}