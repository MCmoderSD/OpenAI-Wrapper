package de.MCmoderSD;

import com.fasterxml.jackson.databind.JsonNode;
import de.MCmoderSD.json.JsonUtility;
import de.MCmoderSD.openai.core.OpenAI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String userInput = "Where and how often does the letter E appear in the German word Heidelbeere";

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Load Config
        JsonNode config = JsonUtility.loadJson("/config.json", false);

        // Initialize OpenAI
        OpenAI openAI = new OpenAI(config);
    }

    private static void promptLoop(OpenAI openAI) {
        String input;

        System.out.println("Enter your prompt (type 'exit' to quit):\nYou: ");
        while (!(input = scanner.nextLine()).equalsIgnoreCase("exit")) {
            String response = openAI.prompt(input);
            System.out.println("Response: " + response);
            System.out.print("You: ");
        }

        System.out.println("Exiting prompt loop.");
    }

    private static void chatLoop(OpenAI openAI) {
        String input;

        System.out.println("Enter your prompt (type 'exit' to quit):\nYou: ");
        while (!(input = scanner.nextLine()).equalsIgnoreCase("exit")) {
            String response = openAI.prompt(1, input);
            System.out.println("Response: " + response);
            System.out.print("You: ");
        }

        System.out.println("Exiting prompt loop.");
    }
}