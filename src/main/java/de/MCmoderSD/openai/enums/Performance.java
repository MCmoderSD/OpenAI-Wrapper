package de.MCmoderSD.openai.enums;

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

    // Constructor
    Performance(int performance) {
        this.performance = performance;
    }

    // Getter
    public int getPerformance() {
        return performance;
    }

    public static Performance getPerformance(int performance) {
        for (var p : Performance.values()) if (p.getPerformance() == performance) return p;
        throw new IllegalArgumentException("Invalid performance value: " + performance);
    }
}