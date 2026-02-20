package de.MCmoderSD.openai.enums;

public enum Voice {

    // Voices
    ALLOY, ASH, BALLAD, CORAL, ECHO, FABLE, NOVA, ONYX, SAGE, SHIMMER, VERSE, MARIN, CEDAR;

    // Getter
    public String getName() {
        return name().toLowerCase();
    }
}
