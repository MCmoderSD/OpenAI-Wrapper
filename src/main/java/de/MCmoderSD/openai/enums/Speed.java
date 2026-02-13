package de.MCmoderSD.openai.enums;

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

    // Constructor
    Speed(int speed) {
        this.speed = speed;
    }

    // Getter
    public int getSpeed() {
        return speed;
    }

    public static Speed getSpeed(int speed) {
        for (var s : Speed.values()) if (s.getSpeed() == speed) return s;
        throw new IllegalArgumentException("Invalid speed value: " + speed);
    }
}