package de.MCmoderSD.openai.objects;

import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class ChatHistory {

    // Attributes
    private final ArrayList<ChatPrompt> chatPrompts;
    private final ArrayList<ChatCompletionMessageParam> messages;

    // Variables
    private long inputTokens;
    private long outputTokens;
    private long totalTokens;

    // Constructor
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

    // Getters
    public ArrayList<ChatPrompt> getPrompts() {
        return chatPrompts;
    }

    public ArrayList<ChatCompletionMessageParam> getMessages() {
        return messages;
    }

    public long getInputTokens() {
        return inputTokens;
    }

    public long getOutputTokens() {
        return outputTokens;
    }

    public long getTotalTokens() {
        return totalTokens;
    }
}
