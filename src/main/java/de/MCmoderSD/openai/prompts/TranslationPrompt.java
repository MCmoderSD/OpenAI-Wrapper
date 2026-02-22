package de.MCmoderSD.openai.prompts;

import com.openai.models.audio.translations.TranslationCreateParams;
import com.openai.models.audio.translations.TranslationCreateResponse;
import de.MCmoderSD.openai.objects.Segment;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class TranslationPrompt {

    // Parameters
    private final TranslationCreateParams input;
    private final TranslationCreateResponse output;

    // Content
    private final double duration;
    private final String language;
    private final String text;

    // Segments
    private final ArrayList<Segment> segments;

    // Constructor
    public TranslationPrompt(TranslationCreateParams input, TranslationCreateResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Content
        var translation = output.asVerbose();
        duration = translation.duration();
        language = translation.language();
        text = translation.text();

        // Extract Segments
        segments = new ArrayList<>(translation.segments().orElseThrow().size());
        for (var segment : translation.segments().orElseThrow()) segments.add(new Segment(segment));
    }

    // Getters
    public TranslationCreateParams getInput() {
        return input;
    }

    public TranslationCreateResponse getOutput() {
        return output;
    }

    public double getDuration() {
        return duration;
    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }
}