package de.MCmoderSD.openai.models;

import com.openai.models.ReasoningEffort;
import de.MCmoderSD.openai.enums.Modality;
import de.MCmoderSD.openai.enums.Speed;
import de.MCmoderSD.openai.enums.Tool;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static com.openai.models.ReasoningEffort.*;
import static de.MCmoderSD.openai.enums.Modality.*;
import static de.MCmoderSD.openai.enums.Tool.*;
import static java.util.List.of;

@SuppressWarnings("unused")
public enum ChatModel {

    GPT_5_2_PRO(
            "gpt-5.2-pro",
            Speed.SLOWEST,
            21_00,
            168_00,
            of(TEXT, IMAGE),
            of(TEXT),
            of(MEDIUM, HIGH, XHIGH),
            of(WEBSEARCH, FILESEARCH, IMAGEGEN, MCP),
            400_000,
            128_000
    ),

    GPT_5_2(
            "gpt-5.2",
            Speed.MEDIUM,
            1_75,
            14_00,
            of(TEXT, IMAGE),
            of(TEXT),
            of(NONE, LOW, MEDIUM, HIGH, XHIGH),
            of(WEBSEARCH, FILESEARCH, IMAGEGEN, CODE_INTERPRETATION, MCP),
            400_000,
            128_000
    ),

    GPT_5(
            "gpt-5",
            Speed.MEDIUM,
            1_25,
            10_00,
            of(TEXT, IMAGE),
            of(TEXT),
            of(MINIMAL, LOW, MEDIUM, HIGH),
            of(WEBSEARCH, FILESEARCH, IMAGEGEN, CODE_INTERPRETATION, MCP),
            400_000,
            128_000
    ),


    GPT_5_MINI(
            "gpt-5-mini",
            Speed.FAST,
            25,
            2_00,
            of(TEXT, IMAGE),
            of(TEXT),
            of(MINIMAL, LOW, MEDIUM, HIGH),
            of(WEBSEARCH, FILESEARCH, CODE_INTERPRETATION, MCP),
            400_000,
            128_000
    ),

    GPT_5_NANO(
            "gpt-5-nano",
            Speed.FASTEST,
            5,
            40,
            of(TEXT, IMAGE),
            of(TEXT),
            of(MINIMAL, LOW, MEDIUM, HIGH),
            of(WEBSEARCH, FILESEARCH, IMAGEGEN, CODE_INTERPRETATION, MCP),
            400_000,
            128_000
    ),

    GPT_5_CHAT_LATEST(
            "gpt-5-chat-latest",
            Speed.MEDIUM,
            1_25,
            10_00,
            of(TEXT, IMAGE),
            of(TEXT),
            of(),
            of(WEBSEARCH, FILESEARCH, IMAGEGEN, CODE_INTERPRETATION, MCP),
            128_000,
            16_384
    ),

    GPT_4_1(
            "gpt-4.1",
            Speed.SLOWEST,
            2_00,
            8_00,
            of(TEXT, IMAGE),
            of(TEXT),
            of(),
            of(WEBSEARCH, FILESEARCH, IMAGEGEN, CODE_INTERPRETATION, MCP),
            1_047_576,
            32_768
    );

    // Attributes
    private final Speed speed;
    private final BigDecimal inputPrice;
    private final BigDecimal outputPrice;
    private final HashSet<Modality> input;
    private final HashSet<Modality> output;
    private final HashSet<ReasoningEffort> reasoning;
    private final HashSet<Tool> tool;
    private final long contextWindow;
    private final long maxOutputTokens;
    private final String name;
    private final com.openai.models.ChatModel model;

    // Constructor
    ChatModel(
            String name,                        // Name
            Speed speed,                        // Speed
            int inputPrice,                     // Cent Per Million Tokens for Input
            int outputPrice,                    // Cent Per Million Tokens for Output
            List<Modality> input,               // Supported Input Modalities
            List<Modality> output,              // Supported Output Modalities
            List<ReasoningEffort> reasoning,    // Supported Reasoning Effort Levels
            List<Tool> tool,                    // Supported Tool
            long contextWindow,                 // Context Window Size in Tokens
            long maxOutputTokens                // Maximum Output Tokens
    ) {
        // Set Attributes
        this.name = name;
        this.speed = speed;
        this.contextWindow = contextWindow;
        this.maxOutputTokens = maxOutputTokens;

        // Calculate Cost
        this.inputPrice = BigDecimal.valueOf(inputPrice).movePointLeft(8);
        this.outputPrice = BigDecimal.valueOf(outputPrice).movePointLeft(8);

        // Set Modalities and Capabilities
        this.input = new HashSet<>(input);
        this.output = new HashSet<>(output);
        this.reasoning = new HashSet<>(reasoning);
        this.tool = new HashSet<>(tool);

        // Parse Model
        this.model = com.openai.models.ChatModel.of(name());
    }

    // Getter
    public com.openai.models.ChatModel getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getInputPrice() {
        return inputPrice;
    }

    public BigDecimal getOutputPrice() {
        return outputPrice;
    }

    public Speed getSpeed() {
        return speed;
    }

    public HashSet<Modality> getInput() {
        return input;
    }

    public HashSet<Modality> getOutput() {
        return output;
    }

    public HashSet<ReasoningEffort> getReasoning() {
        return reasoning;
    }

    public HashSet<Tool> getTool() {
        return tool;
    }

    public long getContextWindow() {
        return contextWindow;
    }

    public long getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public boolean hasInput(Modality... modality) {
        return input.containsAll(of(modality));
    }

    public boolean hasOutput(Modality... modality) {
        return output.containsAll(of(modality));
    }

    public boolean hasReasoning(ReasoningEffort... reasoningEffort) {
        return reasoning.containsAll(of(reasoningEffort));
    }

    public boolean hasTool(Tool... tool) {
        return this.tool.containsAll(of(tool));
    }

    public BigDecimal getInputCost(long tokens) {
        return inputPrice.multiply(BigDecimal.valueOf(tokens));
    }

    public BigDecimal getOutputCost(long tokens) {
        return outputPrice.multiply(BigDecimal.valueOf(tokens));
    }

    // Static Methods
    public static ChatModel getModel(String name) {
        name = name.replaceAll("-\\d{4}-\\d{2}-\\d{2}", "").toLowerCase().trim();
        for (var model : ChatModel.values()) if (model.getName().equalsIgnoreCase(name)) return model;
        throw new IllegalArgumentException("Invalid model name: " + name);
    }
}