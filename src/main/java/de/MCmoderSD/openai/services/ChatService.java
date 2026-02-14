package de.MCmoderSD.openai.services;

import com.openai.client.OpenAIClient;
import com.openai.models.Reasoning;
import com.openai.models.ReasoningEffort;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.services.blocking.ResponseService;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.models.ChatModel;
import de.MCmoderSD.openai.prompts.ChatPrompt;

import static de.MCmoderSD.openai.models.ChatModel.*;
import static com.openai.models.ReasoningEffort.*;

@SuppressWarnings("unused")
public class ChatService {

    // Attributes
    private final ResponseService service;

    // Parameters
    private final ChatModel model;
    private final ReasoningEffort reasoningEffort;
    private final String instructions;
    private final double temperature;
    private final double topP;
    private final int maxOutputTokens;

    // Constructor
    private ChatService(OpenAIClient client, ChatModel model, ReasoningEffort reasoningEffort, String instructions, double temperature, double topP, int maxOutputTokens) {
        service = client.responses();
        this.model = model;
        this.reasoningEffort = reasoningEffort;
        this.instructions = instructions;
        this.temperature = temperature;
        this.topP = topP;
        this.maxOutputTokens = maxOutputTokens;
    }

    // Builder
    private ResponseCreateParams.Builder initBuilder() {

        // Init Builder
        ResponseCreateParams.Builder builder = ResponseCreateParams.builder();

        // Set Parameters
        builder.model(model.getName());
        builder.temperature(temperature);
        builder.topP(topP);
        if (!instructions.isBlank()) builder.instructions(instructions);
        if (maxOutputTokens > 0) builder.maxOutputTokens(maxOutputTokens);

        // Return Builder
        return builder;
    }

    private ResponseCreateParams buildParams(String prompt, String previousResponseId) {

        // Init Builder
        ResponseCreateParams.Builder builder = initBuilder();

        // Set Parameters
        if (model.hasReasoning(reasoningEffort)) builder.reasoning(Reasoning.builder().effort(reasoningEffort).build());
        if (previousResponseId.startsWith("resp_")) builder.previousResponseId(previousResponseId);
        builder.input(prompt);

        // Build and return
        return builder.build();
    }

    // Create fresh Chat
    public ChatPrompt create(String prompt) {

        // Check Parameters
        if (prompt == null || prompt.isBlank()) throw new IllegalArgumentException("Prompt must not be null or blank");

        // Build Params
        ResponseCreateParams params = buildParams(prompt, "");

        // Create Response
        Response response = service.create(params);

        // Return Chat Prompt
        return new ChatPrompt(params, response);
    }

    // Create Chat with previous chat history
    public ChatPrompt create(String prompt, String previousResponseId) {

        // Check Parameters
        if (prompt == null || prompt.isBlank()) throw new IllegalArgumentException("Prompt must not be null or blank");
        if (previousResponseId == null || previousResponseId.startsWith("resp_")) throw new IllegalArgumentException("Previous response ID must be null or start with 'resp_'");

        // Build Params
        ResponseCreateParams params = buildParams(prompt, previousResponseId);

        // Create Response
        Response response = service.create(params);

        // Return Chat Prompt
        return new ChatPrompt(params, response);
    }

    // Static Builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder Class
    public static class Builder {

        // Parameter
        private ChatModel model = GPT_5_2;
        private ReasoningEffort reasoningEffort;
        private String instructions = "";
        private double temperature = 1;
        private double topP = 1;
        private int maxOutputTokens = 0;

        // Build
        public ChatService build(OpenAI openAI) {

            // Check OpenAI
            if (openAI == null) throw new IllegalArgumentException("OpenAI must not be null");
            if (reasoningEffort == null) reasoningEffort = lowestAvailableReasoning();
            if (maxOutputTokens > model.getMaxOutputTokens()) throw new IllegalArgumentException("Max output tokens must be less than or equal to the model's maximum output tokens");

            // Return Service
            return new ChatService(openAI.getClient(), model, reasoningEffort, instructions, temperature, topP, maxOutputTokens);
        }

        // Set Model
        public Builder setModel(ChatModel model) {

            // Check Model
            if (model == null) throw new IllegalArgumentException("Model must not be null");

            // Set Model
            this.model = model;
            return this;
        }

        // Set Reasoning Effort
        public Builder setReasoningEffort(ReasoningEffort reasoningEffort) {

            // Check Reasoning Effort
            if (reasoningEffort == null) throw new IllegalArgumentException("Reasoning effort must not be null");

            // Set Reasoning Effort
            this.reasoningEffort = reasoningEffort;
            return this;
        }

        // Set Instructions
        public Builder setInstructions(String instructions) {

            // Check Instruction
            if (instructions == null || instructions.isBlank()) throw new IllegalArgumentException("Instructions must not be null or blank");

            // Set Instruction
            this.instructions = instructions;
            return this;
        }

        // Set Temperature
        public Builder setTemperature(double temperature) {

            // Check Temperature
            if (temperature < 0 || temperature > 2) throw new IllegalArgumentException("Temperature must be between 0 and 2");

            // Set Temperature
            this.temperature = temperature;
            return this;
        }

        // Set TopP
        public Builder setTopP(double topP) {

            // Check TopP
            if (topP < 0 || topP > 1) throw new IllegalArgumentException("TopP must be between 0 and 1");

            // Set TopP
            this.topP = topP;
            return this;
        }

        // Set Max Output Tokens
        public Builder setMaxOutputTokens(int maxOutputTokens) {

            // Check Max Output Tokens
            if (maxOutputTokens < 1) throw new IllegalArgumentException("MaxOutputTokens must be positive");

            // Set Max Output Tokens
            this.maxOutputTokens = maxOutputTokens;
            return this;
        }

        // Helper Method
        private ReasoningEffort lowestAvailableReasoning() {
            if (reasoningEffort == null) {
                if (model.hasReasoning(NONE)) return NONE;
                if (model.hasReasoning(MINIMAL)) return MINIMAL;
                if (model.hasReasoning(LOW)) return LOW;
                if (model.hasReasoning(MEDIUM)) return MEDIUM;
                if (model.hasReasoning(HIGH)) return HIGH;
                if (model.hasReasoning(XHIGH)) return XHIGH;
            } else return reasoningEffort;
            return NONE;
        }
    }
}