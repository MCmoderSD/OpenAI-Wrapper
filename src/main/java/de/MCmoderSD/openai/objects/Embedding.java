package de.MCmoderSD.openai.objects;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

@SuppressWarnings("unused")
public class Embedding implements Serializable {

    // Attributes
    private final int dimension;
    private final float[] vector;

    // Variables
    private final double magnitude;
    private final float[] unit;

    public Embedding(byte[] bytes) {
        this(Embedding.fromBytes(bytes).getVector());
    }

    public Embedding(float[] vector) {

        // Initialize Attributes
        this.vector = vector;
        this.dimension = vector.length;

        // Compute Magnitude
        magnitude = magnitude(this);

        // Compute Unit Vector
        unit = normalize(this);
    }

    public Embedding(List<Double> data) {

        // Initialize Attributes
        dimension = data.size();
        vector = new float[dimension];

        // Convert Data
        for (var i = 0; i < dimension; i++) {
            var d = data.get(i);
            var f = d.floatValue();
            if (d.equals((double) f)) vector[i] = f;
            else throw new ArithmeticException("Precision loss detected during float conversion: " + d);
        }

        // Compute Magnitude
        magnitude = magnitude(this);

        // Compute Unit Vector
        unit = normalize(this);
    }

    public void checkDimensions(Embedding... vector) {
        for (var v : vector)
            if (dimension != v.getDimension()) throw new IllegalArgumentException("Vector dimensions do not match.");
    }

    public static double magnitude(Embedding vector) {
        var sum = 0d;
        for (var v : vector.getVector()) sum += Math.pow(v, 2);
        return Math.sqrt(sum);
    }

    public static float[] normalize(Embedding vector) {
        if (vector.getMagnitude() == 0) throw new ArithmeticException("Cannot normalize a zero vector.");
        var unit = new float[vector.getDimension()];
        for (var i = 0; i < vector.getDimension(); i++) unit[i] = vector.getVector()[i] / (float) vector.getMagnitude();
        return unit;
    }

    public double dotProduct(Embedding vector) {
        checkDimensions(vector);
        var result = 0d;
        for (var i = 0; i < dimension; i++) result += this.vector[i] * vector.getVector()[i];
        return result;
    }


    public double manhattanDistance(Embedding vector) {
        checkDimensions(vector);
        var sum = 0d;
        for (var i = 0; i < dimension; i++) sum += Math.abs(this.vector[i] - vector.getVector()[i]);
        return sum;
    }

    public double euclideanDistance(Embedding vector) {
        checkDimensions(vector);
        var sum = 0d;
        for (var i = 0; i < dimension; i++) {
            var diff = this.vector[i] - vector.getVector()[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    public Embedding add(Embedding vector) {
        checkDimensions(vector);
        var result = new float[dimension];
        for (var i = 0; i < dimension; i++) result[i] = this.vector[i] + vector.getVector()[i];
        return new Embedding(result);
    }

    public Embedding subtract(Embedding vector) {
        checkDimensions(vector);
        var result = new float[dimension];
        for (var i = 0; i < dimension; i++) result[i] = this.vector[i] - vector.getVector()[i];
        return new Embedding(result);
    }

    public Embedding multiply(float scalar) {
        var result = new float[dimension];
        for (var i = 0; i < dimension; i++) result[i] = this.vector[i] * scalar;
        return new Embedding(result);
    }

    public double cosineSimilarity(Embedding vector) {
        checkDimensions(vector);
        double magnitudeProduct = magnitude * magnitude(vector);
        return magnitudeProduct == 0d ? 0d : dotProduct(vector) / magnitudeProduct;
    }

    public double cosineDistance(Embedding vector) {
        return 1 - cosineSimilarity(vector);
    }

    public double angleBetween(Embedding vector) {
        checkDimensions(vector);
        return Math.acos(Math.max(-1, Math.min(1, cosineSimilarity(vector))));
    }

    public double pearsonCorrelation(Embedding vector) {
        checkDimensions(vector);

        var meanA = 0d;
        var meanB = 0d;

        for (var i = 0; i < dimension; i++) {
            meanA += this.vector[i];
            meanB += vector.getVector()[i];
        }

        meanA /= dimension;
        meanB /= dimension;

        var numerator = 0d;
        var denominatorA = 0d;
        var denominatorB = 0d;

        for (var i = 0; i < dimension; i++) {
            var diffA = this.vector[i] - meanA;
            var diffB = vector.getVector()[i] - meanB;

            numerator += diffA * diffB;
            denominatorA += diffA * diffA;
            denominatorB += diffB * diffB;
        }

        return (denominatorA == 0 || denominatorB == 0) ? 0 : numerator / Math.sqrt(denominatorA * denominatorB);
    }

    public double jensenShannonDivergence(Embedding vector) {
        checkDimensions(vector);

        var sum = 0d;
        for (var i = 0; i < dimension; i++) {
            var a = this.vector[i];
            var b = vector.getVector()[i];
            var m = (a + b) / 2d;
            sum += a * Math.log(a / m) + b * Math.log(b / m);
        }

        return sum / 2d;
    }

    public double meanSquaredError(Embedding vector) {
        checkDimensions(vector);

        var sum = 0d;
        for (var i = 0; i < dimension; i++) {
            var diff = this.vector[i] - vector.getVector()[i];
            sum += diff * diff;
        }

        return sum / dimension;
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

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(dimension * Float.BYTES);
        for (var v : vector) buffer.putFloat(v);
        return buffer.array();
    }

    public static Embedding fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        var values = new float[bytes.length / Float.BYTES];
        for (var i = 0; i < values.length; i++) values[i] = buffer.getFloat();
        return new Embedding(values);
    }
}