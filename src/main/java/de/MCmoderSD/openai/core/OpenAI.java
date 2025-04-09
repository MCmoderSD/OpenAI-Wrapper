package de.MCmoderSD.openai.core;

import com.fasterxml.jackson.databind.JsonNode;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.HttpResponse;

import com.openai.models.ChatModel;

import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.audio.speech.SpeechCreateParams.ResponseFormat;
import com.openai.models.audio.transcriptions.Transcription;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;

import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;

import com.openai.models.images.Image;
import com.openai.models.images.ImageGenerateParams;
import com.openai.models.images.ImageModel;
import com.openai.models.images.ImagesResponse;

import com.openai.models.moderations.ModerationCreateParams;
import com.openai.models.moderations.ModerationCreateResponse;

import de.MCmoderSD.openai.enums.Language;
import de.MCmoderSD.openai.helper.Builder;

import de.MCmoderSD.openai.model.AudioModel;
import de.MCmoderSD.openai.model.EmbeddingModel;
import de.MCmoderSD.openai.model.ModerationModel;
import de.MCmoderSD.openai.model.SpeechModel;

import de.MCmoderSD.openai.objects.ChatHistory;
import de.MCmoderSD.openai.objects.ChatPrompt;
import de.MCmoderSD.openai.objects.EmbeddingPrompt;
import de.MCmoderSD.openai.objects.ImagePrompt;
import de.MCmoderSD.openai.objects.ModerationPrompt;
import de.MCmoderSD.openai.objects.SpeechPrompt;
import de.MCmoderSD.openai.objects.TranscriptionPrompt;


import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.openai.models.audio.speech.SpeechCreateParams.*;
import static com.openai.models.images.ImageGenerateParams.*;
import static de.MCmoderSD.imageloader.Converter.convertToPNG;
import static de.MCmoderSD.imageloader.Encoder.encodeToDataURI;
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

    // Set Config
    public static void setConfig(JsonNode config) {
        Builder.setConfig(config);
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

    private CreateEmbeddingResponse createEmbedding(EmbeddingCreateParams params) {
        return client.embeddings().create(params);
    }

    // Prompt
    public ChatPrompt prompt(String prompt) {
        return prompt(null, null, null, null, null, null, null, null, null, null, prompt, null);
    }

    // Prompt with Images
    public ChatPrompt prompt(String prompt, @Nullable ArrayList<BufferedImage> images) {
        return prompt(null, null, null, null, null, null, null, null, null, null, prompt, images);
    }

    // Prompt with User
    public ChatPrompt prompt(@Nullable String user, String prompt) {
        return prompt(null, user, null, null, null, null, null, null, null, null, prompt, null);
    }

    // Prompt with ID
    public ChatPrompt prompt(@Nullable Integer id, String prompt) {
        return prompt(null, null, null, null, null, null, null, null, null, id, prompt, null);
    }

    // Prompt with User and ID
    public ChatPrompt prompt(@Nullable String user, @Nullable Integer id, String prompt) {
        return prompt(null, user, null, null, null, null, null, null, null, id, prompt, null);
    }

    // Prompt with all Parameters
    public ChatPrompt prompt(@Nullable ChatModel chatModel, @Nullable String user, @Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable String devMessage, @Nullable Integer id, String prompt, @Nullable ArrayList<BufferedImage> images) {

        // Encode Images
        ArrayList<String> encodedImages = null;
        if (images != null && !images.isEmpty()) for (BufferedImage image : images) {
            try {
                encodedImages = new ArrayList<>();
                encodedImages.add(encodeToDataURI(convertToPNG(image), "png"));
            } catch (IOException e) {
                System.err.println("Error encoding image: " + e.getMessage());
            }
        }

        // Check Parameters
        if (!checkParameter(maxTokens, Temperature, topP, frequencyPenalty, presencePenalty, n, encodedImages)) return null;

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
                messages,           // Messages
                encodedImages       // Images
        );

        // Execute Chat Completion
        var completion = createChatCompletion(params);

        var chatPrompt = new ChatPrompt(prompt, completion);

        // Add ChatPrompt to History
        if (id != null) chatHistory.get(id).addPrompt(chatPrompt);

        return chatPrompt;
    }

    // Transcription
    public TranscriptionPrompt transcription(byte[] data) {
        return transcription(null, null, null, null, data);
    }

    // Transcription with Prompt
    public TranscriptionPrompt transcription(@Nullable String prompt, byte[] data) {
        return transcription(null, null, null, prompt, data);
    }

    // Transcription with Language
    public TranscriptionPrompt transcription(@Nullable Language language, byte[] data) {
        return transcription(null, null, language, null, data);
    }

    // Transcription with Temperature
    public TranscriptionPrompt transcription(@Nullable Double temperature, byte[] data) {
        return transcription(null, temperature, null, null, data);
    }

    // Transcription with Model
    public TranscriptionPrompt transcription(@Nullable AudioModel model, byte[] data) {
        return transcription(model, null, null, null, data);
    }

    public TranscriptionPrompt transcription(@Nullable AudioModel model, @Nullable Double temperature, byte[] data) {
        return transcription(model, temperature, null, null, data);
    }

    public TranscriptionPrompt transcription(@Nullable AudioModel model, @Nullable String prompt, byte[] data) {
        return transcription(model, null, null, prompt, data);
    }

    public TranscriptionPrompt transcription(@Nullable Language language, @Nullable String prompt, byte[] data) {
        return transcription(null, null, language, prompt, data);
    }

    public TranscriptionPrompt transcription(@Nullable AudioModel model, @Nullable Double temperature, @Nullable Language language, byte[] data) {
        return transcription(model, temperature, language, null, data);
    }

    public TranscriptionPrompt transcription(@Nullable AudioModel model, @Nullable Double temperature, @Nullable String prompt, byte[] data) {
        return transcription(model, temperature, null, prompt, data);
    }

    public TranscriptionPrompt transcription(@Nullable AudioModel model,@Nullable Language language, @Nullable String prompt, byte[] data) {
        return transcription(model, null, language, prompt, data);
    }

    public TranscriptionPrompt transcription(@Nullable Double temperature, @Nullable Language language, @Nullable String prompt, byte[] data) {
        return transcription(null, temperature, language, prompt, data);
    }

    // Transcription with all Parameters
    public TranscriptionPrompt transcription(@Nullable AudioModel model, @Nullable Double temperature, @Nullable Language language, @Nullable String prompt, byte[] data) {

        // Check Parameters
        if (!checkParameter(temperature, data)) return null;

        // Variables
        var offset = 0;
        var length = data.length;
        var chunkCount = (int) Math.ceil((double) length / FILE_SIZE_LIMIT);

        // Array of Chunks
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

        // Create Transcription Prompt
        var transcription = new TranscriptionPrompt(params, data, responses);

        // Clean up temporary files
        for (var chunk : chunks) {
            try {
                Files.deleteIfExists(chunk.toPath());
            } catch (IOException e) {
                chunk.deleteOnExit();
            }
        }

        // Return Transcription
        return transcription;
    }

    // Speech
    public SpeechPrompt speech(String prompt) throws IOException {
        return speech(null, null, null, null, null, prompt);
    }

    // Speech with Instruction
    public SpeechPrompt speech(String instruction, String prompt) throws IOException {
        return speech(null, null, null, null, instruction, prompt);
    }

    // Speech with Speed
    public SpeechPrompt speech(@Nullable Double speed,String prompt) throws IOException {
        return speech(null, null, null, speed, null, prompt);
    }

    // Speech with Response Format
    public SpeechPrompt speech(@Nullable ResponseFormat format, String prompt) throws IOException {
        return speech(null, null, format, null, null, prompt);
    }

    // Speech with Voice
    public SpeechPrompt speech(@Nullable Voice voice, String prompt) throws IOException {
        return speech(null, voice, null, null, null, prompt);
    }

    // Speech with Model
    public SpeechPrompt speech(@Nullable SpeechModel speechModel, String prompt) throws IOException {
        return speech(speechModel, null, null, null, null, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable Voice voice, String prompt) throws IOException {
        return speech(speechModel, voice, null, null, null, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable String instruction, String prompt) throws IOException {
        return speech(speechModel, null, null, null, instruction, prompt);
    }

    public SpeechPrompt speech(@Nullable Double speed, @Nullable String instruction, String prompt) throws IOException {
        return speech(null, null, null, speed, instruction, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable Voice voice, @Nullable ResponseFormat format, String prompt) throws IOException {
        return speech(speechModel, voice, format, null, null, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable Voice voice, @Nullable String instruction, String prompt) throws IOException {
        return speech(speechModel, voice, null, null, instruction, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable Double speed, @Nullable String instruction, String prompt) throws IOException {
        return speech(speechModel, null, null, speed, instruction, prompt);
    }

    public SpeechPrompt speech(@Nullable ResponseFormat format, @Nullable Double speed, @Nullable String instruction, String prompt) throws IOException {
        return speech(null, null, format, speed, instruction, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable Voice voice, @Nullable ResponseFormat format, @Nullable Double speed, String prompt) throws IOException {
        return speech(speechModel, voice, format, speed, null, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable Voice voice, @Nullable ResponseFormat format, @Nullable String instruction, String prompt) throws IOException {
        return speech(speechModel, voice, format, null, instruction, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable Voice voice,@Nullable Double speed, @Nullable String instruction, String prompt) throws IOException {
        return speech(speechModel, voice, null, speed, instruction, prompt);
    }

    public SpeechPrompt speech(@Nullable SpeechModel speechModel,@Nullable ResponseFormat format, @Nullable Double speed, @Nullable String instruction, String prompt) throws IOException {
        return speech(speechModel, null, format, speed, instruction, prompt);
    }

    public SpeechPrompt speech(@Nullable Voice voice, @Nullable ResponseFormat format, @Nullable Double speed, @Nullable String instruction, String prompt) throws IOException {
        return speech(null, voice, format, speed, instruction, prompt);
    }

    // SpeechModel with all Parameters
    public SpeechPrompt speech(@Nullable SpeechModel speechModel, @Nullable Voice voice, @Nullable ResponseFormat format, @Nullable Double speed, @Nullable String instruction, String prompt) throws IOException {

        // Check Parameters
        if (!checkParameter(speed, prompt)) return null;

        // Create SpeechModel Params
        var params = Builder.Speech.buildParams(
                speechModel,         // Model
                voice,               // Voice
                format,              // Format
                speed,               // Speed
                instruction,         // Instruction
                prompt               // prompt
        );

        // Execute SpeechModel
        var response = createSpeech(params);

        // Check Status Code
        if (response.statusCode() != 200) {
            var statusCode = response.statusCode();
            String errorMessage = new String(response.body().readAllBytes());
            System.err.println("Error generating speech: " + statusCode + " - " + errorMessage);
            return null;
        }

        // Return SpeechModel Prompt
        return new SpeechPrompt(params, response);
    }

    // Image Generation
    public ArrayList<ImagePrompt> generateImage(String prompt) {
        return generateImage(null, null, null, null, null, null, prompt);
    }

    // Image Generation with Number of Images
    public ArrayList<ImagePrompt> generateImage(@Nullable Integer n, String prompt) {
        return generateImage(null, null, null, null, null, n, prompt);
    }

    // Image Generation with Style
    public ArrayList<ImagePrompt> generateImage(@Nullable Style style, String prompt) {
        return generateImage(null, null, null, null, style, null, prompt);
    }

    // Image Generation with Quality
    public ArrayList<ImagePrompt> generateImage(@Nullable Quality quality, String prompt) {
        return generateImage(null, null, null, quality, null, null, prompt);
    }

    // Image Generation with Size
    public ArrayList<ImagePrompt> generateImage(@Nullable Size size, String prompt) {
        return generateImage(null, null, size, null, null, null, prompt);
    }

    // Image Generation with User
    public ArrayList<ImagePrompt> generateImage(@Nullable String user,String prompt) {
        return generateImage(null, user, null, null, null, null, prompt);
    }

    // Image Generation with Model
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, String prompt) {
        return generateImage(model, null, null, null, null, null, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, String prompt) {
        return generateImage(model, user, null, null, null, null, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable Integer n, String prompt) {
        return generateImage(model, null, null, null, null, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(null, null, null, null, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Size size, String prompt) {
        return generateImage(model, user, size, null, null, null, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Integer n, String prompt) {
        return generateImage(model, user, null, null, null, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(model, null, null, null, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable Quality quality, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(null, null, null, quality, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Size size, @Nullable Quality quality, String prompt) {
        return generateImage(model, user, size, quality, null, null, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Size size,@Nullable Integer n, String prompt) {
        return generateImage(model, user, size, null, null, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(model, user, null, null, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable Quality quality, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(model, null, null, quality, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable Size size, @Nullable Quality quality, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(null, null, size, quality, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Size size, @Nullable Quality quality, @Nullable Style style, String prompt) {
        return generateImage(model, user, size, quality, style, null, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Size size, @Nullable Quality quality, @Nullable Integer n, String prompt) {
        return generateImage(model, user, size, quality, null, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Size size, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(model, user, size, null, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Quality quality, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(model, user, null, quality, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model,@Nullable Size size, @Nullable Quality quality, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(model, null, size, quality, style, n, prompt);
    }

    public ArrayList<ImagePrompt> generateImage(@Nullable String user, @Nullable Size size, @Nullable Quality quality, @Nullable Style style, @Nullable Integer n, String prompt) {
        return generateImage(null, user, size, quality, style, n, prompt);
    }

    // Image Generation with all Parameters
    public ArrayList<ImagePrompt> generateImage(@Nullable ImageModel model, @Nullable String user, @Nullable Size size, @Nullable Quality quality, @Nullable Style style, @Nullable Integer n, String prompt) {

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
                images.add(new ImagePrompt(params, image, created));
            } catch (IOException | URISyntaxException e) {
                System.err.println("Error generating image: " + e.getMessage());
            }
        }

        // Check if Images are empty
        if (images.isEmpty()) return null;

        // Return Images
        return images;
    }

    // ModerationModel
    public ModerationPrompt moderate(String prompt) {
        return moderate(null, prompt);
    }

    // ModerationModel all Parameters
    public ModerationPrompt moderate(@Nullable ModerationModel model, String prompt) {

        // Create ModerationModel Params
        var params = Builder.Moderation.buildParams(
                model,               // Model
                prompt               // prompt
        );

        // Execute ModerationModel
        var response = createModeration(params);

        // Return ModerationModel Prompt
        return new ModerationPrompt(params, response);
    }

    // Embedding
    public EmbeddingPrompt embedding(String prompt) {
        return embedding(null, null, null, prompt);
    }

    // Embedding with Dimensions
    public EmbeddingPrompt embedding(@Nullable Long dimensions, String prompt) {
        return embedding(null, null, dimensions, prompt);
    }

    // Embedding with User
    public EmbeddingPrompt embedding(@Nullable String user, String prompt) {
        return embedding(null, user, null, prompt);
    }

    // Embedding with Model
    public EmbeddingPrompt embedding(@Nullable EmbeddingModel model, String prompt) {
        return embedding(model, null, null, prompt);
    }

    // Embedding with Model and User
    public EmbeddingPrompt embedding(@Nullable EmbeddingModel model, @Nullable String user, String prompt) {
        return embedding(model, user, null, prompt);
    }

    // Embedding with Model and Dimensions
    public EmbeddingPrompt embedding(@Nullable EmbeddingModel model, @Nullable Long dimensions, String prompt) {
        return embedding(model, null, dimensions, prompt);
    }

    // Embedding with User and Dimensions
    public EmbeddingPrompt embedding(@Nullable String user, @Nullable Long dimensions, String prompt) {
        return embedding(null, user, dimensions, prompt);
    }

    // Embedding with all Parameters
    public EmbeddingPrompt embedding(@Nullable EmbeddingModel model, @Nullable String user, @Nullable Long dimensions, String prompt) {

        // Check Parameters
        if (!checkParameter(dimensions, prompt)) return null;

        // Create Embedding Params
        var params = Builder.Embeddings.buildParams(
                model,               // Model
                user,                // User
                dimensions,          // Dimensions
                prompt               // prompt
        );

        // Execute Embedding
        var response = createEmbedding(params);

        // Return Embedding Prompt
        return new EmbeddingPrompt(params, response);
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