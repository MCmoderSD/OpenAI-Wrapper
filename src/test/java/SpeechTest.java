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