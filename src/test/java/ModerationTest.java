import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.ModerationPrompt;
import de.MCmoderSD.openai.services.ModerationService;

import tools.jackson.databind.JsonNode;

import static de.MCmoderSD.openai.models.ModerationModel.*;
import static de.MCmoderSD.openai.objects.Rating.Data.*;

void main() {

    // Load Config
    JsonNode config = JsonUtility.getInstance().loadResource("/config.json");

    // Initialize OpenAI
    OpenAI openAI = new OpenAI(config.get("apiKey").asString());

    // Configure Service
    ModerationService service = ModerationService.builder()
            .setModel(OMNI_MODERATION_LATEST)
            .build(openAI);

    // Create Prompt
    ModerationPrompt prompt = service.create("I want to kill myself.");

    // Print Moderation Data
    IO.println("ID: " + prompt.getId());
    IO.println("Model: " + prompt.getModel().getName());
    IO.println("Flagged: " + prompt.getRating().isFlagged());
    IO.println(prompt.getRating().getData(POSITIVE));
}