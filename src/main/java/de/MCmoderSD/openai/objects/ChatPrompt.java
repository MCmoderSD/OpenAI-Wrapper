package de.MCmoderSD.openai.objects;

import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.models.completions.CompletionUsage;

import java.sql.Timestamp;
import java.util.ArrayList;

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

    // Usage
    private final long promptTokens;
    private final long completionTokens;
    private final long totalTokens;

    // Content
    private final ArrayList<ChatCompletion.Choice> choices;
    private final ArrayList<ChatCompletionMessage> messages;
    private final ArrayList<String> content = new ArrayList<>();

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public ChatPrompt(String input, ChatCompletion output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Data
        id = output.id();
        timestamp = new Timestamp(output.created() * 1000L);
        model = output.model();
        systemFingerprint = output.systemFingerprint().get();
        usage = output.usage().get();

        // Extract Usage
        promptTokens = usage.promptTokens();
        completionTokens = usage.completionTokens();
        totalTokens = usage.totalTokens();

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

    // Getters
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

    public long getPromptTokens() {
        return promptTokens;
    }

    public long getCompletionTokens() {
        return completionTokens;
    }

    public long getTotalTokens() {
        return totalTokens;
    }

    public ChatCompletion.Choice getChoice() {
        return choices.getFirst();
    }

    public ChatCompletion.Choice getChoice(int index) {
        return choices.get(index);
    }

    public ArrayList<ChatCompletion.Choice> getChoices() {
        return choices;
    }

    public ChatCompletionMessage getMessage() {
        return choices.getFirst().message();
    }

    public ChatCompletionMessage getMessage(int index) {
        return choices.get(index).message();
    }

    public ArrayList<ChatCompletionMessage> getMessages() {
        return messages;
    }

    public ArrayList<String> getContent() {
        return content;
    }
}