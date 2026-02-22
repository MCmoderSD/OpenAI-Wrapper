import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.ModerationPrompt;
import de.MCmoderSD.openai.services.ModerationService;

import static de.MCmoderSD.openai.models.ModerationModel.*;
import static de.MCmoderSD.openai.objects.Rating.Data.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Configure Service
    ModerationService service = ModerationService.builder()
            .setModel(OMNI_MODERATION_LATEST)   // Model (required)
            .build(openAI);

    // Create Prompt
    ModerationPrompt response = service.create("I want to kill myself.");

    // Print Moderation Data
    IO.println("ID: " + response.getId());
    IO.println("Model: " + response.getModel().getName());
    IO.println("Flagged: " + response.getRating().isFlagged());
    IO.println(response.getRating().getData(POSITIVE));
}