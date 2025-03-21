package de.MCmoderSD.openai.objects;

import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.models.completions.CompletionUsage;

import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Represents a chat prompt containing user input, AI-generated response, and metadata.
 * This class encapsulates all information related to a single chat interaction with the OpenAI API,
 * including tokens usage statistics, response content, and timestamp information.
 */
@SuppressWarnings("unused")
public class ChatPrompt implements Serializable {

    // Parameters
    private final String input;
    private final ChatCompletion output;

    // Data
    private final String id;
    private final Timestamp timestamp;
    private final String model;
    private final String systemFingerprint;
    private final CompletionUsage usage;
    private final CompletionUsage.PromptTokensDetails promptTokensDetails;
    private final CompletionUsage.CompletionTokensDetails completionTokensDetails;

    // Usage
    private final long inputTokens;
    private final long cachedInputTokens;
    private final long outputTokens;
    private final long reasoningTokens;
    private final long totalTokens;

    // Content
    private final ArrayList<ChatCompletion.Choice> choices;
    private final ArrayList<ChatCompletionMessage> messages;
    private final ArrayList<String> content = new ArrayList<>();

    /**
     * Constructs a ChatPrompt instance with the provided user input and AI-generated response.
     *
     * @param input  The user's input text message
     * @param output The AI-generated response associated with the input
     */
    public ChatPrompt(String input, ChatCompletion output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Data
        id = output.id();
        timestamp = new Timestamp(output.created() * 1000L);
        model = output.model();
        systemFingerprint = output.systemFingerprint().orElse(null);
        usage = output.usage().orElse(null);

        // Extract Usage
        promptTokensDetails = usage != null ? usage.promptTokensDetails().orElse(null) : null;
        completionTokensDetails = usage != null ? usage.completionTokensDetails().orElse(null) : null;

        // Extract Token Usage
        inputTokens = usage != null ? usage.promptTokens() : 0L;
        outputTokens = usage != null ? usage.completionTokens() : 0L;
        totalTokens = usage != null ? usage.promptTokens() : 0L;

        // Extract Cached and Reasoning Tokens
        cachedInputTokens = promptTokensDetails != null ? promptTokensDetails.cachedTokens().orElse(0L) : 0L;
        reasoningTokens = completionTokensDetails != null ? completionTokensDetails.reasoningTokens().orElse(0L) : 0L;

        // Extract Content
        choices = new ArrayList<>(output.choices());
        messages = new ArrayList<>();
        choices.forEach(choice -> messages.add(choice.message()));
        messages.forEach(message -> {
            StringBuilder content = new StringBuilder();
            message.content().ifPresent(content::append);
            if (!content.isEmpty()) this.content.add(content.toString());
        });
    }

    /**
     * Returns the user input text.
     *
     * @return The user's input string
     */
    public String getInput() {
        return input;
    }

    /**
     * Returns the complete ChatCompletion object from the API.
     *
     * @return The ChatCompletion response object
     */
    public ChatCompletion getOutput() {
        return output;
    }

    /**
     * Returns the unique identifier for this chat completion.
     *
     * @return The identifier string
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the timestamp when the response was created.
     *
     * @return The creation timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the model used to generate the response.
     *
     * @return The model identifier string
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the system fingerprint associated with this response.
     *
     * @return The system fingerprint string, or null if not provided
     */
    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    /**
     * Returns the token usage statistics for this completion.
     *
     * @return The CompletionUsage object, or null if not provided
     */
    public CompletionUsage getUsage() {
        return usage;
    }

    /**
     * Returns detailed information about prompt tokens.
     *
     * @return The PromptTokensDetails object, or null if not provided
     */
    public CompletionUsage.PromptTokensDetails getPromptTokensDetails() {
        return promptTokensDetails;
    }

    /**
     * Returns detailed information about completion tokens.
     *
     * @return The CompletionTokensDetails object, or null if not provided
     */
    public CompletionUsage.CompletionTokensDetails getCompletionTokensDetails() {
        return completionTokensDetails;
    }

    /**
     * Returns the number of tokens used in the input prompt.
     *
     * @return The count of input tokens
     */
    public long getInputTokens() {
        return inputTokens;
    }

    /**
     * Returns the number of cached input tokens.
     *
     * @return The count of cached input tokens
     */
    public long getCachedInputTokens() {
        return cachedInputTokens;
    }

    /**
     * Returns the number of tokens in the output completion.
     *
     * @return The count of output tokens
     */
    public long getOutputTokens() {
        return outputTokens;
    }

    /**
     * Returns the number of tokens used for reasoning in the completion.
     *
     * @return The count of reasoning tokens
     */
    public long getReasoningTokens() {
        return reasoningTokens;
    }

    /**
     * Returns the total number of tokens used.
     *
     * @return The total token count
     */
    public long getTotalTokens() {
        return totalTokens;
    }

    /**
     * Returns the list of choices provided in the completion response.
     *
     * @return An ArrayList of Choice objects
     */
    public ArrayList<ChatCompletion.Choice> getChoices() {
        return choices;
    }

    /**
     * Returns the list of message objects from the response.
     *
     * @return An ArrayList of ChatCompletionMessage objects
     */
    public ArrayList<ChatCompletionMessage> getMessages() {
        return messages;
    }

    /**
     * Returns the extracted text content from all messages.
     *
     * @return An ArrayList of content strings
     */
    public ArrayList<String> getContent() {
        return content;
    }

    /**
     * Serializes this ChatPrompt instance to a byte array.
     *
     * @return The serialized ChatPrompt as a byte array
     * @throws IOException If an I/O error occurs during serialization
     */
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(data);
        stream.writeObject(this);
        stream.flush();
        return data.toByteArray();
    }

    /**
     * Deserializes a ChatPrompt instance from a byte array.
     *
     * @param bytes The byte array containing the serialized ChatPrompt
     * @return The deserialized ChatPrompt instance
     * @throws IOException            If an I/O error occurs during deserialization
     * @throws ClassNotFoundException If the class of the serialized object cannot be found
     */
    public static ChatPrompt fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        return (ChatPrompt) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
    }
}