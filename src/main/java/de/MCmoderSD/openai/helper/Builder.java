package de.MCmoderSD.openai.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Builder {

    // Setup
    private static ChatModel chatModel = ChatModel.CHATGPT_4O_LATEST;
    private static String user = "";
    private static String devMessage = "";

    // Configuration
    private static Double temperature = 1d;
    private static Double topP = 1d;
    private static Double frequencyPenalty = 0d;
    private static Double presencePenalty = 0d;
    private static Long n = 1L;
    private static Long maxTokens = 120L;

    public static ChatCompletionCreateParams buildParams(@Nullable ChatModel chatModel, @Nullable String user, @Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable String devMessage, String prompt, @Nullable ArrayList<ChatCompletionMessageParam> messages) {

        // Create Chat Completion Parameters
        var params = ChatCompletionCreateParams.builder()
                .model(chatModel != null ? chatModel : Builder.chatModel)                                   // Model
                .user(user != null ? user : Builder.user)                                                   // User
                .maxCompletionTokens(maxTokens != null ? maxTokens : Builder.maxTokens)                     // Max Tokens
                .temperature(Temperature != null ? Temperature : Builder.temperature)                       // Temperature
                .topP(topP != null ? topP : Builder.topP)                                                   // Top P
                .frequencyPenalty(frequencyPenalty != null ? frequencyPenalty : Builder.frequencyPenalty)   // Frequency Penalty
                .presencePenalty(presencePenalty != null ? presencePenalty : Builder.presencePenalty)       // Presence Penalty
                .n(n != null ? n : Builder.n);                                                              // Number of Completions

        // Add Message History
        if (messages != null && !messages.isEmpty()) {
            for (var i = 0; i < messages.size(); i++) {
                var message = messages.get(i);
                if (i % 2 == 0) params.addMessage(message.asUser());
                else params.addMessage(message.asAssistant());
            }
        } else params.addDeveloperMessage(devMessage != null ? devMessage : Builder.devMessage);            // Add Developer Message
        return params.addUserMessage(prompt).build();                                                       // Add User Message
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
        maxTokens = config.has("maxTokens") ? config.get("maxTokens").asLong() : 120;
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

    public static void setMaxTokens(Long maxTokens) {
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

    public static Long getMaxTokens() {
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