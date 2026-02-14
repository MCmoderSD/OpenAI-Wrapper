import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.ChatPrompt;
import de.MCmoderSD.openai.services.ChatService;
import tools.jackson.databind.JsonNode;

import static de.MCmoderSD.openai.models.ChatModel.*;

void main() {

    // Load Config
    JsonNode config = JsonUtility.getInstance().loadResource("/config.json");

    // Initialize OpenAI
    OpenAI openAI = new OpenAI(config.get("apiKey").asString());

    // Configure Chat Service
    ChatService service = ChatService.builder()
            .setInstructions("Don't use markdown or formatting!")
            .setModel(GPT_5_NANO)
            .build(openAI);

    ChatPrompt chatPrompt = null;
    String userInput;

    IO.println("Type 'exit' to end the conversation.\nYou:");
    while (!(userInput = IO.readln()).equalsIgnoreCase("exit")) {

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