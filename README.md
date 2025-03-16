# OpenAI Wrapper

## Description
This **Java Wrapper** provides seamless integration with the official **OpenAI Java SDK**, offering a simple and straightforward approach to interact with various OpenAI features.
It's the successor of my old [OpenAI-Utility](https://www.GitHub.com/MCmoderSD/OpenAI-Utility/), which was based on the deprecated [OpenAI Library](https://www.GitHub.com/TheoKanning/openai-java/) by [Theo Kanning](https://www.GitHub.com/TheoKanning/).
This new Wrapper is completely rewritten and uses the official [OpenAI Java SDK](https://www.GitHub.com/openai/openai-java/) by [OpenAI](https://www.GitHub.com/openai/).

### Supported Features:
- **Chat API**: Generate conversational responses with advanced models.

### Planned Features:
- **Image API**: Create stunning visuals using OpenAI's image generation tools.
- **Speech API**: Convert text to speech with customizable voices and formats.
- **Transcription API**: Accurately transcribe audio files with support for multiple languages.

This utility simplifies complex interactions, making it easier than ever to harness the power of OpenAI in your Java projects.

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
    <version>2.0.0</version>
</dependency>
```


## Configuration
To configure the utility, provide a `JsonNode` with the following structure:
```json
{
  "apiKey": "YOUR_API_KEY",
  "user": "YOUR_USER",
  "organizationId": "YOUR_ORGANIZATION_ID",
  "projectId": "YOUR_PROJECT_ID",

  "chat": {
    "model": "gpt-4o",
    "temperature": 1,
    "topP": 1,
    "frequencyPenalty": 0,
    "presencePenalty": 0,
    "n" : 1,
    "maxTokens": 120,
    "devMessage": "Answer like a Pirate, use the word 'Arrr' in your sentences and especially at the end. You don't use emojis just common pirate words and especially the Arrr."
  }
}
```
Note: <br>
- Obtain your API key from [OpenAI](https://platform.openai.com/signup). <br>
- The `user` field such as the` organizationId` and `projectId` are optional. Leaving them empty or removing them will not affect the functionality of the wrapper. <br>
- The IDs are used for tracking purposes and can be obtained from the [OpenAI dashboard](https://platform.openai.com/settings/organization/general). <br>
<hr>

### Chat Configuration

| **Field**        | **Description**                                                                                |
|:-----------------|:-----------------------------------------------------------------------------------------------|
| model            | Model used for generating text. (Default: `chatgpt-4o-latest`)                                 |
| temperature      | Controls randomness: `0` (deterministic) to `2` (creative). (Default: `1`)                     |
| maxOutputTokens  | Maximum tokens in a response. So 500 characters are approximately 125 tokens). (Default `120`) |
| topP             | Nucleus sampling: `0` (plain) to `1` (creative). (Default: `1`)                                |
| frequencyPenalty | Reduces repetition of words. Values range from `0` to `1`. (Default: `0`)                      |
| presencePenalty  | Discourages repeating words from the conversation. Values range from `0` to `1` (Default: `0`) |
| devMessage       | Provides guidance for the bot's behavior.                                                      |

## Usage Example
```java
import com.fasterxml.jackson.databind.JsonNode;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;

public class Example {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(config);

        // Configure OpenAI
        Builder.setConfig(config);

        // Prompt
        String response = openAI.prompt("Where and how often does the letter E appear in the German word Heidelbeere?");

        // Print Response
        System.out.println("Response: " + response);
    }
}
```

### Single Prompt Example
```java
import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class PromptExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        Scanner scanner = new Scanner(System.in);
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,             // API Key (required)
                null,               // Organization (optional)
                null                // Project (optional)
        );

        // Configure OpenAI
        Builder.setConfig(config);

        // ChatPrompt Loop
        System.out.println("Enter your prompt (type 'exit' to quit):\nYou: ");
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {
            String response = openAI.prompt(userInput);
            System.out.println("Response: " + response);
            System.out.print("You: ");
        }
    }
}
```

### Chat Example
```java
import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ChatExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        Scanner scanner = new Scanner(System.in);
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();
        int userId = 1; // Example user ID

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,             // API Key (required)
                null,               // Organization (optional)
                null                // Project (optional)
        );

        // Configure OpenAI
        Builder.setConfig(config);

        // ChatPrompt Loop
        System.out.println("Enter your prompt (type 'exit' to quit):\nYou: ");
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {
            String response = openAI.prompt(userId, userInput);
            System.out.println("Response: " + response);
            System.out.print("You: ");
        }
    }
}
```