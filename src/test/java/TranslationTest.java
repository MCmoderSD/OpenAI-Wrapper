import de.MCmoderSD.openai.core.OpenAI;
import de.MCmoderSD.openai.services.TranslationService;

import static de.MCmoderSD.openai.models.TranslationModel.*;

void main() {

    // Initialize OpenAI
    var openAI = new OpenAI("sk-proj-"); // Replace with your actual API key

    // Prompt
    var prompt = "Translate the following audio file to English:";

    // Configure Service
    var service = TranslationService.builder()
            .setModel(WHISPER_1)    // Model (required)
            .setPrompt(prompt)      // Prompt (optional)
            .setTemperature(1.0)    // Temperature (optional, default: 1.0)
            .build(openAI);

    // Input File
    var input = new File("output.wav"); // Supported file formats: flac, mp3, mp4, mpeg, mpga, m4a, wav, webm

    // Create Translation
    var response = service.create(input);

    // Print Translation
    IO.println(response.getText());
}