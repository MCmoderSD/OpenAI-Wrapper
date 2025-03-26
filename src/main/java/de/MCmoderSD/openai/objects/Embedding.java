package de.MCmoderSD.openai.objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Represents an embedding vector with various utility methods for vector operations.
 */
@SuppressWarnings("unused")
public class Embedding implements Serializable {

    // Attributes
    private final int dimension;
    private final float[] vector;

    // Variables
    private final double magnitude;
    private final float[] unit;

    /**
     * Constructs an Embedding from a byte array.
     *
     * @param bytes the byte array representing the embedding vector
     */
    public Embedding(byte[] bytes) {
        this(Embedding.fromBytes(bytes).getVector());
    }

    /**
     * Constructs an Embedding from a float array.
     *
     * @param vector the float array representing the embedding vector
     */
    public Embedding(float[] vector) {

        // Initialize Attributes
        this.vector = vector;
        this.dimension = vector.length;

        // Compute Magnitude
        magnitude = magnitude(this);

        // Compute Unit Vector
        unit = normalize(this);
    }

    /**
     * Constructs an Embedding from a list of doubles.
     *
     * @param data the list of doubles representing the embedding vector
     */
    public Embedding(List<Double> data) {

        // Initialize Attributes
        dimension = data.size();
        vector = new float[dimension];

        // Convert Data
        for (var i = 0; i < dimension; i++) {
            var d = BigDecimal.valueOf(data.get(i));
            var f = BigDecimal.valueOf(d.floatValue()).setScale(d.scale(), RoundingMode.HALF_UP);
            if (d.equals(f)) vector[i] = f.floatValue();
            else throw new ArithmeticException("Precision loss detected during float conversion: " + d);
        }

        // Compute Magnitude
        magnitude = magnitude(this);

        // Compute Unit Vector
        unit = normalize(this);
    }

    /**
     * Checks if the dimensions of the given embeddings match the current embedding.
     *
     * @param vector the embeddings to check
     */
    public void checkDimensions(Embedding... vector) {
        for (var v : vector)
            if (dimension != v.getDimension()) throw new IllegalArgumentException("Vector dimensions do not match.");
    }

    /**
     * Computes the magnitude of the given embedding.
     *
     * @param vector the embedding to compute the magnitude for
     * @return the magnitude of the embedding
     */
    public static double magnitude(Embedding vector) {
        var sum = 0d;
        for (var v : vector.getVector()) sum += Math.pow(v, 2);
        return Math.sqrt(sum);
    }

    /**
     * Normalizes the given embedding to a unit vector.
     *
     * @param vector the embedding to normalize
     * @return the unit vector
     */
    public static float[] normalize(Embedding vector) {
        if (vector.getMagnitude() == 0) throw new ArithmeticException("Cannot normalize a zero vector.");
        var unit = new float[vector.getDimension()];
        for (var i = 0; i < vector.getDimension(); i++) unit[i] = vector.getVector()[i] / (float) vector.getMagnitude();
        return unit;
    }

    /**
     * Computes the dot product of the current embedding with another embedding.
     *
     * @param vector the other embedding
     * @return the dot product
     */
    public double dotProduct(Embedding vector) {
        checkDimensions(vector);
        var result = 0d;
        for (var i = 0; i < dimension; i++) result += this.vector[i] * vector.getVector()[i];
        return result;
    }

    /**
     * Computes the Manhattan distance between the current embedding and another embedding.
     *
     * @param vector the other embedding
     * @return the Manhattan distance
     */
    public double manhattanDistance(Embedding vector) {
        checkDimensions(vector);
        var sum = 0d;
        for (var i = 0; i < dimension; i++) sum += Math.abs(this.vector[i] - vector.getVector()[i]);
        return sum;
    }

    /**
     * Computes the Euclidean distance between the current embedding and another embedding.
     *
     * @param vector the other embedding
     * @return the Euclidean distance
     */
    public double euclideanDistance(Embedding vector) {
        checkDimensions(vector);
        var sum = 0d;
        for (var i = 0; i < dimension; i++) {
            var diff = this.vector[i] - vector.getVector()[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    /**
     * Adds another embedding to the current embedding.
     *
     * @param vector the other embedding
     * @return the resulting embedding
     */
    public Embedding add(Embedding vector) {
        checkDimensions(vector);
        var result = new float[dimension];
        for (var i = 0; i < dimension; i++) result[i] = this.vector[i] + vector.getVector()[i];
        return new Embedding(result);
    }

    /**
     * Subtracts another embedding from the current embedding.
     *
     * @param vector the other embedding
     * @return the resulting embedding
     */
    public Embedding subtract(Embedding vector) {
        checkDimensions(vector);
        var result = new float[dimension];
        for (var i = 0; i < dimension; i++) result[i] = this.vector[i] - vector.getVector()[i];
        return new Embedding(result);
    }

    /**
     * Multiplies the current embedding by a scalar.
     *
     * @param scalar the scalar value
     * @return the resulting embedding
     */
    public Embedding multiply(float scalar) {
        var result = new float[dimension];
        for (var i = 0; i < dimension; i++) result[i] = this.vector[i] * scalar;
        return new Embedding(result);
    }

    /**
     * Computes the cosine similarity between the current embedding and another embedding.
     *
     * @param vector the other embedding
     * @return the cosine similarity
     */
    public double cosineSimilarity(Embedding vector) {
        checkDimensions(vector);
        double magnitudeProduct = magnitude * magnitude(vector);
        return magnitudeProduct == 0d ? 0d : dotProduct(vector) / magnitudeProduct;
    }

    /**
     * Computes the cosine distance between the current embedding and another embedding.
     *
     * @param vector the other embedding
     * @return the cosine distance
     */
    public double cosineDistance(Embedding vector) {
        return 1 - cosineSimilarity(vector);
    }

    /**
     * Computes the angle between the current embedding and another embedding.
     *
     * @param vector the other embedding
     * @return the angle in radians
     */
    public double angleBetween(Embedding vector) {
        checkDimensions(vector);
        return Math.acos(Math.max(-1, Math.min(1, cosineSimilarity(vector))));
    }

    /**
     * Computes the Pearson correlation coefficient between the current embedding and another embedding.
     *
     * @param vector the other embedding
     * @return the Pearson correlation coefficient
     */
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

    /**
     * Computes the Jensen-Shannon divergence between the current embedding and another embedding.
     *
     * @param vector the other embedding
     * @return the Jensen-Shannon divergence
     */
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

    /**
     * Computes the mean squared error between the current embedding and another embedding.
     *
     * @param vector the other embedding
     * @return the mean squared error
     */
    public double meanSquaredError(Embedding vector) {
        checkDimensions(vector);

        var sum = 0d;
        for (var i = 0; i < dimension; i++) {
            var diff = this.vector[i] - vector.getVector()[i];
            sum += diff * diff;
        }

        return sum / dimension;
    }

    /**
     * Gets the dimension of the embedding.
     *
     * @return the dimension
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Gets the vector of the embedding.
     *
     * @return the vector
     */
    public float[] getVector() {
        return vector;
    }

    /**
     * Gets the magnitude of the embedding.
     *
     * @return the magnitude
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * Gets the unit vector of the embedding.
     *
     * @return the unit vector
     */
    public float[] getUnit() {
        return unit;
    }

    /**
     * Converts the embedding to a byte array.
     *
     * @return the byte array representation of the embedding
     */
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(dimension * Float.BYTES);
        for (var v : vector) buffer.putFloat(v);
        return buffer.array();
    }

    /**
     * Constructs an Embedding from a byte array.
     *
     * @param bytes the byte array
     * @return the constructed Embedding
     */
    public static Embedding fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        var values = new float[bytes.length / Float.BYTES];
        for (var i = 0; i < values.length; i++) values[i] = buffer.getFloat();
        return new Embedding(values);
    }
}