package de.MCmoderSD.openai.enums;

/**
 * Enum representing different levels of reasoning.
 */
@SuppressWarnings("unused")
public enum Reasoning {

    // Enum Values
    LOW(1),
    AVERAGE(2),
    HIGH(3),
    HIGHER(4);

    // Attributes
    private final int reasoning;

    /**
     * Constructor for the Reasoning enum.
     *
     * @param reasoning the reasoning level
     */
    Reasoning(int reasoning) {
        this.reasoning = reasoning;
    }

    /**
     * Gets the reasoning level.
     *
     * @return the reasoning level
     */
    public int getReasoning() {
        return reasoning;
    }

    /**
     * Gets the Reasoning enum value corresponding to the given reasoning level.
     *
     * @param reasoning the reasoning level
     * @return the corresponding Reasoning enum value
     * @throws IllegalArgumentException if the reasoning level is invalid
     */
    public static Reasoning getReasoning(int reasoning) {
        for (Reasoning r : Reasoning.values()) if (r.getReasoning() == reasoning) return r;
        throw new IllegalArgumentException("Invalid reasoning value: " + reasoning);
    }
}