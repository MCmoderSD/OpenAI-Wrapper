package de.MCmoderSD.openai.services;

import com.openai.client.OpenAIClient;
import com.openai.models.embeddings.EmbeddingCreateParams;

import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.models.EmbeddingModel;
import de.MCmoderSD.openai.prompts.EmbeddingPrompt;

import static de.MCmoderSD.openai.models.EmbeddingModel.*;
import static com.openai.models.embeddings.EmbeddingCreateParams.EncodingFormat.*;

@SuppressWarnings("unused")
public class EmbeddingService {

    // Attributes
    private final com.openai.services.blocking.EmbeddingService service;

    // Parameters
    private final EmbeddingModel model;
    private final String user;

    // Constructor
    private EmbeddingService(OpenAIClient client, EmbeddingModel model, String user) {
        service = client.embeddings();
        this.model = model;
        this.user = user;
    }

    // Builder
    private EmbeddingCreateParams buildParams(String prompt) {

        // Init Builder
        var builder = EmbeddingCreateParams.builder();

        // Set Parameters
        builder.model(model.getName());
        if (!user.isBlank()) builder.user(user);
        builder.input(prompt);
        builder.encodingFormat(FLOAT);

        // Build and return
        return builder.build();
    }

    // Create Embedding
    public EmbeddingPrompt create(String prompt) {

        // Check Parameters
        if (prompt == null || prompt.isBlank()) throw new IllegalArgumentException("Prompt must not be null or blank");

        // Create Embedding request
        var request = buildParams(prompt);

        // Create Embedding
        var response = service.create(request);

        // Return Embedding Prompt
        return new EmbeddingPrompt(request, response);
    }

    // Static Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {

        // Parameter
        private EmbeddingModel model;
        private String user = "";

        // Build
        public EmbeddingService build(OpenAI openAI) {

            // Check OpenAI
            if (openAI == null) throw new IllegalArgumentException("OpenAI must not be null");

            // Return Service
            return new EmbeddingService(openAI.getClient(), model == null ? TEXT_EMBEDDING_3_LARGE : model, user);
        }

        // Set Model
        public Builder setModel(EmbeddingModel model) {

            // Check Model
            if (model == null) throw new IllegalArgumentException("Model must not be null");

            // Set Model
            this.model = model;
            return this;
        }

        // Set User
        public Builder setUser(String user) {

            // Check User
            if (user == null) throw new IllegalArgumentException("User must not be null");
            if (user.isBlank()) throw new IllegalArgumentException("User must not be blank");

            // Set User
            this.user = user;
            return this;
        }
    }
}