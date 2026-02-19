# OpenAI Wrapper

## Description
This **Java Wrapper** provides seamless integration with the official [**OpenAI Java SDK**](https://www.GitHub.com/openai/openai-java/), offering a simple and straightforward approach to interact with various OpenAI features.
It's the successor of my old [OpenAI-Utility](https://www.GitHub.com/MCmoderSD/OpenAI-Utility/), which was based on the deprecated [OpenAI Library](https://www.GitHub.com/TheoKanning/openai-java/) by [Theo Kanning](https://www.GitHub.com/TheoKanning/).
This new wrapper is completely rewritten and uses the official [OpenAI Java SDK](https://www.GitHub.com/openai/openai-java/) by [OpenAI](https://www.GitHub.com/openai/).

### Supported Features:
- **Chat API**: Create conversational agents that can interact with users in a natural and engaging way.
- **Embedding API**: Generate embeddings to measure semantic similarity between text inputs.
- **Moderation API**: Detect and filter inappropriate content from text inputs.

### Supported Models:

#### Chat Models:
- [GPT-5.2 pro](https://platform.openai.com/docs/models/gpt-5.2-pro)
- [GPT-5.2](https://platform.openai.com/docs/models/gpt-5.2)
- [GPT-5](https://platform.openai.com/docs/models/gpt-5)
- [GPT-5 mini](https://platform.openai.com/docs/models/gpt-5-mini)
- [GPT-5 nano](https://platform.openai.com/docs/models/gpt-5-nano)
- [GPT-5 Chat](https://platform.openai.com/docs/models/gpt-5-chat-latest)
- [GPT-4.1](https://platform.openai.com/docs/models/gpt-4.1)

#### Embedding Models:
- [text-embedding-3-large](https://platform.openai.com/docs/models/text-embedding-3-large)
- [text-embedding-3-small](https://platform.openai.com/docs/models/text-embedding-3-small)
- [text-embedding-ada-002](https://platform.openai.com/docs/models/text-embedding-ada-002)

#### Moderation Models:
- [omni-moderation-latest](https://platform.openai.com/docs/models/omni-moderation-latest)

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
    <version>3.1.3</version>
</dependency>
```

### Chat API Example
```java
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
```

### Embedding API Example
```java
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.EmbeddingPrompt;
import de.MCmoderSD.openai.services.EmbeddingService;

import static de.MCmoderSD.openai.models.EmbeddingModel.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("YOUR API KEY HERE"); // Replace with your actual API key

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

### Moderation API Example
```java
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.ModerationPrompt;
import de.MCmoderSD.openai.services.ModerationService;

import static de.MCmoderSD.openai.models.ModerationModel.*;
import static de.MCmoderSD.openai.objects.Rating.Data.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("YOUR API KEY HERE"); // Replace with your actual API key

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