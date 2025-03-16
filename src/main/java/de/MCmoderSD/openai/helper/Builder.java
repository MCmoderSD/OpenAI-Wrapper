package de.MCmoderSD.openai.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Builder {

    // Setup
    private static ChatModel chatModel = ChatModel.GPT_4O_MINI;
    private static String user = "Developer";
    private static String devMessage;

    // Configuration
    private static Double temperature = 1d;
    private static Double topP = 1d;
    private static Double frequencyPenalty = 0d;
    private static Double presencePenalty = 0d;
    private static Long n = 1L;
    private static Integer maxTokens = 120;

    public static ChatCompletionCreateParams buildParams(String prompt, @Nullable ArrayList<ChatCompletionMessageParam> messages) {

        // Create Chat Completion Parameters
        var params = ChatCompletionCreateParams.builder()
                .model(chatModel)                   // Model
                .user(user)                         // User
                .maxCompletionTokens(maxTokens)     // Max Tokens
                .temperature(temperature)           // Temperature
                .topP(topP)                         // Top P
                .frequencyPenalty(frequencyPenalty) // Frequency Penalty
                .presencePenalty(presencePenalty)   // Presence Penalty
                .n(n);                              // Number of Completions

        // Add Message History
        if (messages != null && !messages.isEmpty()) {
            for (var i = 0; i < messages.size(); i++) {
                var message = messages.get(i);
                if (i % 2 == 0) params.addMessage(message.asUser());
                else params.addMessage(message.asAssistant());
            }
        } else params.addDeveloperMessage(devMessage);  // Add Developer Message
        return params.addUserMessage(prompt).build();   // Add User Message
    }

    // Setter
    public static void setConfig(JsonNode config) {

        // Load Setup
        chatModel = config.has("model") ? Helper.getChatModel(config.get("model").asText()) : ChatModel.CHATGPT_4O_LATEST;
        user = config.has("user") ? config.get("user").asText() : "";

        // Load Chat Configuration
        config = config.get("chat");
        devMessage = config.has("devMessage") ? config.get("devMessage").asText() : "";

        // Load Configuration
        temperature = config.has("temperature") ? config.get("temperature").asDouble() : 1d;
        topP = config.has("topP") ? config.get("topP").asDouble() : 1d;
        frequencyPenalty = config.has("frequencyPenalty") ? config.get("frequencyPenalty").asDouble() : 0d;
        presencePenalty = config.has("presencePenalty") ? config.get("presencePenalty").asDouble() : 0d;
        n = config.has("n") ? config.get("n").asLong() : 1L;
        maxTokens = config.has("maxTokens") ? config.get("maxTokens").asInt() : 120;
    }

    public static void setTemperature(Double temperature) {
        Builder.temperature = temperature;
    }

    public static void setTopP(Double topP) {
        Builder.topP = topP;
    }

    public static void setFrequencyPenalty(Double frequencyPenalty) {
        Builder.frequencyPenalty = frequencyPenalty;
    }

    public static void setPresencePenalty(Double presencePenalty) {
        Builder.presencePenalty = presencePenalty;
    }

    public static void setN(Long n) {
        Builder.n = n;
    }

    public static void setMaxTokens(Integer maxTokens) {
        Builder.maxTokens = maxTokens;
    }

    public static void setChatModel(ChatModel chatModel) {
        Builder.chatModel = chatModel;
    }

    public static void setUser(String user) {
        Builder.user = user;
    }

    public static void setDevMessage(String devMessage) {
        Builder.devMessage = devMessage;
    }

    // Getter
    public static Double getTemperature() {
        return temperature;
    }

    public static Double getTopP() {
        return topP;
    }

    public static Double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public static Double getPresencePenalty() {
        return presencePenalty;
    }

    public static Long getN() {
        return n;
    }

    public static Integer getMaxTokens() {
        return maxTokens;
    }

    public static ChatModel getChatModel() {
        return chatModel;
    }

    public static String getUser() {
        return user;
    }

    public static String getDevMessage() {
        return devMessage;
    }
}