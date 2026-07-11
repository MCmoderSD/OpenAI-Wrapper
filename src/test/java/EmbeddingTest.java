import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.services.EmbeddingService;

import static de.MCmoderSD.openai.models.EmbeddingModel.*;
import static java.lang.IO.println;

void main() {

    // Initialize OpenAI
    var openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Configure Service
    var service = EmbeddingService.builder()
            .setModel(TEXT_EMBEDDING_3_LARGE)   // Model (required)
            .setUser("Debug-User")              // User (optional)
            .build(openAI);

    // Create Prompt
    var response = service.create("Hello World!");

    // Print Embedding Data
    println("Prompt Tokens: " + response.getPromptTokens());
    println("Total Tokens: " + response.getTotalTokens());
    println("Prompt Cost: " + response.getPromptCost());
    println("Total Cost: " + response.getTotalCost());
    println("Dimension: " + response.getDimension());
    println("Embedding: " + Arrays.toString(response.getEmbedding().getVector()));
}