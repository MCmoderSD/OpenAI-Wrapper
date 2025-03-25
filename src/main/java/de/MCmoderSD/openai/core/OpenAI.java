package de.MCmoderSD.openai.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.HttpResponse;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.audio.AudioModel;
import com.openai.models.audio.transcriptions.Transcription;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;
import com.openai.models.audio.speech.SpeechModel;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.images.Image;
import com.openai.models.images.ImageModel;
import com.openai.models.images.ImagesResponse;
import com.openai.models.images.ImageGenerateParams;
import com.openai.models.moderations.ModerationCreateParams;
import com.openai.models.moderations.ModerationCreateResponse;
import com.openai.models.moderations.ModerationModel;

import de.MCmoderSD.openai.enums.Language;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatHistory;
import de.MCmoderSD.openai.objects.ChatPrompt;
import de.MCmoderSD.openai.objects.ImagePrompt;
import de.MCmoderSD.openai.objects.ModerationPrompt;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static de.MCmoderSD.openai.helper.Helper.*;

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

    private Transcription createTranscription(TranscriptionCreateParams params) {
        return client.audio().transcriptions().create(params).asTranscription();
    }

    private HttpResponse createSpeech(SpeechCreateParams params) {
        return client.audio().speech().create(params);
    }

    private ImagesResponse createImage(ImageGenerateParams params) {
        return client.images().generate(params);
    }

    private ModerationCreateResponse createModeration(ModerationCreateParams params) {
        return client.moderations().create(params);
    }

    // Prompt
    public ChatPrompt prompt(String prompt) {
        return prompt(null, null, null, null, null, null, null, null, null, null, prompt);
    }

    public ChatPrompt prompt(@Nullable String user, String prompt) {
        return prompt(null, user, null, null, null, null, null, null, null, null, prompt);
    }

    // Prompt with ID
    public ChatPrompt prompt(@Nullable Integer id, String prompt) {
        return prompt(null, null, null, null, null, null, null, null, null, id, prompt);
    }

    // Prompt with User and ID
    public ChatPrompt prompt(@Nullable String user, @Nullable Integer id, String prompt) {
        return prompt(null, user, null, null, null, null, null, null, null, id, prompt);
    }

    // Prompt with all Parameters
    public ChatPrompt prompt(@Nullable ChatModel chatModel, @Nullable String user, @Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable String devMessage, @Nullable Integer id, String prompt) {

        // Check Parameters
        if (!checkParameter(maxTokens, Temperature, topP, frequencyPenalty, presencePenalty, n)) return null;

        // Check if Chat History exists
        if (id != null) chatHistory.putIfAbsent(id, new ChatHistory(null));
        var messages = id != null ? chatHistory.get(id).getMessages() : null;

        // Create Chat Completion
        var params = Builder.Chat.buildParams(
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
                messages            // Messages
        );

        // Execute Chat Completion
        var completion = createChatCompletion(params);

        var chatPrompt = new ChatPrompt(prompt, completion);

        // Add ChatPrompt to History
        if (id != null) chatHistory.get(id).addPrompt(chatPrompt);

        return chatPrompt;
    }

    // Transcription
    public String transcribe(byte[] data) {
        return transcribe(null, null, null, null, data);
    }

    // Transcription with Language
    public String transcribe(Language language, byte[] data) {
        return transcribe(null, null, null, null, data);
    }

    // Transcription with Prompt
    public String transcribe(@Nullable String prompt, byte[] data) {
        return transcribe(null, null, null, prompt, data);
    }

    // Transcription with Language and Prompt
    public String transcribe(@Nullable Language language, @Nullable String prompt, byte[] data) {
        return transcribe(null, language, prompt, data, null);
    }

    // Transcription with Language, Prompt and ID
    public String transcribe(@Nullable Double temperature, @Nullable Language language, @Nullable String prompt, byte[] data, Integer id) {
        return transcribe(null, temperature, language, prompt, data);
    }

    // Transcription with all Parameters
    public String transcribe(@Nullable AudioModel model, @Nullable Double temperature, @Nullable Language language, @Nullable String prompt, byte[] data) {

        // Check Parameters
        if (!checkParameter(temperature, data)) return null;

        // Variables
        var offset = 0;
        var length = data.length;
        var chunkCount = (int) Math.ceil((double) length / FILE_SIZE_LIMIT);

        // Array of Chunks
        var transcription = new StringBuilder();
        var chunks = new File[chunkCount];
        var params = new TranscriptionCreateParams[chunkCount];
        var responses = new Transcription[chunkCount];

        // Split Audio Data into Chunks
        for (var i = 0; i < chunkCount; i++) {
            var chunk = new byte[Math.min(FILE_SIZE_LIMIT, length - offset)];
            System.arraycopy(data, offset, chunk, 0, chunk.length);
            try {
                chunks[i] = File.createTempFile(String.valueOf(Arrays.hashCode(chunk)), ".wav");
                try (var stream = new FileOutputStream(chunks[i])) {
                    stream.write(chunk);
                }
            } catch (IOException e) {
                System.err.println("Error writing chunk " + i + ": " + e.getMessage());
            }
            offset += FILE_SIZE_LIMIT;
        }

        // Create Transcription Params
        for (var i = 0; i < chunkCount; i++) {
            params[i] = Builder.Transcription.buildParams(
                    model,               // Model
                    temperature,         // Temperature
                    language,            // Language
                    prompt,              // Prompt
                    chunks[i]            // File
            );
        }

        // Execute Transcription
        for (var i = 0; i < chunkCount; i++) responses[i] = createTranscription(params[i]);

        // Combine Transcription Responses
        for (var i = 0; i < chunkCount; i++) {
            var response = responses[i];
            if (response != null) {
                var content = response.text();
                if (!content.isBlank()) transcription.append(content);
            }
        }

        // Clean up temporary files
        for (var chunk : chunks) {
            try {
                Files.deleteIfExists(chunk.toPath());
            } catch (IOException e) {
                chunk.deleteOnExit();
            }
        }

        // Return Transcription
        return transcription.toString();
    }

    // Speech
    public byte[] speech(String input) throws IOException {
        return speech(null, null, null, null, input);
    }

    // Speech with Speed
    public byte[] speech(@Nullable Double speed, String input) throws IOException {
        return speech(null, null, null, speed, input);
    }

    // Speech with Voice
    public byte[] speech(@Nullable SpeechCreateParams.Voice voice, @Nullable String input) throws IOException {
        return speech(null, voice, null, null, input);
    }

    // Speech with Voice and Speed
    public byte[] speech(@Nullable SpeechCreateParams.Voice voice, @Nullable Double speed, String input) throws IOException {
        return speech(null, voice, null, speed, input);
    }

    // Speech with Voice, Format and Speed
    public byte[] speech(@Nullable SpeechCreateParams.Voice voice, @Nullable SpeechCreateParams.ResponseFormat format, @Nullable Double speed, String input) throws IOException {
        return speech(null, voice, format, speed, input);
    }

    // Speech with all Parameters
    public byte[] speech(@Nullable SpeechModel speechModel, @Nullable SpeechCreateParams.Voice voice, @Nullable SpeechCreateParams.ResponseFormat format, @Nullable Double speed, String input) throws IOException {

        // Check Parameters
        if (!checkParameter(speed, input)) return null;

        // Create Speech Params
        var params = Builder.Speech.buildParams(
                speechModel,         // Model
                voice,               // Voice
                format,              // Format
                speed,               // Speed
                input                // Input
        );

        // Execute Speech
        var response = createSpeech(params);

        // Check Status Code
        if (response.statusCode() != 200) {
            int statusCode = response.statusCode();
            String errorMessage = new String(response.body().readAllBytes());
            System.err.println("Error generating speech: " + statusCode + " - " + errorMessage);
            return null;
        }

        return response.body().readAllBytes();
    }

    // Image Generation
    public ArrayList<ImagePrompt> generateImage(String prompt) {
        return generateImage(null, null, null, null, null, null, prompt);
    }

    // Image Generation with Model
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, String prompt) {
        return generateImage(model, null, null, null, null, null, prompt);
    }

    // Image Generation with User
    public ArrayList<ImagePrompt> generateImage(@Nullable String user, String prompt) {
        return generateImage(null, user, null, null, null, null, prompt);
    }

    // Image Generation with Model and User
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, String prompt) {
        return generateImage(model, user, null, null, null, null, prompt);
    }

    // Image Generation with Size
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Integer n, String prompt) {
        return generateImage(model, user, null, null, null, n, prompt);
    }

    // Image Generation with Size and User
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable ImageGenerateParams.Size size, String prompt) {
        return generateImage(model, user, size, null, null, null, prompt);
    }

    // Image Generation with Size and Style
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable ImageGenerateParams.Size size, @Nullable Integer n, String prompt) {
        return generateImage(model, user, size, null, null, n, prompt);
    }

    // Image Generation with Size and Quality
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable ImageGenerateParams.Size size, @Nullable ImageGenerateParams.Style style, @Nullable Integer n, String prompt) {
        return generateImage(model, user, size, null, style, n, prompt);
    }

    // Image Generation with Size and Quality
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable ImageGenerateParams.Size size, @Nullable ImageGenerateParams.Quality quality, @Nullable Integer n, String prompt) {
        return generateImage(model, user, size, quality, null, n, prompt);
    }

    // Image Generation with Size, Quality and Style
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable ImageGenerateParams.Size size, @Nullable ImageGenerateParams.Quality quality, @Nullable ImageGenerateParams.Style style, String prompt) {
        return generateImage(model, user, size, quality, style, null, prompt);
    }

    // Image Generation with all Parameters
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable ImageGenerateParams.Size size, @Nullable ImageGenerateParams.Quality quality, @Nullable ImageGenerateParams.Style style, @Nullable Integer n, String prompt) {

        // Create Image Params
        var params = Builder.Images.buildParams(
                model,               // Model
                user,                // User
                size,                // Size
                quality,             // Quality
                style,               // Style
                n,                   // Number of Images
                prompt               // Prompt
        );

        // Execute Image Generation
        var response = createImage(params);

        var created = response.created();
        ArrayList<ImagePrompt> images = new ArrayList<>();

        // Extract Images
        for (Image image : response.data()) {
            try {
                images.add(new ImagePrompt(prompt, image, created));
            } catch (IOException | URISyntaxException e) {
                System.err.println("Error generating image: " + e.getMessage());
            }
        }

        // Check if Images are empty
        if (images.isEmpty()) return null;

        // Return Images
        return images;
    }

    // Moderation
    public ModerationPrompt moderate(String input) {
        return moderate(null, input);
    }

    // Moderation with Model
    public ModerationPrompt moderate(@Nullable ModerationModel model, String input) {

        // Create Moderation Params
        var params = Builder.Moderation.buildParams(
                model,               // Model
                input                // Input
        );

        // Execute Moderation
        var response = createModeration(params);

        // Return Moderation Prompt
        return new ModerationPrompt(input, response);
    }

    // Setters
    public void clearChatHistory(Integer id) {
        chatHistory.remove(id);
    }

    public void clearAllChatHistory() {
        chatHistory.clear();
    }

    public void addChatHistory(Integer id, ChatHistory chatHistory) {
        this.chatHistory.put(id, chatHistory);
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