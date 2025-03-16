package de.MCmoderSD.openai.objects;

import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Represents the history of a chat session, storing chat prompts, messages, and token usage.
 */
@SuppressWarnings("unused")
public class ChatHistory {

    // Attributes
    private final ArrayList<ChatPrompt> chatPrompts;
    private final ArrayList<ChatCompletionMessageParam> messages;

    // Variables
    private long inputTokens;
    private long outputTokens;
    private long totalTokens;

    /**
     * Constructs a new ChatHistory instance.
     *
     * @param chatPrompt An optional initial chat prompt to be added to the history.
     */
    public ChatHistory(@Nullable ChatPrompt chatPrompt) {

        // Initialize Attributes
        chatPrompts = new ArrayList<>();
        messages = new ArrayList<>();

        // Initialize Variables
        inputTokens = 0;
        outputTokens = 0;
        totalTokens = 0;

        // Add ChatPrompt to History
        if (chatPrompt != null) addPrompt(chatPrompt);
    }

    /**
     * Adds a chat prompt to the history and updates token counts.
     *
     * @param chatPrompt The chat prompt to be added.
     */
    public void addPrompt(ChatPrompt chatPrompt) {

        // Add ChatPrompt to History
        chatPrompts.add(chatPrompt);

        // Add User Message to History
        messages.add(ChatCompletionMessageParam.ofUser(ChatCompletionUserMessageParam.builder().content(chatPrompt.getInput()).build()));
        messages.add(ChatCompletionMessageParam.ofAssistant(chatPrompt.getMessage().toParam()));

        // Update Tokens
        inputTokens += chatPrompt.getPromptTokens();
        outputTokens += chatPrompt.getCompletionTokens();
        totalTokens += chatPrompt.getTotalTokens();
    }

    /**
     * Returns the list of chat prompts stored in the history.
     *
     * @return A list of chat prompts.
     */
    public ArrayList<ChatPrompt> getPrompts() {
        return chatPrompts;
    }

    /**
     * Returns the list of chat completion messages stored in the history.
     *
     * @return A list of chat messages.
     */
    public ArrayList<ChatCompletionMessageParam> getMessages() {
        return messages;
    }

    /**
     * Returns the total number of input tokens used.
     *
     * @return The number of input tokens.
     */
    public long getInputTokens() {
        return inputTokens;
    }

    /**
     * Returns the total number of output tokens generated.
     *
     * @return The number of output tokens.
     */
    public long getOutputTokens() {
        return outputTokens;
    }

    /**
     * Returns the total number of tokens used (input + output).
     *
     * @return The total token count.
     */
    public long getTotalTokens() {
        return totalTokens;
    }
}