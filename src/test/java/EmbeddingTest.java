import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.EmbeddingPrompt;
import de.MCmoderSD.openai.services.EmbeddingService;

import static de.MCmoderSD.openai.models.EmbeddingModel.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Configure Service
    EmbeddingService service = EmbeddingService.builder()
            .setModel(TEXT_EMBEDDING_3_LARGE)   // Model (required)
            .setUser("Debug-User")              // User (optional)
            .build(openAI);

    // Create Prompt
    EmbeddingPrompt response = service.create("Hello World!");

    // Print Embedding Data
    IO.println("Prompt Tokens: " + response.getPromptTokens());
    IO.println("Total Tokens: " + response.getTotalTokens());
    IO.println("Prompt Cost: " + response.getPromptCost());
    IO.println("Total Cost: " + response.getTotalCost());
    IO.println("Dimension: " + response.getDimension());
    IO.println("Embedding: " + Arrays.toString(response.getEmbedding().getVector()));
}