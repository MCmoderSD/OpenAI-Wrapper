# OpenAI Wrapper

## Description
This **Java Wrapper** provides seamless integration with the official [**OpenAI Java SDK**](https://www.GitHub.com/openai/openai-java/), offering a simple and straightforward approach to interact with various OpenAI features.
It's the successor of my old [OpenAI-Utility](https://www.GitHub.com/MCmoderSD/OpenAI-Utility/), which was based on the deprecated [OpenAI Library](https://www.GitHub.com/TheoKanning/openai-java/) by [Theo Kanning](https://www.GitHub.com/TheoKanning/).
This new wrapper is completely rewritten and uses the official [OpenAI Java SDK](https://www.GitHub.com/openai/openai-java/) by [OpenAI](https://www.GitHub.com/openai/).

### Supported Features:
- **Moderation API**: Detect and filter inappropriate content from text inputs.
- **Embedding API**: Generate embeddings to measure semantic similarity between text inputs.

## Usage

### Maven
Make sure you have my Sonatype Nexus OSS repository added to your `pom.xml` file:
```xml
<repositories>
    <repository>
        <id>Nexus</id>
        <name>Sonatype Nexus</name>
        <url>https://mcmodersd.de/nexus/repository/maven-releases/</url>
    </repository>
</repositories>
```
Add the dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>de.MCmoderSD</groupId>
    <artifactId>OpenAI</artifactId>
    <version>3.0.0</version>
</dependency>
```

### Moderation API Example
```java
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
```

### Embedding API Example
```java
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
```