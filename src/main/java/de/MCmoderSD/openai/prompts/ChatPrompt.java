package de.MCmoderSD.openai.prompts;

import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.Response;
import de.MCmoderSD.openai.models.ChatModel;

import java.time.Instant;

@SuppressWarnings("unused")
public class ChatPrompt {

    // Parameters
    private final ResponseCreateParams input;
    private final Response output;

    // Data
    private final String id;
    private final Instant createdAt;
    private final Instant completedAt;

    // Usage
    private final long inputTokens;
    private final long outputTokens;
    private final long totalTokens;
    private final long cachedInputTokens;
    private final long reasoningTokens;

    // Variables
    private final ChatModel model;
    private final double temperature;
    private final double topP;

    // Content
    private final String content;

    // Constructor
    public ChatPrompt(ResponseCreateParams input, Response output) {

        // Set Parameters
        this.input = input;
        this.output = output;

        // Data
        id = output.id();
        createdAt = Instant.ofEpochSecond(Math.round(output.createdAt()));
        completedAt = Instant.ofEpochSecond(Math.round(output.completedAt().orElse((double) Instant.now().toEpochMilli() / 1000)));

        // Usage
        var usage = output.usage().orElseThrow();
        totalTokens= usage.totalTokens();
        inputTokens = usage.inputTokens();
        outputTokens = usage.outputTokens();
        cachedInputTokens = usage.inputTokensDetails().cachedTokens();
        reasoningTokens = usage.outputTokensDetails().reasoningTokens();

        // Variables
        model = ChatModel.getModel(output.model().asChat().asString());
        temperature = output.temperature().orElseThrow();
        topP = output.topP().orElseThrow();

        // Extract Content
        content = output.output().getLast().asMessage().content().getFirst().asOutputText().text().trim();
    }

    // Getters
    public ResponseCreateParams getInput() {
        return input;
    }

    public Response getOutput() {
        return output;
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
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

    public long getCachedInputTokens() {
        return cachedInputTokens;
    }

    public long getReasoningTokens() {
        return reasoningTokens;
    }

    public ChatModel getModel() {
        return model;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTopP() {
        return topP;
    }

    public String getContent() {
        return content;
    }
}