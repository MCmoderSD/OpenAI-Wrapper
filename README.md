# OpenAI Wrapper

## Description
This **Java Wrapper** provides seamless integration with the official [**OpenAI Java SDK**](https://www.GitHub.com/openai/openai-java/), offering a simple and straightforward approach to interact with various OpenAI features.
It's the successor of my old [OpenAI-Utility](https://www.GitHub.com/MCmoderSD/OpenAI-Utility/), which was based on the deprecated [OpenAI Library](https://www.GitHub.com/TheoKanning/openai-java/) by [Theo Kanning](https://www.GitHub.com/TheoKanning/).
This new wrapper is completely rewritten and uses the official [OpenAI Java SDK](https://www.GitHub.com/openai/openai-java/) by [OpenAI](https://www.GitHub.com/openai/).

### Supported Features:
- **Chat API**: Generate conversational responses with advanced models.
- **Web Search API**: Perform web searches and retrieve relevant information.
- **Reasoning API**: Perform complex reasoning tasks with advanced models.
- **Vision API**: Analyze and interpret images, including image generation.
- **Moderation API**: Detect and filter inappropriate content from text inputs.
- **Transcription API**: Accurately transcribe audio files with support for multiple languages.
- **Speech API**: Convert text to speech with customizable voices.
- **Image API**: Generate images from text prompts with various styles and qualities.
- **Embedding API**: Generate embeddings to measure semantic similarity between text inputs.

### Planned Features:
- **Chat API Streaming**: Stream chat responses for real-time interactions.
- **Transcription API Streaming**: Stream audio transcription for real-time processing.

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
    <version>2.7.0</version>
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
  "baseUrl": null,

  "country": "YOUR_COUNTRY",
  "region": "YOUR_REGION",
  "city": "YOUR_CITY",
  "timezone": "YOUR/TIMEZONE",

  "chat": {
    "model": "gpt-4o",
    "searchModel": "gpt-4o-search-preview",
    "reasoningModel": "o3-mini",
    "temperature": 1,
    "topP": 1,
    "frequencyPenalty": 0,
    "presencePenalty": 0,
    "n" : 1,
    "maxTokens": 120,
    "devMessage": "Answer like a Pirate, use the word 'Arrr' in your sentences and especially at the end. You don't use emojis just common pirate words and especially the Arrr.",
    "searchContextSize": "high",
    "reasoningEffort": "high"
  },

  "moderation": {
    "model": "omni-moderation-latest"
  },

  "transcription": {
    "model": "whisper-1",
    "temperature": 1,
    "language": "en",
    "prompt": "Transcribe the following audio to text."
  },

  "speech": {
    "model": "tts-1-hd",
    "format": "wav",
    "voice": "onyx",
    "speed": 1,
    "instruction": "Talk like a pirate."
  },

  "image": {
    "model": "dall-e-2",
    "size": "256x256",
    "quality": "",
    "style": "",
    "n": 1
  },

  "embeddings": {
    "model": "text-embedding-3-large",
    "dimensions": 3072
  }
}
```
Note: <br>
- Obtain your API key from [OpenAI](https://platform.openai.com/signup).
- The `baseUrl` is used for the OpenAI API. If you want to use the OpenAI API, set it to `null`.
- The `user` field such as the` organizationId` and `projectId` are optional. Leaving them empty or removing them will not affect the functionality of the wrapper.
- The IDs are used for tracking purposes and can be obtained from the [OpenAI dashboard](https://platform.openai.com/settings/organization/general).
- The location is used for web search user location, to improve the search results.
<hr>

### Chat Configuration
| **Field**         | **Description**                                                                                |
|:------------------|:-----------------------------------------------------------------------------------------------|
| model             | Model used for generating text. (Default: `chatgpt-4o-latest`)                                 |
| searchModel       | Model used for web search. (Default: `gpt-4o-search-preview`)                                  |
| temperature       | Controls randomness: `0` (deterministic) to `2` (creative). (Default: `1`)                     |
| topP              | Nucleus sampling: `0` (plain) to `1` (creative). (Default: `1`)                                |
| frequencyPenalty  | Reduces repetition of words. Values range from `0` to `1`. (Default: `0`)                      |
| presencePenalty   | Discourages repeating words from the conversation. Values range from `0` to `1` (Default: `0`) |
| n                 | Number of completions to generate. (Default: `1`)                                              |
| maxTokens         | Maximum tokens in a response. So 500 characters are approximately 125 tokens). (Default `120`) |
| devMessage        | Provides guidance for the bot's behavior.                                                      |
| searchContextSize | Size of the context for web search. Options: `low`, `medium`, `high`. (Default: `high`)        |
| reasoningEffort   | Effort level for reasoning tasks. Options: `low`, `medium`, `high`. (Default: `high`)          |

### Moderation Configuration
| **Field** | **Description**                                                     |
|:----------|:--------------------------------------------------------------------|
| model     | Model used for text moderation. (Default: `omni-moderation-latest`) |

### Transcription Configuration
| **Field**        | **Description**                                                            |
|:-----------------|:---------------------------------------------------------------------------|
| model            | Model used for generating text. (Default: `whisper-1`)                     |
| temperature      | Controls randomness: `0` (deterministic) to `2` (creative). (Default: `0`) |
| language         | Language of the input audio.                                               |
| prompt           | Provides context for the transcription.                                    |

### Speech Configuration
| **Field**    | **Description**                                     |
|:-------------|:----------------------------------------------------|
| model        | Model used for generating text. (Default: `tts-1`)  |
| format       | Output format for the audio. (Default: `wav`)       |
| voice        | Voice used for speech synthesis. (Default: `alloy`) |
| speed        | Speed of the speech synthesis. (Default: `1`)       |
| instructions | Instructions for the speech synthesis.              |

### Image Configuration
| **Field** | **Description**                                         |
|:----------|:--------------------------------------------------------|
| model     | Model used for generating images. (Default: `dall-e-2`) |
| size      | Size of the generated image. (Default: `256x256`)       |
| quality   | Quality of the generated image. (Default: `standard`)   |
| style     | Style of the generated image. (Default: `vivid`)        |
| n         | Number of images to generate. (Default: `1`)            |

Options for image generation:
- **model**: `dall-e-2`, `dall-e-3`
- **size**: `256x256`, `512x512`, `1024x1024` (for `dall-e-2`), `1024x1024`, `1792x1024`, `1024x1792` (for `dall-e-3`)
- **quality**: `standard`, `high` (only for `dall-e-3`)
- **style**: `vivid`, `natural` (only for `dall-e-3`)
- **n**: `1` to `10` (only for `dall-e-2`)

### Embedding Configuration
| **Field**  | **Description**                                                           |
|:-----------|:--------------------------------------------------------------------------|
| model      | Model used for generating embeddings. (Default: `text-embedding-3-large`) |
| dimensions | Number of dimensions for the embeddings. (Default: `3072`)                |

Options for embedding generation:
- **model**: `text-embedding-3-large`, `text-embedding-3-small`, `text-embedding-ada-002`
- **dimensions**: `1` to `3072` for `text-embedding-3-large`, otherwise only `1536`

<br>

## Usage Example
```java
import com.fasterxml.jackson.databind.JsonNode;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;

public class Example {

    // Main Method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(config);

        // Configure OpenAI
        Builder.Chat.setConfig(config);

        // Prompt
        String response = openAI.prompt("Where and how often does the letter E appear in the German word Heidelbeere?").getText();

        // Print Response
        System.out.println("Response: " + response);
    }
}
```

### Single Prompt Example
```java
package chat;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class PromptExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);

        // Setup Chat
        System.out.println("Enter your prompt (type 'exit' to quit):");
        System.out.println("You: ");

        // Prompt Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Generate Chat Prompt
            ChatPrompt chatPrompt = openAI.prompt(
                    null,       // Chat Model
                    null,       // User
                    null,       // Max Tokens
                    null,       // Temperature
                    null,       // Top P
                    null,       // Frequency Penalty
                    null,       // Presence Penalty
                    null,       // Number of Completions
                    null,       // Developer Message
                    null,       // ID
                    userInput,  // User Message
                    null        // System Message
            );

            // Get Response
            String response = chatPrompt.getText();

            // Print Response
            System.out.println("\nResponse: \n" + response + "\n");
            System.out.print("You: \n");
        }
    }
}
```

### Chat Example
```java
package chat;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ChatExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        int userId = 1;
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);

        // Setup Chat
        System.out.println("Enter your prompt (type 'exit' to quit):");
        System.out.println("You: ");

        // Chat Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Generate Chat Prompt
            ChatPrompt chatPrompt = openAI.prompt(
                    null,       // Chat Model
                    null,       // User
                    null,       // Max Tokens
                    null,       // Temperature
                    null,       // Top P
                    null,       // Frequency Penalty
                    null,       // Presence Penalty
                    null,       // Number of Completions
                    null,       // Developer Message
                    userId,     // ID
                    userInput,  // User Message
                    null        // Images
            );

            // Get Response
            String response = chatPrompt.getText();

            // Print Response
            System.out.println("\nResponse: \n" + response + "\n");
            System.out.print("You: \n");
        }
    }
}
```

### Translation Example
```java
package chat;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class TranslationExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String instruction = "Please translate the following text to ";
        String userInput;
        String devMessage;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);

        // Setup Chat
        System.out.println("Enter the text to translate (type 'exit' to quit):");
        System.out.print("You: ");

        // Translation Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Get Language Input
            System.out.print("Language: ");
            devMessage = instruction + scanner.nextLine();

            // Generate Chat Prompt
            ChatPrompt chatPrompt = openAI.prompt(
                    null,           // Chat Model
                    null,           // User
                    null,           // Max Tokens
                    0d,             // Temperature (Override Config)
                    null,           // Top P
                    null,           // Frequency Penalty
                    null,           // Presence Penalty
                    null,           // Number of Completions
                    devMessage,     // Developer Message (Override Config)
                    null,           // ID
                    userInput,      // User Message
                    null            // Images
            );

            // Get Response
            String response = chatPrompt.getText();

            // Print Response
            System.out.println("\nResponse: \n" + response + "\n");
            System.out.print("You: \n");
        }
    }
}
```

### Vision Example
```java
package chat;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.imageloader.ImageLoader;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class VisionExample {

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);

        // Setup Prompt
        String devMessage = "Precisely describe the image in detail, so an AI image generation model like DALL-E can recreate it. Then adapt your description to the user's request.";
        String userInput = "Make the cat look like out of LEGO.";

        // Load Image
        BufferedImage image = ImageLoader.loadImage("src/test/resources/assets/katze-mit-hut.png", true);
        ArrayList<BufferedImage> images = new ArrayList<>(Collections.singleton(image));

        // Generate Chat Prompt
        ChatPrompt chatPrompt = openAI.prompt(
                null,       // Chat Model
                null,       // User
                null,       // Max Tokens
                null,       // Temperature
                null,       // Top P
                null,       // Frequency Penalty
                null,       // Presence Penalty
                null,       // Number of Completions
                devMessage, // Developer Message
                null,       // ID
                userInput,  // Prompt
                images      // Image
        );

        // Get Response
        String response = chatPrompt.getText();
        System.out.println("Response: " + response);

        // Print Prompt Data
        System.out.println("Input Tokens: " + chatPrompt.getInputTokens());
        System.out.println("Prompt Cost: " + chatPrompt.getCachedInputTokens());
        System.out.println("Output Tokens: " + chatPrompt.getOutputTokens());
        System.out.println("Total Tokens: " + chatPrompt.getTotalTokens());
    }
}
```

### Web Search Example
```java
package chat;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import static de.MCmoderSD.openai.enums.SearchContextSize.*;

public class SearchExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);

        // Setup Chat
        System.out.println("Enter your search prompt (type 'exit' to quit):");
        System.out.println("You: ");

        // Prompt Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Generate Chat Prompt
            ChatPrompt chatPrompt = openAI.search(
                    null,       // Search Model
                    null,       // User
                    null,       // Max Tokens
                    HIGH,       // Search Context Size
                    null,       // Country
                    null,       // Region
                    null,       // City
                    null,       // Timezone
                    null,       // Developer Message
                    null,       // ID
                    userInput,  // User Message
                    null        // System Message
            );

            // Get Response
            String response = chatPrompt.getText();

            // Print Response
            System.out.println("\nResponse: \n" + response + "\n");
            System.out.print("You: \n");
        }
    }
}
```

### Reasoning Example
```java
package chat;


import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ReasoningExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);
        var maxTokens = Builder.Chat.getMaxTokens() * 3; // 3x for reasoning

        // Setup Chat
        System.out.println("Enter your prompt (type 'exit' to quit):");
        System.out.println("You: ");

        // Prompt Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Generate Chat Prompt
            ChatPrompt chatPrompt = openAI.reasoning(
                    null,       // Chat Model
                    null,       // User
                    maxTokens,  // Max Tokens
                    null,       // Temperature
                    null,       // Top P
                    null,       // Frequency Penalty
                    null,       // Presence Penalty
                    null,       // Number of Completions
                    null,       // Reasoning Effort
                    null,       // ID
                    userInput,  // User Message
                    null        // System Message
            );

            // Get Response
            String response = chatPrompt.getText();

            // Print Response
            System.out.println("\nResponse: \n" + response + "\n");
            System.out.print("You: \n");
        }
    }
}
```

### Moderation Example
```java
package chat;

import com.fasterxml.jackson.databind.JsonNode;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ModerationPrompt;
import de.MCmoderSD.openai.objects.Rating;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ModerationExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main Method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Moderation.setConfig(config);

        // Setup Chat
        System.out.println("User Input:");

        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Prompt
            ModerationPrompt prompt = openAI.moderate(
                    null,       // Model
                    userInput       // User Message
            );

            // Get Rating
            Rating rating = prompt.getRating();

            System.out.println("\nSize: " + rating.getBytes().length + " bytes\n");

            // Get Data
            String data = rating.getData(Rating.Data.POSITIVE);

            // Print Data
            System.out.println("\nPositive Flags:\n" + data);
            System.out.println("Hit: " + (data.split(":").length - 1) + "/13");

            // User Input
            System.out.println("\n\nUser Input: \n");
        }
    }
}
```

### Speech & Transcription Example

```java
package audio;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.SpeechPrompt;
import de.MCmoderSD.openai.objects.TranscriptionPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class SpeechExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Speech.setConfig(config);
        Builder.Transcription.setConfig(config);

        // Setup Input
        System.out.println("Enter the text to convert to speech:");
        userInput = scanner.nextLine();

        // Generate Speech
        SpeechPrompt speechPrompt = openAI.speech(
                null,       // Model
                null,       // Voice
                null,       // Response Format
                null,       // Speed
                null,       // Instructions
                userInput   // Text
        );

        // Audio Data
        byte[] audioData = speechPrompt.getAudioData();

        // Print Audio Data Size
        System.out.println("\nAudio data size: " + audioData.length + " bytes");

        // Transcribe Speech
        TranscriptionPrompt transcription = openAI.transcription(
                null,       // Model
                null,       // Language
                null,       // Prompt
                null,       // Temperature
                audioData   // Audio Data
        );

        // Print Transcription
        System.out.println("\nTranscription: \n" + transcription);
    }
}
```

### Image Example
```java
package image;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ImagePrompt;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class ImageExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Images.setConfig(config);

        // Setup Chat
        System.out.println("Enter your prompt (type 'exit' to quit):");
        System.out.println("You: ");

        // Image Generation Loop
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Generate Images
            ArrayList<ImagePrompt> response = openAI.generateImage(
                    null,       // Model
                    null,       // Size
                    null,       // Response Format
                    null,       // Number of Images
                    null,       // Style
                    null,       // Quality
                    userInput   // Prompt
            );

            // Show images
            for (ImagePrompt imagePrompt : response) showImage(imagePrompt);

            // Save images
            for (ImagePrompt imagePrompt : response) ImageIO.write(imagePrompt.getImage(), "png", new File("src/test/resources/assets/" + String.join("-", imagePrompt.getPrompt().split(" ")) + ".png"));

            // Wait for user to press 'Enter'
            System.out.println("\n\nPress 'Enter' to continue...");
            scanner.nextLine();
            System.out.print("You: \n");

            // Close all open frames
            for (Window window : Window.getWindows()) if (window instanceof JFrame) window.dispose();
        }
    }

    // Show image
    private static void showImage(ImagePrompt imagePrompt) {

        // Get Image
        BufferedImage image = imagePrompt.getImage();
        Dimension size = new Dimension(image.getWidth(), image.getHeight());

        // Create frame
        JFrame frame = new JFrame(imagePrompt.getPrompt());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(size);
        frame.setResizable(false);
        frame.setIconImage(image);

        // Create panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        // Add panel to frame
        panel.setPreferredSize(size);
        frame.add(panel);

        // Center frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Show frame
        frame.setVisible(true);
    }
}
```

### Image Conversion Example
```java
package image;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.imageloader.ImageLoader;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ChatPrompt;
import de.MCmoderSD.openai.objects.ImagePrompt;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class ConvertExample {

    // Main method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Chat.setConfig(config);
        Builder.Images.setConfig(config);

        // Setup Prompt
        String devMessage = "Precisely describe the image in detail, so an AI image generation model like DALL-E can recreate it. Then adapt your description to the user's request.";
        String userInput = "Make the cat look like out of LEGO.";

        // Load Image
        BufferedImage image = ImageLoader.loadImage("src/test/resources/assets/katze-mit-hut.png", true);
        ArrayList<BufferedImage> images = new ArrayList<>(Collections.singleton(image));

        // Generate Chat Prompt
        ChatPrompt chatPrompt = openAI.prompt(
                null,       // Chat Model
                null,       // User
                16384L,     // Max Tokens
                1d,         // Temperature
                null,       // Top P
                null,       // Frequency Penalty
                null,       // Presence Penalty
                null,       // Number of Completions
                devMessage, // Developer Message
                null,       // ID
                userInput,  // Prompt
                images      // Image
        );

        // Get Response
        String response = chatPrompt.getText();
        System.out.println("Response: " + response);

        // Print Prompt Data
        System.out.println("Input Tokens: " + chatPrompt.getInputTokens());
        System.out.println("Prompt Cost: " + chatPrompt.getCachedInputTokens());
        System.out.println("Output Tokens: " + chatPrompt.getOutputTokens());
        System.out.println("Total Tokens: " + chatPrompt.getTotalTokens());

        // Create Image Prompt
        // Generate Images
        ArrayList<ImagePrompt> imagePrompts = openAI.generateImage(
                null,       // Model
                null,       // Size
                null,       // Response Format
                null,       // Number of Images
                null,       // Style
                null,       // Quality
                response    // Prompt
        );

        // Show images
        for (ImagePrompt imagePrompt : imagePrompts) showImage(imagePrompt);

        // Save images
        for (ImagePrompt imagePrompt : imagePrompts) ImageIO.write(imagePrompt.getImage(), "png", new File("src/test/resources/assets/" + String.join("-", userInput.split(" ")) + ".png"));
    }

    // Show image
    private static void showImage(ImagePrompt imagePrompt) {

        // Get Image
        BufferedImage image = imagePrompt.getImage();
        Dimension size = new Dimension(image.getWidth(), image.getHeight());

        // Create frame
        JFrame frame = new JFrame(imagePrompt.getPrompt());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(size);
        frame.setResizable(false);
        frame.setIconImage(image);

        // Create panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        // Add panel to frame
        panel.setPreferredSize(size);
        frame.add(panel);

        // Center frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Show frame
        frame.setVisible(true);
    }
}
```

### Embedding Example
```java
package chat;

import com.fasterxml.jackson.databind.JsonNode;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.Embedding;
import de.MCmoderSD.openai.objects.EmbeddingPrompt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class EmbeddingExample {

    // Scanner for user input
    private final static Scanner scanner = new Scanner(System.in);

    // Main Method
    public static void main(String[] args) throws IOException, URISyntaxException {

        // Variables
        String userInput;

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);
        String apiKey = config.get("apiKey").asText();

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(
                apiKey,     // API Key (required)
                null,       // Organization (optional)
                null        // Project (optional)
        );

        // Configure OpenAI
        Builder.Embeddings.setConfig(config);

        // Setup Chat
        System.out.println("User Input A: ");

        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Prompt
            EmbeddingPrompt promptA = openAI.embedding(
                    null,
                    null,
                    null,
                    userInput
            );

            // Get Embedding
            Embedding vectorA = promptA.getEmbedding();

            // Print Prompt A Data
            System.out.println("\nPrompt A Data:");
            System.out.println("Prompt Tokens : " + promptA.getPromptTokens());
            System.out.println("Total Tokens: " + promptA.getTotalTokens());
            System.out.println("Prompt Cost: " + promptA.getPromptCost());
            System.out.println("Total Cost: " + promptA.getTotalCost());
            System.out.println("Size: " + vectorA.getBytes().length + " bytes\n");

            System.out.println("User Input B: ");
            userInput = scanner.nextLine();

            // Prompt again
            EmbeddingPrompt promptB = openAI.embedding(userInput);

            // Get Embedding
            Embedding vectorB = promptB.getEmbedding();

            // Print Prompt B Data
            System.out.println("\nPrompt B Data:");
            System.out.println("Prompt Tokens : " + promptB.getPromptTokens());
            System.out.println("Total Tokens: " + promptB.getTotalTokens());
            System.out.println("Prompt Cost: " + promptB.getPromptCost());
            System.out.println("Total Cost: " + promptB.getTotalCost());
            System.out.println("Size: " + vectorB.getBytes().length + " bytes\n");

            // Calculate Cosine Similarity
            double similarity = vectorA.cosineSimilarity(vectorB);
            System.out.println("Cosine Similarity: " + similarity);

            // Calculate Euclidean Distance
            double distance = vectorA.euclideanDistance(vectorB);
            System.out.println("Euclidean Distance: " + distance);

            // Calculate Manhattan Distance
            double manhattan = vectorA.manhattanDistance(vectorB);
            System.out.println("Manhattan Distance: " + manhattan);

            // Calculate Angle
            double angle = vectorA.angleBetween(vectorB);
            System.out.println("Angle: " + angle);

            // User Input
            System.out.println("\n\nUser Input A: ");
        }
    }
}
```