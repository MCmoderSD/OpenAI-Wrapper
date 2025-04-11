package de.MCmoderSD.openai.helper;

import com.fasterxml.jackson.databind.JsonNode;

import com.openai.models.ChatModel;
import com.openai.models.ReasoningEffort;
import com.openai.models.audio.AudioResponseFormat;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.audio.speech.SpeechCreateParams.Voice;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;
import com.openai.models.chat.completions.ChatCompletionContentPart;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionCreateParams.WebSearchOptions;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.images.ImageGenerateParams;
import com.openai.models.images.ImageModel;
import com.openai.models.moderations.ModerationCreateParams;

import de.MCmoderSD.openai.enums.Input;
import de.MCmoderSD.openai.enums.Language;
import de.MCmoderSD.openai.enums.SearchContextSize;

import de.MCmoderSD.openai.models.*;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

import static com.openai.models.ChatModel.*;
import static com.openai.models.audio.speech.SpeechCreateParams.ResponseFormat.*;
import static com.openai.models.audio.speech.SpeechCreateParams.Voice.*;
import static com.openai.models.images.ImageGenerateParams.*;
import static com.openai.models.images.ImageGenerateParams.ResponseFormat.*;
import static com.openai.models.images.ImageGenerateParams.Size.*;
import static com.openai.models.images.ImageModel.*;
import static de.MCmoderSD.openai.helper.Helper.addImage;
import static de.MCmoderSD.openai.models.AudioModel.*;
import static de.MCmoderSD.openai.models.EmbeddingModel.*;
import static de.MCmoderSD.openai.models.ModerationModel.*;
import static de.MCmoderSD.openai.models.ReasoningModel.O3_MINI;
import static de.MCmoderSD.openai.models.SearchModel.GPT_4O_SEARCH_PREVIEW;
import static de.MCmoderSD.openai.models.SpeechModel.*;

@SuppressWarnings("unused")
public class Builder {

    public static void setConfig(JsonNode config) {
        Builder.Chat.setConfig(config);
        Builder.Transcription.setConfig(config);
        Builder.Speech.setConfig(config);
        Builder.Images.setConfig(config);
        Builder.Moderation.setConfig(config);
        Builder.Embeddings.setConfig(config);
    }

    public static class Chat {

        // Setup
        private static ChatModel model = CHATGPT_4O_LATEST;
        private static SearchModel searchModel = GPT_4O_SEARCH_PREVIEW;
        private static ReasoningModel reasoningModel = O3_MINI;
        private static String user = null;
        private static String devMessage = null;

        // Location
        private static String country = null;
        private static String city = null;
        private static String region = null;
        private static String timezone = null;

        // Configuration
        private static Double temperature = 1d;
        private static Double topP = 1d;
        private static Double frequencyPenalty = 0d;
        private static Double presencePenalty = 0d;
        private static Long n = 1L;
        private static Long maxTokens = 120L;
        private static SearchContextSize searchContextSize = SearchContextSize.HIGH;
        private static ReasoningEffort reasoningEffort = ReasoningEffort.HIGH;

        // Builder
        public static ChatCompletionCreateParams buildParams(@Nullable ChatModel chatModel, @Nullable String user, @Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable String devMessage, String prompt, @Nullable ArrayList<ChatCompletionMessageParam> messages, @Nullable ArrayList<String> images) {

            var params = ChatCompletionCreateParams.builder()
                    .model(chatModel != null ? chatModel : Chat.model)                                      // Model
                    .user(user != null ? user : Chat.user == null ? "" : Chat.user)                         // User
                    .maxCompletionTokens(maxTokens != null ? maxTokens : Chat.maxTokens)                    // Max Tokens
                    .temperature(Temperature != null ? Temperature : Chat.temperature)                      // Temperature
                    .topP(topP != null ? topP : Chat.topP)                                                  // Top P
                    .frequencyPenalty(frequencyPenalty != null ? frequencyPenalty : Chat.frequencyPenalty)  // Frequency Penalty
                    .presencePenalty(presencePenalty != null ? presencePenalty : Chat.presencePenalty)      // Presence Penalty
                    .n(n != null ? n : Chat.n);                                                             // Number of Completions

            return buildParams(params, devMessage, prompt, messages, images);                               // Finalize Parameters
        }

        // Search Builder
        public static ChatCompletionCreateParams buildParams(@Nullable SearchModel searchModel, @Nullable String user, @Nullable Long maxTokens, @Nullable SearchContextSize searchContextSize, @Nullable String country, @Nullable String region, @Nullable String city, @Nullable String timezone, @Nullable String devMessage, String prompt, @Nullable ArrayList<ChatCompletionMessageParam> messages, @Nullable ArrayList<String> images) {

            // Determine Search Model
            var s = searchModel != null ? searchModel : Chat.searchModel;

            // Check Images
            if (images != null && !images.isEmpty() && !s.hasInput(Input.IMAGE)) throw new IllegalArgumentException("The Search Model " + s.getName() + " does not support image input.");

            // Build Parameters
            var params = ChatCompletionCreateParams.builder()
                    .model(s.getName())                                                                     // Model
                    .user(user != null ? user : Chat.user == null ? "" : Chat.user)                         // User
                    .maxCompletionTokens(maxTokens != null ? maxTokens : Chat.maxTokens)                    // Max Tokens
                    .webSearchOptions(buildParams(searchContextSize, country, city, region, timezone));     // WebSearch Options

            return buildParams(params, devMessage, prompt, messages, images);                               // Finalize Parameters
        }
/*      ToDo: Wait for OpenAI to fix
        // Reasoning Builder
        public static ChatCompletionCreateParams buildParams(@Nullable ReasoningModel reasoningModel, @Nullable String user, @Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable ReasoningEffort reasoningEffort, String prompt, @Nullable ArrayList<ChatCompletionMessageParam> messages, @Nullable ArrayList<String> images) {

            // Determine Reasoning Model
            var r = reasoningModel != null ? reasoningModel : Chat.reasoningModel;

            // Check Images
            if (images != null && !images.isEmpty() && !r.hasInput(Input.IMAGE)) throw new IllegalArgumentException("The Reasoning Model " + r.getName() + " does not support image input.");

            // Build Parameters
            var params = ChatCompletionCreateParams.builder()
                    .model(r.getName())                                                                         // Model
                    .user(user != null ? user : Chat.user == null ? "" : Chat.user)                             // User
                    .maxCompletionTokens(maxTokens != null ? maxTokens : Chat.maxTokens)                        // Max Tokens
                    .temperature(Temperature != null ? Temperature : Chat.temperature)                          // Temperature
                    .topP(topP != null ? topP : Chat.topP)                                                      // Top P
                    .frequencyPenalty(frequencyPenalty != null ? frequencyPenalty : Chat.frequencyPenalty)      // Frequency Penalty
                    .presencePenalty(presencePenalty != null ? presencePenalty : Chat.presencePenalty)          // Presence Penalty
                    .n(n != null ? n : Chat.n)                                                                  // Number of Completions
                    .reasoningEffort(reasoningEffort != null ? reasoningEffort : Chat.reasoningEffort);         // Reasoning Effort

            return buildParams(params, "", prompt, messages, images);                                   // Finalize Parameters
        }
*/
        // Finalize Parameters
        private static ChatCompletionCreateParams buildParams(ChatCompletionCreateParams.Builder params, @Nullable String devMessage, String prompt, @Nullable ArrayList<ChatCompletionMessageParam> messages, @Nullable ArrayList<String> images) {

            // Add Message History
            if (messages != null && !messages.isEmpty()) {
                for (var i = 0; i < messages.size(); i++) {
                    var message = messages.get(i);
                    if (i % 2 == 0) params.addMessage(message.asUser());
                    else params.addMessage(message.asAssistant());
                }
            } else params.addDeveloperMessage(devMessage != null ? devMessage : Chat.devMessage);           // Add Developer Message

            // Add Images
            if (images != null && !images.isEmpty()) {
                ArrayList<ChatCompletionContentPart> parts = new ArrayList<>();
                for (var image : images) parts.add(addImage(image, null));                              // Add Image
                params.addUserMessageOfArrayOfContentParts(parts);
            }

            return params.addUserMessage(prompt).build();                                                   // Add User Message
        }

        // Build WebSearch Options
        public static WebSearchOptions buildParams(@Nullable SearchContextSize searchContextSize, @Nullable String country, @Nullable String city, @Nullable String region, @Nullable String timezone) {

            // Determine User Location
            var approximation = WebSearchOptions.UserLocation.Approximate.builder();
            String s = country != null ? country : Chat.country;
            String r = region != null ? region : Chat.region;
            String c = city != null ? city : Chat.city;
            String t = timezone != null ? timezone : Chat.timezone;

            if (s != null) approximation.country(s);    // Country
            if (r != null) approximation.region(r);     // Region
            if (c != null) approximation.city(c);       // City
            if (t != null) approximation.timezone(t);   // Timezone

            // Build Parameters
            var params = WebSearchOptions.builder().searchContextSize(searchContextSize != null ? searchContextSize.size() : Chat.searchContextSize.size()); // Search Context Size
            if (s != null || c != null || r != null || t != null) params.userLocation(WebSearchOptions.UserLocation.builder().approximate(approximation.build()).build());
            return params.build();
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Module
            if (!config.has("chat")) return;
            JsonNode chat = config.get("chat");

            // Load Setup
            model = chat.has("model") ? Helper.getChatModel(chat.get("model").asText()) : CHATGPT_4O_LATEST;
            searchModel = chat.has("searchModel") ? SearchModel.getModel(chat.get("searchModel").asText()) : GPT_4O_SEARCH_PREVIEW;
            reasoningModel = chat.has("reasoningModel") ? ReasoningModel.getModel(chat.get("reasoningModel").asText()) : O3_MINI;
            user = config.has("user") ? config.get("user").asText() : null;
            devMessage = chat.has("devMessage") ? chat.get("devMessage").asText() : null;

            // Load Location
            country = config.has("country") ? config.get("country").asText() : null;
            city = config.has("city") ? config.get("city").asText() : null;
            region = config.has("region") ? config.get("region").asText() : null;
            timezone = config.has("timezone") ? config.get("timezone").asText() : null;

            // Load Configuration
            temperature = chat.has("temperature") ? chat.get("temperature").asDouble() : 1d;
            topP = chat.has("topP") ? chat.get("topP").asDouble() : 1d;
            frequencyPenalty = chat.has("frequencyPenalty") ? chat.get("frequencyPenalty").asDouble() : 0d;
            presencePenalty = chat.has("presencePenalty") ? chat.get("presencePenalty").asDouble() : 0d;
            n = chat.has("n") ? chat.get("n").asLong() : 1L;
            maxTokens = chat.has("maxTokens") ? chat.get("maxTokens").asLong() : 120;
            searchContextSize = chat.has("searchContextSize") ? SearchContextSize.getSearchContextSize(chat.get("searchContextSize").asText()) : SearchContextSize.HIGH;
            reasoningEffort = chat.has("reasoningEffort") ? Helper.getReasoningEffort(chat.get("reasoningEffort").asText()) : ReasoningEffort.HIGH;
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

        public static void setSearchContextSize(SearchContextSize searchContextSize) {
            Chat.searchContextSize = searchContextSize;
        }

        public static void setReasoningEffort(ReasoningEffort reasoningEffort) {
            Chat.reasoningEffort = reasoningEffort;
        }

        public static void setCountry(String country) {
            Chat.country = country;
        }

        public static void setCity(String city) {
            Chat.city = city;
        }

        public static void setRegion(String region) {
            Chat.region = region;
        }

        public static void setTimezone(String timezone) {
            Chat.timezone = timezone;
        }

        public static void setModel(ChatModel model) {
            Chat.model = model;
        }

        public static void setSearchModel(SearchModel searchModel) {
            Chat.searchModel = searchModel;
        }

        public static void setReasoningModel(ReasoningModel reasoningModel) {
            Chat.reasoningModel = reasoningModel;
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

        public static SearchContextSize getSearchContextSize() {
            return searchContextSize;
        }

        public static ReasoningEffort getReasoningEffort() {
            return reasoningEffort;
        }

        public static String getCountry() {
            return country;
        }

        public static String getCity() {
            return city;
        }

        public static String getRegion() {
            return region;
        }

        public static String getTimezone() {
            return timezone;
        }

        public static ChatModel getModel() {
            return model;
        }

        public static SearchModel getSearchModel() {
            return searchModel;
        }

        public static ReasoningModel getReasoningModel() {
            return reasoningModel;
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
        private static AudioModel model = WHISPER_1;
        private static Language language = null;
        private static String prompt = null;

        // Configuration
        private static Double temperature = 1d;

        // Builder
        public static TranscriptionCreateParams buildParams(@Nullable AudioModel model, @Nullable Double temperature, @Nullable Language language, @Nullable String prompt, File file) {

            // Init Builder
            var builder = TranscriptionCreateParams.builder();

            // Set Parameters
            builder.model(model != null ? model.getModel() : Transcription.model.getModel());
            if (language != null) builder.language(language.getName());
            else if (Transcription.language != null) builder.language(Transcription.language.getCode());
            if (!(prompt == null || prompt.isBlank())) builder.prompt(prompt);
            else if (!(Transcription.prompt == null || Transcription.prompt.isBlank())) builder.prompt(Transcription.prompt);
            builder.temperature(temperature != null ? temperature : Transcription.temperature);
            builder.responseFormat(AudioResponseFormat.JSON);

            // Set File and Build
            return builder.file(file.toPath()).build();
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Module
            if (!config.has("transcription")) return;
            JsonNode transcription = config.get("transcription");

            // Load Setup
            model = transcription.has("model") ? AudioModel.getModel(transcription.get("model").asText()) : WHISPER_1;
            language = transcription.has("language") ? Language.getLanguage(transcription.get("language").asText()) : null;
            prompt = transcription.has("prompt") ? transcription.get("prompt").asText() : null;

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
        private static SpeechModel model = TTS_1;
        private static Voice voice = ALLOY;
        private static SpeechCreateParams.ResponseFormat format = WAV;
        private static Double speed = 1.0;
        private static String instruction = null;

        // Builder
        public static SpeechCreateParams buildParams(@Nullable SpeechModel model, @Nullable Voice voice, @Nullable SpeechCreateParams.ResponseFormat format, @Nullable Double speed, @Nullable String instruction, String prompt) {

            // Determine Instruction
            String i = instruction != null ? instruction : Speech.instruction;

            var params = SpeechCreateParams.builder()
                    .model(model != null ? model.getModel() : Speech.model.getModel())      // Model
                    .voice(voice != null ? voice : Speech.voice)                            // Voice
                    .responseFormat(format != null ? format : Speech.format)                // Format
                    .speed(speed != null ? speed : Speech.speed);                           // Speed

            // Add Instruction
            if (i != null) params.instructions(i);                                          // Instruction
            return params.input(prompt).build();                                            // Prompt
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Module
            if (!config.has("speech")) return;
            JsonNode speech = config.get("speech");

            // Load Setup
            model = speech.has("model") ? SpeechModel.getModel(speech.get("model").asText()) : TTS_1;
            voice = speech.has("voice") ? Helper.getVoice(speech.get("voice").asText()) : ALLOY;
            format = speech.has("format") ? Helper.getResponseFormat(speech.get("format").asText()) : WAV;
            speed = speech.has("speed") ? speech.get("speed").asDouble() : 1.0;
            instruction = speech.has("instruction") ? speech.get("instruction").asText() : null;
        }

        public static void setModel(SpeechModel model) {
            Speech.model = model;
        }

        public static void setVoice(Voice voice) {
            Speech.voice = voice;
        }

        public static void setFormat(SpeechCreateParams.ResponseFormat format) {
            Speech.format = format;
        }

        public static void setSpeed(Double speed) {
            Speech.speed = speed;
        }

        public static void setInstruction(String instruction) {
            Speech.instruction = instruction;
        }

        // Getter
        public static SpeechModel getModel() {
            return model;
        }

        public static Voice getVoice() {
            return voice;
        }

        public static SpeechCreateParams.ResponseFormat getFormat() {
            return format;
        }

        public static Double getSpeed() {
            return speed;
        }

        public static String getInstruction() {
            return instruction;
        }
    }

    public static class Images {

        // Setup
        private static ImageModel model = DALL_E_2;
        private static String user = null;

        // Configuration
        private static Size size = _256X256;
        private static Quality quality = null;
        private static Style style = null;
        private static Integer n = 1;

        // Builder
        public static ImageGenerateParams buildParams(@Nullable ImageModel model, @Nullable String user, @Nullable Size size, @Nullable Quality quality, @Nullable Style style, @Nullable Integer n, String prompt) {

            // Determine Parameters
            ImageModel m = model != null ? model : Images.model;
            Size s = size != null ? size : Images.size;
            Quality q = quality != null ? quality : Images.quality;
            Style y = style != null ? style : Images.style;
            Integer i = n != null ? n : Images.n;

            // Check Model
            if (DALL_E_2.equals(m)) {
                if (q != null || y != null) System.err.println("Warning: DALL-E 2 does not support quality or style parameters, ignoring them.");
                return buildParams(user, s, i, prompt);
            }

            if (ImageModel.DALL_E_3.equals(m)) {
                if (n != null && n > 1) System.err.println("Warning: DALL-E 3 only supports 1 image per prompt, ignoring n parameter.");
                return buildParams(user, s, i, prompt);
            }

            throw new IllegalArgumentException("Invalid Images Model: " + m);
        }

        // DALL-E 2
        public static ImageGenerateParams buildParams(@Nullable String user, @Nullable Size size, @Nullable Integer n, String prompt) {

            // Determine Size and check for DALL-E 2
            Size s = size != null ? size : Images.size;
            if (s == _1024X1792 || s == _1792X1024) throw new IllegalArgumentException("DALL-E 2 only supports sizes 1024x1024, 512x512 and 256x256, no quality or style");

            // Build Parameters
            return builder()
                    .responseFormat(B64_JSON)
                    .model(DALL_E_2)
                    .user(user != null ? user : Images.user == null ? "" : Images.user)
                    .size(s)
                    .n(n != null ? n : Images.n)
                    .prompt(prompt)
                    .build();
        }

        // DALL-E 3
        private static ImageGenerateParams buildParams(@Nullable String user, @Nullable Size size, @Nullable Quality quality, @Nullable Style style, String prompt) {

            // Determine Size and check for DALL-E 3
            Size s = size != null ? size : Images.size;
            if (s == Size._512X512 || s == _256X256) throw new IllegalArgumentException("DALL-E 3 only supports sizes 1024x1024, 1024x1792, 1792x1024 and only 1 image per prompt");

            // Build Parameters
            return builder()
                    .model(ImageModel.DALL_E_3)
                    .user(user != null ? user : Images.user == null ? "" : Images.user)
                    .size(s)
                    .quality(quality != null ? quality : Images.quality)
                    .style(style != null ? style : Images.style)
                    .prompt(prompt)
                    .build();
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Module
            if (!config.has("image")) return;
            JsonNode image = config.get("image");

            // Load Setup
            model = image.has("model") ? Helper.getImageModel(image.get("model").asText()) : DALL_E_2;
            user = config.has("user") ? config.get("user").asText() : null;

            // Load Configuration
            size = image.has("size") ? Helper.getSize(image.get("size").asText()) : _1024X1024;
            quality = image.has("quality") ? Helper.getQuality(image.get("quality").asText()) : null;
            style = image.has("style") ? Helper.getStyle(image.get("style").asText()) : null;
            n = image.has("n") ? image.get("n").asInt() : 1;
        }

        public static void setModel(ImageModel model) {
            Images.model = model;
        }

        public static void setUser(String user) {
            Images.user = user;
        }

        public static void setSize(Size size) {
            Images.size = size;
        }

        public static void setQuality(@Nullable Quality quality) {
            Images.quality = quality;
        }

        public static void setStyle(@Nullable Style style) {
            Images.style = style;
        }

        public static void setN(Integer n) {
            Images.n = n;
        }

        // Getter
        public static ImageModel getModel() {
            return model;
        }

        public static String getUser() {
            return user;
        }

        public static Size getSize() {
            return size;
        }

        public static Quality getQuality() {
            return quality;
        }

        public static Style getStyle() {
            return style;
        }

        public static Integer getN() {
            return n;
        }
    }

    public static class Moderation {

        // Setup
        private static ModerationModel model = OMNI_MODERATION_LATEST;

        // Builder
        public static ModerationCreateParams buildParams(@Nullable ModerationModel model, String input) {
            return ModerationCreateParams.builder()
                    .model(model != null ? model.getModel() : Moderation.model.getModel())  // Model
                    .input(input)                                                           // Input
                    .build();                                                               // Build
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Module
            if (!config.has("moderation")) return;
            JsonNode moderation = config.get("moderation");

            // Load Setup
            model = moderation.has("model") ? ModerationModel.getModel(moderation.get("model").asText()) : OMNI_MODERATION_LATEST;
        }

        public static void setModel(ModerationModel model) {
            Moderation.model = model;
        }
    }

    public static class Embeddings {

        // Setup
        private static EmbeddingModel model = TEXT_EMBEDDING_3_LARGE;
        private static String user = null;

        // Configuration
        private static Long dimensions = null;

        // Builder
        public static EmbeddingCreateParams buildParams(@Nullable EmbeddingModel model, @Nullable String user, @Nullable Long dimensions, String prompt) {

            // Determine Parameters
            var m = model != null ? model : Embeddings.model;
            var u = user != null ? user : Embeddings.user;
            var d = dimensions != null ? dimensions : Embeddings.dimensions;

            // Build Parameters
            var params = EmbeddingCreateParams.builder();
            if (m != null) params.model(m.getModel());      // Model
            if (u != null && !u.isBlank()) params.user(u);  // User

            // Check Model
            if (TEXT_EMBEDDING_3_LARGE.equals(m) && d != null && d > 0) params.dimensions(d);
            else if (d != null && d != 0) System.err.println("Warning: Only text-embedding-3-large supports custom dimensions, ignoring them.");

            // Return
            return params.input(prompt).build();
        }

        // Setter
        public static void setConfig(JsonNode config) {

            // Load Module
            if (!config.has("embeddings")) return;
            JsonNode embeddings = config.get("embeddings");

            // Load Setup
            model = embeddings.has("model") ? EmbeddingModel.getModel(embeddings.get("model").asText()) : TEXT_EMBEDDING_3_LARGE;
            user = config.has("user") ? config.get("user").asText() : null;

            // Load Configuration
            dimensions = embeddings.has("dimensions") ? embeddings.get("dimensions").asLong() : null;
        }

        public static void setModel(EmbeddingModel model) {
            Embeddings.model = model;
        }

        public static void setUser(String user) {
            Embeddings.user = user;
        }

        public static void setDimensions(Long dimensions) {
            Embeddings.dimensions = dimensions;
        }

        // Getter
        public static EmbeddingModel getModel() {
            return model;
        }

        public static String getUser() {
            return user;
        }

        public static Long getDimensions() {
            return dimensions;
        }
    }
}