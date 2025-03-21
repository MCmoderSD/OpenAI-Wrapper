# OpenAI Wrapper

## Description
This **Java Wrapper** provides seamless integration with the official [**OpenAI Java SDK**](https://www.GitHub.com/openai/openai-java/), offering a simple and straightforward approach to interact with various OpenAI features.
It's the successor of my old [OpenAI-Utility](https://www.GitHub.com/MCmoderSD/OpenAI-Utility/), which was based on the deprecated [OpenAI Library](https://www.GitHub.com/TheoKanning/openai-java/) by [Theo Kanning](https://www.GitHub.com/TheoKanning/).
This new wrapper is completely rewritten and uses the official [OpenAI Java SDK](https://www.GitHub.com/openai/openai-java/) by [OpenAI](https://www.GitHub.com/openai/).

### Supported Features:
- **Chat API**: Generate conversational responses with advanced models.
- **Transcription API**: Accurately transcribe audio files with support for multiple languages.
- **Speech API**: Convert text to speech with customizable voices and formats.
- **Image API**: Generate images from text prompts with various styles and qualities.
- **Moderation API**: Detect and filter inappropriate content from text inputs.

### Planned Features:
- **Web Search API**: Integrate web search capabilities for enhanced data retrieval.

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
    <version>2.4.0</version>
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
    "speed": 1
  },

  "image": {
    "model": "dall-e-2",
    "size": "256x256",
    "quality": "",
    "style": "",
    "n": 1
  },

  "moderation": {
    "model": "omni-moderation-latest"
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
| topP             | Nucleus sampling: `0` (plain) to `1` (creative). (Default: `1`)                                |
| frequencyPenalty | Reduces repetition of words. Values range from `0` to `1`. (Default: `0`)                      |
| presencePenalty  | Discourages repeating words from the conversation. Values range from `0` to `1` (Default: `0`) |
| n                | Number of completions to generate. (Default: `1`)                                              |
| maxTokens        | Maximum tokens in a response. So 500 characters are approximately 125 tokens). (Default `120`) |
| devMessage       | Provides guidance for the bot's behavior.                                                      |

### Transcription Configuration
| **Field**        | **Description**                                                            |
|:-----------------|:---------------------------------------------------------------------------|
| model            | Model used for generating text. (Default: `whisper-1`)                     |
| temperature      | Controls randomness: `0` (deterministic) to `2` (creative). (Default: `0`) |
| language         | Language of the input audio.                                               |
| prompt           | Provides context for the transcription.                                    |

### Speech Configuration
| **Field**        | **Description**                                     |
|:-----------------|:----------------------------------------------------|
| model            | Model used for generating text. (Default: `tts-1`)  |
| format           | Output format for the audio. (Default: `wav`)       |
| voice            | Voice used for speech synthesis. (Default: `alloy`) |
| speed            | Speed of the speech synthesis. (Default: `1`)       |

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

### Moderation Configuration
| **Field** | **Description**                                                     |
|:----------|:--------------------------------------------------------------------|
| model     | Model used for text moderation. (Default: `omni-moderation-latest`) |

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

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(config);

        // Configure OpenAI
        Builder.Chat.setConfig(config);

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
        Builder.Chat.setConfig(config);

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
        Builder.Chat.setConfig(config);

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

### Translation Example
```java
import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class TranslationExample {

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
        Builder.Chat.setConfig(config);

        // Translation Loop
        System.out.println("Enter the text to translate (type 'exit' to quit):\nYou: ");
        while (!(userInput = scanner.nextLine()).equalsIgnoreCase("exit")) {
            System.out.print("Language: ");
            String devMessage = "Please translate the following text to " + scanner.nextLine();
            String response = openAI.prompt(
                    null,               // Chat Model
                    null,               // User
                    null,               // Max Tokens
                    0d,                 // Temperature
                    null,               // Top P
                    null,               // Frequency Penalty
                    null,               // Presence Penalty
                    null,               // Number of Completions
                    devMessage,         // Developer Message
                    userInput,          // User Message
                    null                // ID
            );
            System.out.println("Response: " + response);
            System.out.print("You: ");
        }
    }
}
```

### Speech & Transcription Example
```java
import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;

import java.io.IOException;
import java.net.URISyntaxException;

public class SpeechExample {

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
        Builder.Speech.setConfig(config);
        Builder.Transcription.setConfig(config);

        // Speech Synthesis and Transcription Example
        String text = "Hello, this is a test of the speech synthesis feature.";

        // Generate Speech
        byte[] audioData = openAI.speech(
                null,   // Model (optional)
                null,   // Voice (optional)
                null,   // Response Format (optional)
                null,   // Speed (optional)
                text    // Text (required)
        );

        // Transcribe Speech
        String transcription = openAI.transcribe(
                null,       // Model (optional)
                null,       // Language (optional)
                null,       // Prompt (optional)
                null,       // Temperature (optional)
                audioData   // Audio Data (required)
        );

        // Print Transcription
        System.out.println("Transcription: " + transcription);
    }
}
```

### Image Example
```java
import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.helper.Builder;
import de.MCmoderSD.openai.objects.ImagePrompt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class ImageExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(config);

        System.out.println("Enter your prompt \nYou: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Configure OpenAI
        Builder.Images.setConfig(config);

        // Prompt
        ArrayList<ImagePrompt> response = openAI.generateImage(
                null,   // Model (optional)
                null,   // Size (optional)
                null,   // Response Format (optional)
                null,   // Number of Images (optional)
                null,   // Style (optional)
                null,   // Quality (optional)
                input   // Prompt (required)
        );

        // Print Response
        for (ImagePrompt imagePrompt : response) showImage(imagePrompt);

        // Save image
        for (ImagePrompt imagePrompt : response) saveImage(imagePrompt);
    }

    // Show image
    public static void showImage(ImagePrompt imagePrompt) {

        // Create frame
        JFrame frame = new JFrame(imagePrompt.getInput());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(256, 256);
        frame.setResizable(false);
        frame.setIconImage(imagePrompt.getImage());

        // Create panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagePrompt.getImage(), 0, 0, null);
            }
        };

        // Add panel to frame
        frame.add(panel);

        // Center frame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Show frame
        frame.setVisible(true);
    }

    // Save image to file
    public static void saveImage(ImagePrompt imagePrompt) {
        try {
            ImageIO.write(imagePrompt.getImage(), "png", new File("src/test/resources/assets/" + String.join("-", imagePrompt.getInput().split(" ")) + ".png"));
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }
}
```

### Moderation Example
```java
import com.fasterxml.jackson.databind.JsonNode;

import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.objects.ModerationPrompt;
import de.MCmoderSD.openai.objects.Rating;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ModerationExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);

        // Initialize Scanner
        Scanner scanner = new Scanner(System.in);

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(config);

        String input;
        System.out.println("User Input:");
        while (!(input = scanner.nextLine()).equalsIgnoreCase("exit")) {

            // Prompt
            ModerationPrompt prompt = openAI.moderate(input);

            // Print Response
            Rating rating = new Rating(prompt.getModerations().getFirst());
            String data = rating.getData(Rating.Data.POSITIVE);
            System.out.println("\nPositive Flags:\n" + data);
            System.out.println("Hit: " + (data.split(":").length - 1) + "/13");

            // User Input
            System.out.println("\n\nUser Input:");
        }
    }
}
```