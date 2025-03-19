package de.MCmoderSD.openai.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.models.ChatModel;
import com.openai.models.audio.AudioModel;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;
import com.openai.models.chat.completions.*;
import de.MCmoderSD.openai.enums.Language;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Builder {

    public static class Chat {

        // Setup
        private static ChatModel model = ChatModel.CHATGPT_4O_LATEST;
        private static String user = "";
        private static String devMessage = "";

        // Configuration
        private static Double temperature = 1d;
        private static Double topP = 1d;
        private static Double frequencyPenalty = 0d;
        private static Double presencePenalty = 0d;
        private static Long n = 1L;
        private static Long maxTokens = 120L;

        // Builder
        public static ChatCompletionCreateParams buildParams(@Nullable ChatModel chatModel, @Nullable String user, @Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable String devMessage, String prompt, @Nullable ArrayList<ChatCompletionMessageParam> messages) {

            var params = ChatCompletionCreateParams.builder()
                    .model(chatModel != null ? chatModel : Chat.model)                                   // Model
                    .user(user != null ? user : Chat.user)                                                   // User
                    .maxCompletionTokens(maxTokens != null ? maxTokens : Chat.maxTokens)                     // Max Tokens
                    .temperature(Temperature != null ? Temperature : Chat.temperature)                       // Temperature
                    .topP(topP != null ? topP : Chat.topP)                                                   // Top P
                    .frequencyPenalty(frequencyPenalty != null ? frequencyPenalty : Chat.frequencyPenalty)   // Frequency Penalty
                    .presencePenalty(presencePenalty != null ? presencePenalty : Chat.presencePenalty)       // Presence Penalty
                    .n(n != null ? n : Chat.n);                                                              // Number of Completions

            // Add Message History
            if (messages != null && !messages.isEmpty()) {
                for (var i = 0; i < messages.size(); i++) {
                    var message = messages.get(i);
                    if (i % 2 == 0) params.addMessage(message.asUser());
                    else params.addMessage(message.asAssistant());
                }
            } else params.addDeveloperMessage(devMessage != null ? devMessage : Chat.devMessage);            // Add Developer Message
            return params.addUserMessage(prompt).build();                                                       // Add User Message
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Setup
            model = config.has("model") ? Helper.getChatModel(config.get("model").asText()) : ChatModel.CHATGPT_4O_LATEST;
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
            Chat.temperature = temperature;
        }

        public static void setTopP(Double topP) {
            Chat.topP = topP;
        }

        public static void setFrequencyPenalty(Double frequencyPenalty) {
            Chat.frequencyPenalty = frequencyPenalty;
        }

        public static void setPresencePenalty(Double presencePenalty) {
            Chat.presencePenalty = presencePenalty;
        }

        public static void setN(Long n) {
            Chat.n = n;
        }

        public static void setMaxTokens(Long maxTokens) {
            Chat.maxTokens = maxTokens;
        }

        public static void setModel(ChatModel model) {
            Chat.model = model;
        }

        public static void setUser(String user) {
            Chat.user = user;
        }

        public static void setDevMessage(String devMessage) {
            Chat.devMessage = devMessage;
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

        public static ChatModel getModel() {
            return model;
        }

        public static String getUser() {
            return user;
        }

        public static String getDevMessage() {
            return devMessage;
        }
    }

    public static class Transcription {

        // Setup
        private static AudioModel model = AudioModel.WHISPER_1;
        private static Language language = null;
        private static String prompt = "";

        // Configuration
        private static Double temperature = 1d;

        // Builder
        public static TranscriptionCreateParams buildParams(@Nullable AudioModel model, @Nullable Double temperature, @Nullable Language language, @Nullable String prompt, File file) {

            // Init Builder
            var builder = TranscriptionCreateParams.builder();

            // Set Parameters
            builder.model(model != null ? model : Transcription.model);
            if (language != null) builder.language(language.getName());
            else if (Transcription.language != null) builder.language(Transcription.language.getName());
            if (!(prompt == null || prompt.isBlank())) builder.prompt(prompt);
            else if (!(Transcription.prompt == null || Transcription.prompt.isBlank())) builder.prompt(Transcription.prompt);
            builder.temperature(temperature != null ? temperature : Transcription.temperature);

            // Set File and Build
            return builder.file(file.toPath()).build();
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Setup
            model = config.has("model") ? Helper.getAudioModel(config.get("model").asText()) : AudioModel.WHISPER_1;
            language = config.has("language") ? Language.fromCode(config.get("language").asText()) : null;
            prompt = config.has("prompt") ? config.get("prompt").asText() : "";

            // Load Configuration
            config = config.get("transcription");
            temperature = config.has("temperature") ? config.get("temperature").asDouble() : 1d;
        }

        public static void setTemperature(Double temperature) {
            Transcription.temperature = temperature;
        }

        public static void setModel(AudioModel model) {
            Transcription.model = model;
        }

        public static void setLanguage(Language language) {
            Transcription.language = language;
        }

        public static void setPrompt(String prompt) {
            Transcription.prompt = prompt;
        }

        // Getter
        public static Double getTemperature() {
            return temperature;
        }

        public static AudioModel getModel() {
            return model;
        }

        public static Language getLanguage() {
            return language;
        }

        public static String getPrompt() {
            return prompt;
        }
    }
}