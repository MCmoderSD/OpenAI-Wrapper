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