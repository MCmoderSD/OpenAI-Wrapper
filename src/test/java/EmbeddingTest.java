import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.EmbeddingPrompt;
import de.MCmoderSD.openai.services.EmbeddingService;

import tools.jackson.databind.JsonNode;

import static de.MCmoderSD.openai.models.EmbeddingModel.*;

void main() {

    // Load Config
    JsonNode config = JsonUtility.getInstance().loadResource("/config.json");

    // Initialize OpenAI
    OpenAI openAI = new OpenAI(config.get("apiKey").asString());

    // Configure Service
    EmbeddingService service = EmbeddingService.builder()
            .setModel(TEXT_EMBEDDING_3_LARGE)
            .build(openAI);

    // Create Prompt
    EmbeddingPrompt prompt = service.create("Hello World!");

    // Print Embedding Data
    IO.println("Prompt Tokens: " + prompt.getPromptTokens());
    IO.println("Total Tokens: " + prompt.getTotalTokens());
    IO.println("Prompt Cost: " + prompt.getPromptCost());
    IO.println("Total Cost: " + prompt.getTotalCost());
    IO.println("Dimension: " + prompt.getDimension());
    IO.println("Embedding: " + Arrays.toString(prompt.getEmbedding().getVector()));
}