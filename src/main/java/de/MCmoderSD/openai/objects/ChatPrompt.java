package de.MCmoderSD.openai.objects;

import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.models.completions.CompletionUsage;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Represents a chat prompt containing user input, AI-generated output, metadata, and token usage statistics.
 */
@SuppressWarnings("unused")
public class ChatPrompt {

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
     * @param input  The user's input prompt.
     * @param output The AI-generated response associated with the input.
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

    // Getter
    public String getInput() {
        return input;
    }

    public ChatCompletion getOutput() {
        return output;
    }

    public String getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getModel() {
        return model;
    }

    public String getSystemFingerprint() {
        return systemFingerprint;
    }

    public CompletionUsage getUsage() {
        return usage;
    }

    public CompletionUsage.PromptTokensDetails getPromptTokensDetails() {
        return promptTokensDetails;
    }

    public CompletionUsage.CompletionTokensDetails getCompletionTokensDetails() {
        return completionTokensDetails;
    }

    public long getInputTokens() {
        return inputTokens;
    }

    public long getCachedInputTokens() {
        return cachedInputTokens;
    }

    public long getOutputTokens() {
        return outputTokens;
    }

    public long getReasoningTokens() {
        return reasoningTokens;
    }

    public long getTotalTokens() {
        return totalTokens;
    }

    public ArrayList<ChatCompletion.Choice> getChoices() {
        return choices;
    }

    public ArrayList<ChatCompletionMessage> getMessages() {
        return messages;
    }

    public ArrayList<String> getContent() {
        return content;
    }
}