package de.MCmoderSD.openai.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.models.ChatModel;
import com.openai.models.audio.AudioModel;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.audio.speech.SpeechModel;
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

            // Load Module
            if (!config.has("chat")) return;
            JsonNode chat = config.get("chat");

            // Load Setup
            model = chat.has("model") ? Helper.getChatModel(chat.get("model").asText()) : ChatModel.CHATGPT_4O_LATEST;
            user = config.has("user") ? config.get("user").asText() : "";
            devMessage = chat.has("devMessage") ? chat.get("devMessage").asText() : "";

            // Load Configuration
            temperature = chat.has("temperature") ? chat.get("temperature").asDouble() : 1d;
            topP = chat.has("topP") ? chat.get("topP").asDouble() : 1d;
            frequencyPenalty = chat.has("frequencyPenalty") ? chat.get("frequencyPenalty").asDouble() : 0d;
            presencePenalty = chat.has("presencePenalty") ? chat.get("presencePenalty").asDouble() : 0d;
            n = chat.has("n") ? chat.get("n").asLong() : 1L;
            maxTokens = chat.has("maxTokens") ? chat.get("maxTokens").asLong() : 120;
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
            else if (Transcription.language != null) builder.language(Transcription.language.getCode());
            if (!(prompt == null || prompt.isBlank())) builder.prompt(prompt);
            else if (!(Transcription.prompt == null || Transcription.prompt.isBlank())) builder.prompt(Transcription.prompt);
            builder.temperature(temperature != null ? temperature : Transcription.temperature);

            // Set File and Build
            return builder.file(file.toPath()).build();
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Module
            if (!config.has("chat")) return;
            JsonNode transcription = config.get("transcription");

            // Load Setup
            model = transcription.has("model") ? Helper.getAudioModel(transcription.get("model").asText()) : AudioModel.WHISPER_1;
            language = transcription.has("language") ? Language.fromCode(transcription.get("language").asText()) : null;
            prompt = transcription.has("prompt") ? transcription.get("prompt").asText() : "";

            // Load Configuration
            temperature = transcription.has("temperature") ? transcription.get("temperature").asDouble() : 1d;
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

    public static class Speech {

        // Setup
        private static SpeechModel model = SpeechModel.TTS_1;
        private static SpeechCreateParams.Voice voice = SpeechCreateParams.Voice.ALLOY;
        private static SpeechCreateParams.ResponseFormat format = SpeechCreateParams.ResponseFormat.WAV;
        private static Double speed = 1.0;

        // Builder
        public static SpeechCreateParams buildParams(@Nullable SpeechModel model, @Nullable SpeechCreateParams.Voice voice, @Nullable SpeechCreateParams.ResponseFormat format, @Nullable Double speed, String input) {
            return SpeechCreateParams.builder()
                    .model(model != null ? model : Speech.model)                // Model
                    .voice(voice != null ? voice : Speech.voice)                // Voice
                    .responseFormat(format != null ? format : Speech.format)    // Format
                    .speed(speed != null ? speed : Speech.speed)                // Speed
                    .input(input)                                               // Input
                    .build();
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Module
            if (!config.has("speech")) return;
            JsonNode speech = config.get("speech");

            // Load Setup
            model = speech.has("model") ? Helper.getSpeechModel(speech.get("model").asText()) : SpeechModel.TTS_1;
            voice = speech.has("voice") ? Helper.getVoice(speech.get("voice").asText()) : SpeechCreateParams.Voice.ALLOY;
            format = speech.has("format") ? Helper.getResponseFormat(speech.get("format").asText()) : SpeechCreateParams.ResponseFormat.WAV;
            speed = speech.has("speed") ? speech.get("speed").asDouble() : 1.0;
        }

        public static void setModel(SpeechModel model) {
            Speech.model = model;
        }

        public static void setVoice(SpeechCreateParams.Voice voice) {
            Speech.voice = voice;
        }

        public static void setFormat(SpeechCreateParams.ResponseFormat format) {
            Speech.format = format;
        }

        public static void setSpeed(Double speed) {
            Speech.speed = speed;
        }

        // Getter
        public static SpeechModel getModel() {
            return model;
        }

        public static SpeechCreateParams.Voice getVoice() {
            return voice;
        }

        public static SpeechCreateParams.ResponseFormat getFormat() {
            return format;
        }

        public static Double getSpeed() {
            return speed;
        }
    }
}