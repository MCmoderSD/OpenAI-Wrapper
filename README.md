# OpenAI Wrapper

## Description
This **Java Wrapper** provides seamless integration with the official [**OpenAI Java SDK**](https://www.GitHub.com/openai/openai-java/), offering a simple and straightforward approach to interact with various OpenAI features.
It's the successor of my old [OpenAI-Utility](https://www.GitHub.com/MCmoderSD/OpenAI-Utility/), which was based on the deprecated [OpenAI Library](https://www.GitHub.com/TheoKanning/openai-java/) by [Theo Kanning](https://www.GitHub.com/TheoKanning/).
This new wrapper is completely rewritten and uses the official [OpenAI Java SDK](https://www.GitHub.com/openai/openai-java/) by [OpenAI](https://www.GitHub.com/openai/).

### Supported Features:
- **Chat API**: Create conversational agents that can interact with users in a natural and engaging way.
- **Embedding API**: Generate embeddings to measure semantic similarity between text inputs.
- **Moderation API**: Detect and filter inappropriate content from text inputs.
- **Speech API**: Convert text to speech using OpenAI's advanced TTS models.
- **Translation API**: Transcribe and translate audio into text using OpenAI's Whisper model.

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

### Speech Models:
- [GPT-4o mini TTS](https://platform.openai.com/docs/models/gpt-4o-mini-tts)
- [TTS-1 HD](https://platform.openai.com/docs/models/tts-1-hd)
- [TTS-1](https://platform.openai.com/docs/models/tts-1)

### Translation Models:
- [Whisper](https://platform.openai.com/docs/models/whisper-1)

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
    <version>3.3.1</version>
</dependency>
```

### Chat API Example
```java
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.ChatPrompt;
import de.MCmoderSD.openai.services.ChatService;

import static de.MCmoderSD.openai.models.ChatModel.*;
import static com.openai.models.ReasoningEffort.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Instructions
    String instructions = "Talk like a pirate! Don't use markdown or formatting!";

    // Configure Chat Service
    ChatService service = ChatService.builder()
            .setModel(GPT_5_NANO)           // Model (required)
            .setReasoningEffort(MINIMAL)    // Reasoning Effort (optional, default: lowest-available)
            .setInstructions(instructions)  // Instructions (optional)
            .setTemperature(1.0)            // Temperature (optional, default: 1.0)
            .setTopP(1.0)                   // Top P (optional, default: 1.0)
            .setMaxOutputTokens(64)         // Max Output Tokens (optional)
            .build(openAI);

    ChatPrompt chatPrompt = null;
    String userInput;

    IO.println("Type 'exit' to end the conversation.\nYou:");
    while (!(userInput = IO.readln()).equalsIgnoreCase("exit")) {
        if (userInput.trim().isBlank()) continue;

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
```

### Speech API Example
```java
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.SpeechPrompt;
import de.MCmoderSD.openai.services.SpeechService;

import static de.MCmoderSD.openai.models.SpeechModel.*;
import static de.MCmoderSD.openai.enums.Voice.*;
import static com.openai.models.audio.speech.SpeechCreateParams.ResponseFormat.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Instructions
    String instructions = "Talk like a pirate!";

    // Configure Service
    SpeechService service = SpeechService.builder()
            .setModel(GPT_4O_MINI_TTS)      // Model (required)
            .setInstructions(instructions)  // Instructions (optional)
            .setSpeed(1.0)                  // Speed (optional, default: 1.0)
            .setVoice(MARIN)                // Voice (optional, default: CEDAR)
            .setFormat(WAV)                 // Format (optional, default: WAV)
            .build(openAI);

    // Create Prompt
    SpeechPrompt response = service.create("Hello, how are you doing today?");

    // Write Output to File
    File file = response.toFile(new File("output.wav"));

    // Print File Path
    IO.println("Audio file saved at: " + file.getAbsolutePath());
}
```

### Translation API Example
```java
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.prompts.TranslationPrompt;
import de.MCmoderSD.openai.services.TranslationService;

import static de.MCmoderSD.openai.models.TranslationModel.*;

void main() {

    // Initialize OpenAI
    OpenAI openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Prompt
    String prompt = "Translate the following audio file to English:";

    // Configure Service
    TranslationService service = TranslationService.builder()
            .setModel(WHISPER_1)    // Model (required)
            .setPrompt(prompt)      // Prompt (optional)
            .setTemperature(1.0)    // Temperature (optional, default: 1.0)
            .build(openAI);

    // Input File
    File input = new File("output.wav"); // Supported file formats: flac, mp3, mp4, mpeg, mpga, m4a, wav, webm

    // Create Translation
    TranslationPrompt response = service.create(input);

    // Print Translation
    IO.println(response.getText());
}
```