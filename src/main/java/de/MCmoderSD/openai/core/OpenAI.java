package de.MCmoderSD.openai.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.HttpResponse;
import com.openai.models.ChatModel;
import com.openai.models.audio.AudioModel;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.audio.speech.SpeechModel;
import com.openai.models.audio.transcriptions.Transcription;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;
import com.openai.models.chat.completions.*;
import de.MCmoderSD.openai.enums.Language;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.helper.Helper;
import de.MCmoderSD.openai.objects.ChatHistory;
import de.MCmoderSD.openai.objects.ChatPrompt;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
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

    private Transcription createTranscription(TranscriptionCreateParams params) {
        return client.audio().transcriptions().create(params).asTranscription();
    }

    private HttpResponse createSpeech(SpeechCreateParams params) {
        return client.audio().speech().create(params);
    }

    // Prompt
    public String prompt(String prompt) {
        return prompt(null, null, null, null, null, null, null, null, null, prompt, null);
    }

    public String prompt(String user, String prompt) {
        return prompt(null, user, null, null, null, null, null, null, null, prompt, null);
    }

    // Prompt with ID
    public String prompt(Integer id, String prompt) {
        return prompt(null, null, null, null, null, null, null, null, null, prompt, id);
    }

    // Prompt with User and ID
    public String prompt(String user, Integer id, String prompt) {
        return prompt(null, user, null, null, null, null, null, null, null, prompt, id);
    }

    public String prompt(@Nullable ChatModel chatModel, @Nullable String user, @Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable String devMessage, String prompt, Integer id) {

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

        // Get Chat Completion Message
        var message = Helper.getMessage(completion);

        // Add ChatPrompt to History
        if (id != null) chatHistory.get(id).addPrompt(new ChatPrompt(prompt, completion));

        // Return Content
        return Helper.getContent(message);
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

    @SuppressWarnings("resource")
    public String transcribe(@Nullable AudioModel model, @Nullable Double temperature, @Nullable Language language, @Nullable String prompt, byte[] data) {

        // Variables
        var chunkSize = 25 * 1024 * 1024; // 25MB
        var offset = 0;
        var length = data.length;
        var chunkCount = (int) Math.ceil((double) length / chunkSize);

        // Array of Chunks
        var transcription = new StringBuilder();
        var chunks = new File[chunkCount];
        var params = new TranscriptionCreateParams[chunkCount];
        var responses = new Transcription[chunkCount];

        // Split Audio Data into Chunks
        for (var i = 0; i < chunkCount; i++) {
            var chunk = new byte[Math.min(chunkSize, length - offset)];
            System.arraycopy(data, offset, chunk, 0, chunk.length);
            try {
                chunks[i] = File.createTempFile(String.valueOf(Arrays.hashCode(chunk)), ".wav");
                var stream = new FileOutputStream(chunks[i]);
                stream.write(chunk);
            } catch (IOException e) {
                System.err.println("Error writing chunk " + i + ": " + e.getMessage());
            }
            offset += chunkSize;
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
                System.err.println("Error deleting chunk file: " + e.getMessage());
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

    public byte[] speech(@Nullable SpeechModel speechModel, @Nullable SpeechCreateParams.Voice voice, @Nullable SpeechCreateParams.ResponseFormat format, @Nullable Double speed, String input) throws IOException {

        // Create Speech Params
        var params = Builder.Speech.buildParams(
                speechModel,         // Model
                voice,               // Voice
                format,              // Format
                speed,               // Speed
                input                // Input
        );

        // Execute Speech
        return createSpeech(params).body().readAllBytes();
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