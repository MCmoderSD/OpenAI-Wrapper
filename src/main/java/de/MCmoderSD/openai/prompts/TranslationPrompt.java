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
        var data = output.asVerbose();
        duration = data.duration();
        language = data.language();
        text = data.text();

        // Extract Segments
        segments = new ArrayList<>(data.segments().orElseThrow().size());
        data.segments().orElseThrow().forEach(segment -> segments.add(new Segment(segment)));
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