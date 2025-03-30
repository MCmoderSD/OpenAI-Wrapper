package de.MCmoderSD.openai.enums;

/**
 * Enum representing different levels of speed.
 */
@SuppressWarnings("unused")
public enum Speed {

    // Enum Values
    SLOWEST(1),
    SLOW(2),
    MEDIUM(3),
    FAST(4),
    FASTEST(5);

    // Attributes
    private final int speed;

    /**
     * Constructor for the Speed enum.
     *
     * @param speed the speed level
     */
    Speed(int speed) {
        this.speed = speed;
    }

    /**
     * Gets the speed level.
     *
     * @return the speed level
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the Speed enum value corresponding to the given speed level.
     *
     * @param speed the speed level
     * @return the corresponding Speed enum value
     * @throws IllegalArgumentException if the speed level is invalid
     */
    public static Speed getSpeed(int speed) {
        for (Speed s : Speed.values()) if (s.getSpeed() == speed) return s;
        throw new IllegalArgumentException("Invalid speed value: " + speed);
    }
}