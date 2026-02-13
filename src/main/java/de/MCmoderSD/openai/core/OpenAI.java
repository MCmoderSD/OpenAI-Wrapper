package de.MCmoderSD.openai.core;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import de.MCmoderSD.openai.services.EmbeddingService;
import de.MCmoderSD.openai.services.ModerationService;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class OpenAI {

    // Attributes
    protected final OpenAIClient client;

    // Constructor
    public OpenAI(String apiKey, @Nullable String organizationId, @Nullable String projectId, @Nullable String baseUrl) {
        var builder = OpenAIOkHttpClient.builder().apiKey(apiKey);
        if (organizationId != null && !organizationId.isBlank()) builder.organization(organizationId);
        if (projectId != null && !projectId.isBlank()) builder.project(projectId);
        if (baseUrl != null && !baseUrl.isBlank()) builder.baseUrl(baseUrl);
        client = builder.build();
    }

    public OpenAI(String apiKey, String organizationId, String projectId) {
        this(apiKey, organizationId, projectId, null);
    }

    public OpenAI(String apiKey, String baseUrl) {
        this(apiKey, null, null, baseUrl);
    }

    public OpenAI(String apiKey) {
        this(apiKey, null, null);
    }

    // Service
    public EmbeddingService embeddings() {
        return EmbeddingService.builder().build(this);
    }

    public ModerationService moderations() {
        return ModerationService.builder().build(this);
    }

    // Getter
    public OpenAIClient getClient() {
        return client;
    }
}