import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.ChatPrompt;
import de.MCmoderSD.openai.services.ChatService;

import static de.MCmoderSD.openai.models.ChatModel.*;
import static com.openai.models.ReasoningEffort.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Instructions
    String instructions = "Talk like a pirate! Don't use markdown or formatting!";

    // Configure Chat Service
    ChatService service = ChatService.builder()
            .setModel(GPT_5_NANO)           // Model (required)
            .setReasoningEffort(MINIMAL)    // Reasoning Effort (optional, default: lowest-available)
            .setInstructions(instructions)  // Instructions (optional)
            .setTemperature(1.0)            // Temperature (optional, default: 1.0)
            .setTopP(1.0)                   // Top P (optional, default: 1.0)
            .setMaxOutputTokens(64)         // Max Output Tokens (optional)
            .build(openAI);

    ChatPrompt chatPrompt = null;
    String userInput;

    IO.println("Type 'exit' to end the conversation.\nYou:");
    while (!(userInput = IO.readln()).equalsIgnoreCase("exit")) {
        if (userInput.trim().isBlank()) continue;

        // Create Chat Prompt
        if (chatPrompt == null) chatPrompt = service.create(userInput);     // New Chat
        else chatPrompt = service.create(userInput, chatPrompt.getId());    // Continue Chat

        // Get Response
        String response = chatPrompt.getContent();

        // Print Response
        IO.println("\nResponse: \n" + response + "\n");
        IO.print("You:\n");
    }
}