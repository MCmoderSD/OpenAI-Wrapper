package de.MCmoderSD.openai.prompts;

import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateParams;
import com.openai.models.moderations.ModerationCreateResponse;
import de.MCmoderSD.openai.models.ModerationModel;
import de.MCmoderSD.openai.objects.Rating;

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

    // Constructor
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

    // Getter
    public ModerationCreateParams getInput() {
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

    public String getText() {
        return text;
    }

    public Moderation getModeration() {
        return moderation;
    }

    public Rating getRating() {
        return rating;
    }
}