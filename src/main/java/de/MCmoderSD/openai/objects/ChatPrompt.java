package de.MCmoderSD.openai.objects;

import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.models.completions.CompletionUsage;
import de.MCmoderSD.openai.helper.Helper;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Represents a chat prompt containing user input, AI-generated response, and metadata.
 * This class encapsulates all information related to a single chat interaction with the OpenAI API,
 * including tokens usage statistics, response content, and timestamp information.
 */
@SuppressWarnings("unused")
public class ChatPrompt {

    // Parameters
    private final String input;
    private final ChatCompletion output;

    // Data
    private final String id;
    private final Timestamp timestamp;
    private final ChatModel model;
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
     * Constructs a new ChatPrompt with the specified input and output.
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
        model = Helper.getChatModel(output.model());
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
     * Gets the input text for the chat prompt.
     *
     * @return The input text
     */
    public String getInput() {
        return input;
    }

    /**
     * Gets the response from the chat completion API.
     *
     * @return The chat completion response
     */
    public ChatCompletion getOutput() {
        return output;
    }

    /**
     * Gets the ID of the chat completion response.
     *
     * @return The response ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the timestamp of the chat completion response.
     *
     * @return The response timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the chat model used for the chat completion response.
     *
     * @return the chat model
     */
    public ChatModel getModel() {
        return model;
    }

    /**
     * Gets the system fingerprint of the chat completion response.
     *
     * @return The system fingerprint
     */
    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    /**
     * Gets the usage details of the chat completion response.
     *
     * @return The usage details
     */
    public CompletionUsage getUsage() {
        return usage;
    }

    /**
     * Gets the prompt tokens details of the chat completion response.
     *
     * @return The prompt tokens details
     */
    public CompletionUsage.PromptTokensDetails getPromptTokensDetails() {
        return promptTokensDetails;
    }

    /**
     * Gets the completion tokens details of the chat completion response.
     *
     * @return The completion tokens details
     */
    public CompletionUsage.CompletionTokensDetails getCompletionTokensDetails() {
        return completionTokensDetails;
    }

    /**
     * Gets the number of input tokens used in the chat completion response.
     *
     * @return The number of input tokens
     */
    public long getInputTokens() {
        return inputTokens;
    }

    /**
     * Gets the number of cached input tokens used in the chat completion response.
     *
     * @return The number of cached input tokens
     */
    public long getCachedInputTokens() {
        return cachedInputTokens;
    }

    /**
     * Gets the number of output tokens used in the chat completion response.
     *
     * @return The number of output tokens
     */
    public long getOutputTokens() {
        return outputTokens;
    }

    /**
     * Gets the number of reasoning tokens used in the chat completion response.
     *
     * @return The number of reasoning tokens
     */
    public long getReasoningTokens() {
        return reasoningTokens;
    }

    /**
     * Gets the total number of tokens used in the chat completion response.
     *
     * @return The total number of tokens
     */
    public long getTotalTokens() {
        return totalTokens;
    }

    /**
     * Gets the list of choices from the chat completion response.
     *
     * @return The list of choices
     */
    public ArrayList<ChatCompletion.Choice> getChoices() {
        return choices;
    }

    /**
     * Gets the list of messages from the chat completion response.
     *
     * @return The list of messages
     */
    public ArrayList<ChatCompletionMessage> getMessages() {
        return messages;
    }

    /**
     * Gets the list of content strings from the chat completion response.
     *
     * @return The list of content strings
     */
    public ArrayList<String> getContent() {
        return content;
    }

    /**
     * Gets the first content string from the chat completion response.
     *
     * @return The first content string
     */
    public String getText() {
        return content.getFirst();
    }
}