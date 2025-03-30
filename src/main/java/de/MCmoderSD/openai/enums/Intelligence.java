package de.MCmoderSD.openai.enums;

/**
 * Enum representing different levels of intelligence.
 */
@SuppressWarnings("unused")
public enum Intelligence {

    // Enum Values
    LOW(1),
    AVERAGE(2),
    HIGH(3),
    HIGHER(4);

    // Attributes
    private final int intelligence;

    /**
     * Constructor for the Intelligence enum.
     *
     * @param intelligence the intelligence level
     */
    Intelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    /**
     * Gets the intelligence level.
     *
     * @return the intelligence level
     */
    public int getIntelligence() {
        return intelligence;
    }

    /**
     * Gets the Intelligence enum value corresponding to the given intelligence level.
     *
     * @param intelligence the intelligence level
     * @return the corresponding Intelligence enum value
     * @throws IllegalArgumentException if the intelligence level is invalid
     */
    public static Intelligence getIntelligence(int intelligence) {
        for (Intelligence i : Intelligence.values()) if (i.getIntelligence() == intelligence) return i;
        throw new IllegalArgumentException("Invalid intelligence value: " + intelligence);
    }
}