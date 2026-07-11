import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.services.ModerationService;

import static de.MCmoderSD.openai.models.ModerationModel.*;
import static de.MCmoderSD.openai.objects.Rating.Data.*;
import static java.lang.IO.println;

void main() {

    // Initialize OpenAI
    var openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Configure Service
    var service = ModerationService.builder()
            .setModel(OMNI_MODERATION_LATEST)   // Model (required)
            .build(openAI);

    // Create Prompt
    var response = service.create("I want to kill myself.");

    // Print Moderation Data
    println("ID: " + response.getId());
    println("Model: " + response.getModel().getName());
    println("Flagged: " + response.getRating().isFlagged());
    println(response.getRating().getData(POSITIVE));
}