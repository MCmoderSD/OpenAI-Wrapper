package de.MCmoderSD.openai.enums;

/**
 * Enum representing different levels of performance.
 */
@SuppressWarnings("unused")
public enum Performance {

    // Enum Values
    LOW(1),
    AVERAGE(2),
    HIGH(3),
    HIGHER(4),
    HIGHEST(5);

    // Attributes
    private final int performance;

    /**
     * Constructor for Performance enum.
     *
     * @param performance The performance level.
     */
    Performance(int performance) {
        this.performance = performance;
    }

    /**
     * Gets the performance level.
     *
     * @return The performance level.
     */
    public int getPerformance() {
        return performance;
    }

    /**
     * Returns the Performance enum corresponding to the given performance value.
     *
     * @param performance The performance value.
     * @return The Performance enum.
     * @throws IllegalArgumentException If the performance value is invalid.
     */
    public static Performance getPerformance(int performance) {
        for (Performance p : Performance.values()) if (p.getPerformance() == performance) return p;
        throw new IllegalArgumentException("Invalid performance value: " + performance);
    }
}