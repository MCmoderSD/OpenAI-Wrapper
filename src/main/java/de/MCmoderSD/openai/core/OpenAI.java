package de.MCmoderSD.openai.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.*;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.helper.Helper;
import de.MCmoderSD.openai.objects.ChatHistory;
import de.MCmoderSD.openai.objects.ChatPrompt;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
@SuppressWarnings("unused")
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

    // Constructor
    public OpenAI(String apiKey) {
        this(apiKey, null, null);
    }

    // Constructor
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

    // Prompt
    public String prompt(String prompt) {
        return prompt(null, null, null, null, null, null, null, null, null, prompt, null);
    }

    // Prompt with ID
    public String prompt(Integer id, String prompt) {
        return prompt(null, null, null, null, null, null, null, null, null, prompt, id);
    }

    public String prompt(@Nullable ChatModel chatModel, @Nullable String user, @Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable String devMessage, String prompt, Integer id) {

        // Check if Chat History exists
        if (id != null) chatHistory.putIfAbsent(id, new ChatHistory(null));
        var messages = id != null ? chatHistory.get(id).getMessages() : null;

        // Create Chat Completion
        var params = Builder.buildParams(
                chatModel,          // Chat Model
                user,               // User
                maxTokens,          // Max Tokens
                Temperature,        // Max Tokens
                topP,               // Top P
                frequencyPenalty,   // Frequency Penalty
                presencePenalty,    // Presence Penalty
                n,                  // Number of Completions
                devMessage,         // Developer Message
                prompt,             // Prompt
                messages         // Messages
        );

        // Execute Chat Completion
        var completion = createChatCompletion(params);

        // Get Chat Completion Message
        var message = Helper.getMessage(completion);

        // Add ChatPrompt to History
        if (id != null) chatHistory.get(id).addPrompt(new ChatPrompt(prompt, completion));

        // Return Content
        return Helper.getContent(message);
    }

    // Setters
    public void clearChatHistory(Integer id) {
        chatHistory.remove(id);
    }

    public void clearAllChatHistory() {
        chatHistory.clear();
    }

    // Getters
    public boolean chatHistoryExists(Integer id) {
        return chatHistory.containsKey(id);
    }

    public ChatHistory getChatHistory(Integer id) {
        return chatHistoryExists(id) ? chatHistory.get(id) : null;
    }

    public HashMap<Integer, ChatHistory> getChatHistories() {
        return chatHistory;
    }
}