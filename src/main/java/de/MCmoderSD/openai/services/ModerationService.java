package de.MCmoderSD.openai.services;

import com.openai.client.OpenAIClient;
import com.openai.models.moderations.ModerationCreateParams;
import com.openai.models.moderations.ModerationCreateResponse;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.models.ModerationModel;
import de.MCmoderSD.openai.prompts.ModerationPrompt;

import static de.MCmoderSD.openai.models.ModerationModel.OMNI_MODERATION_LATEST;

@SuppressWarnings("unused")
public class ModerationService {

    // Attributes
    private final com.openai.services.blocking.ModerationService service;

    // Parameters
    private final ModerationModel model;

    // Constructor
    private ModerationService(OpenAIClient client, ModerationModel model) {
        service = client.moderations();
        this.model = model;
    }

    // Builder
    private ModerationCreateParams buildParams(String prompt) {

        // Init Builder
        ModerationCreateParams.Builder builder = ModerationCreateParams.builder();

        // Set Parameters
        builder.model(model.getName());
        builder.input(prompt);

        // Build and return
        return builder.build();
    }

    public ModerationPrompt create(String prompt) {

        // Check Parameters
        if (prompt == null || prompt.isBlank()) throw new IllegalArgumentException("Prompt must not be null or blank");

        // Build Params
        ModerationCreateParams params = buildParams(prompt);

        // Create Moderation
        ModerationCreateResponse response = service.create(params);

        // Return Moderation Prompt
        return new ModerationPrompt(params, response);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        // Parameter
        private ModerationModel model;

        // Build
        public ModerationService build(OpenAI openAI) {

            // Check OpenAI
            if (openAI == null) throw new IllegalArgumentException("OpenAI must not be null");

            // Return Service
            return new ModerationService(openAI.getClient(), model == null ? OMNI_MODERATION_LATEST : model);
        }

        // Set Model
        public Builder setModel(ModerationModel model) {

            // Check Model
            if (model == null) throw new IllegalArgumentException("Model must not be null");

            // Set Model
            this.model = model;
            return this;
        }
    }
}