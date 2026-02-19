import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.ChatPrompt;
import de.MCmoderSD.openai.services.ChatService;

import static de.MCmoderSD.openai.models.ChatModel.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("YOUR API KEY HERE"); // Replace with your actual API key

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