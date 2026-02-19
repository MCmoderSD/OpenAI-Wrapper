package de.MCmoderSD.openai.objects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Embedding implements Serializable {

    // Attributes
    private final int dimension;
    private final float[] vector;

    // Variables
    private final double magnitude;
    private final float[] unit;

    // Constructor
    public Embedding(float[] vector) {

        // Validate Input
        if (vector == null || vector.length == 0) throw new IllegalArgumentException("Vector cannot be null or empty.");

        // Initialize Attributes
        this.vector = vector;
        this.dimension = vector.length;

        // Compute Magnitude
        magnitude = magnitude(this);

        // Compute Unit Vector
        unit = normalize(this);
    }

    // Helper Methods
    public static float[] convertToFloatArray(List<Float> data) {
        float[] array = new float[data.size()];
        for (var i = 0; i < data.size(); i++) array[i] = data.get(i);
        return array;
    }

    public static double magnitude(Embedding vector) {
        if (vector == null) throw new IllegalArgumentException("Vector cannot be null.");
        var sum = 0d;
        for (var v : vector.getVector()) sum += Math.pow(v, 2);
        return Math.sqrt(sum);
    }

    public static float[] normalize(Embedding vector) {
        if (vector == null) throw new IllegalArgumentException("Vector cannot be null.");
        if (vector.getMagnitude() == 0) throw new ArithmeticException("Cannot normalize a zero vector.");
        var unit = new float[vector.getDimension()];
        for (var i = 0; i < vector.getDimension(); i++) unit[i] = vector.getVector()[i] / (float) vector.getMagnitude();
        return unit;
    }

    // Getter
    public int getDimension() {
        return dimension;
    }

    public float[] getVector() {
        return vector;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public float[] getUnit() {
        return unit;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vector);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && hashCode() == obj.hashCode();
    }
}