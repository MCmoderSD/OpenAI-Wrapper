package de.MCmoderSD.openai.objects;

import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Represents the history of chat prompts and messages.
 * This class maintains a list of chat prompts and their associated messages,
 * along with token usage statistics.
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
     * Constructs a new ChatHistory with an optional initial chat prompt.
     *
     * @param chatPrompt The initial chat prompt to add to the history, or null if none
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
     * Adds a chat prompt to the history and updates token usage statistics.
     *
     * @param chatPrompt The chat prompt to add
     */
    public void addPrompt(ChatPrompt chatPrompt) {

        // Add ChatPrompt to History
        chatPrompts.add(chatPrompt);

        // Add User Message to History
        messages.add(ChatCompletionMessageParam.ofUser(ChatCompletionUserMessageParam.builder().content(chatPrompt.getInput()).build()));
        messages.add(ChatCompletionMessageParam.ofAssistant(chatPrompt.getMessages().getFirst().toParam()));

        // Update Tokens
        inputTokens += chatPrompt.getInputTokens();
        outputTokens += chatPrompt.getOutputTokens();
        totalTokens += chatPrompt.getTotalTokens();
    }

    /**
     * Gets the list of chat prompts in the history.
     *
     * @return The list of chat prompts
     */
    public ArrayList<ChatPrompt> getPrompts() {
        return chatPrompts;
    }

    /**
     * Gets the list of chat completion message parameters in the history.
     *
     * @return The list of chat completion message parameters
     */
    public ArrayList<ChatCompletionMessageParam> getMessages() {
        return messages;
    }

    /**
     * Gets the total number of input tokens used in the chat history.
     *
     * @return The total number of input tokens
     */
    public long getInputTokens() {
        return inputTokens;
    }

    /**
     * Gets the total number of output tokens used in the chat history.
     *
     * @return The total number of output tokens
     */
    public long getOutputTokens() {
        return outputTokens;
    }

    /**
     * Gets the total number of tokens used in the chat history.
     *
     * @return The total number of tokens
     */
    public long getTotalTokens() {
        return totalTokens;
    }
}