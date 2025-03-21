package de.MCmoderSD.openai.objects;

import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Maintains a history of chat prompts and messages exchanged between a user and an AI assistant.
 * Tracks token usage and provides serialization capabilities for persistence.
 */
@SuppressWarnings("unused")
public class ChatHistory implements Serializable {

    // Attributes
    private final ArrayList<ChatPrompt> chatPrompts;
    private final ArrayList<ChatCompletionMessageParam> messages;

    // Variables
    private long inputTokens;
    private long outputTokens;
    private long totalTokens;

    /**
     * Creates a new chat history, optionally with an initial chat prompt.
     *
     * @param chatPrompt The first chat prompt to add to the history, can be null
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
     * Adds a new chat prompt to the history and updates token counts.
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
     * Returns all chat prompts in this history.
     *
     * @return ArrayList of chat prompts
     */
    public ArrayList<ChatPrompt> getPrompts() {
        return chatPrompts;
    }

    /**
     * Returns all message parameters in this history for API communication.
     *
     * @return ArrayList of message parameters
     */
    public ArrayList<ChatCompletionMessageParam> getMessages() {
        return messages;
    }

    /**
     * Returns the total number of input tokens consumed.
     *
     * @return Number of input tokens
     */
    public long getInputTokens() {
        return inputTokens;
    }

    /**
     * Returns the total number of output tokens generated.
     *
     * @return Number of output tokens
     */
    public long getOutputTokens() {
        return outputTokens;
    }

    /**
     * Returns the combined total of input and output tokens.
     *
     * @return Total number of tokens
     */
    public long getTotalTokens() {
        return totalTokens;
    }

    /**
     * Serializes this chat history to a byte array.
     *
     * @return Byte array representation of this chat history
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
     * Deserializes a chat history from a byte array.
     *
     * @param bytes Byte array containing a serialized chat history
     * @return The deserialized ChatHistory object
     * @throws IOException If an I/O error occurs during deserialization
     * @throws ClassNotFoundException If the class of the serialized object cannot be found
     */
    public static ChatHistory fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        return (ChatHistory) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
    }
}