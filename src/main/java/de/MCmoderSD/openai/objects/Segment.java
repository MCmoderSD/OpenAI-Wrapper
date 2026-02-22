package de.MCmoderSD.openai.objects;

import com.openai.models.audio.transcriptions.TranscriptionSegment;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public class Segment implements Serializable {

    // Time
    private final double start;
    private final double end;

    // Content
    private final String text;

    // Metadata
    private final long id;
    private final long[] tokens;
    private final float temperature;

    // Constructor
    public Segment(TranscriptionSegment segment) {

        // Check Segment
        if (segment == null) throw new IllegalArgumentException("Segment must not be null");

        // Initialize Time
        start = segment.start();
        end = segment.end();

        // Initialize Content
        text = segment.text();

        // Initialize Metadata
        id = segment.id();
        temperature = segment.temperature();
        tokens = segment.tokens().stream().mapToLong(Long::longValue).toArray();
    }

    // Getters
    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public String getText() {
        return text;
    }

    public long getId() {
        return id;
    }

    public long[] getTokens() {
        return tokens;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, text, id, Arrays.hashCode(tokens), temperature);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && hashCode() == obj.hashCode();
    }
}