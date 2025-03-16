package de.MCmoderSD.openai.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.*;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.helper.Helper;
import de.MCmoderSD.openai.objects.ChatHistory;
import de.MCmoderSD.openai.objects.ChatPrompt;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
public class OpenAI {

    // Attributes
    private final OpenAIClient client;
    private final HashMap<Integer, ChatHistory> chatHistory;

    // Constructor
    public OpenAI(JsonNode config) {
        this(
                config.get("apiKey").asText(),
                config.has("organizationId") ? config.get("organizationId").asText() : null,
                config.has("projectId") ? config.get("projectId").asText() : null
        );
    }

    public OpenAI(String apiKey, @Nullable String organizationId, @Nullable String projectId) {

        // Initialize OpenAI Client
        var builder = OpenAIOkHttpClient.builder().apiKey(apiKey);
        if (organizationId != null && !organizationId.isBlank()) builder.organization(organizationId);
        if (projectId != null && !projectId.isBlank()) builder.project(projectId);
        this.client = builder.build();

        // Initialize Chat History
        this.chatHistory = new HashMap<>();
    }

    private ChatCompletion createChatCompletion(ChatCompletionCreateParams params) {
        return client.chat().completions().create(params);
    }

    public String prompt(String prompt) {

        // Create Chat Completion
        var params = Builder.buildParams(prompt, null);

        // Execute Chat Completion
        var completion = createChatCompletion(params);

        // Get Chat Completion Message
        var message = Helper.getMessage(completion);

        return Helper.getContent(message);
    }

    public String prompt(Integer id, String prompt) {

        // Check if Chat History exists
        if (!chatHistory.containsKey(id)) chatHistory.put(id, new ChatHistory(null));

        // Create Chat Completion
        var params = Builder.buildParams(prompt, chatHistory.get(id).getMessages());

        // Execute Chat Completion
        var completion = createChatCompletion(params);

        // Get Chat Completion Message
        var message = Helper.getMessage(completion);

        // Add ChatPrompt to History
        chatHistory.get(id).addPrompt(new ChatPrompt(prompt, completion));

        // Return Content
        return Helper.getContent(message);
    }
}