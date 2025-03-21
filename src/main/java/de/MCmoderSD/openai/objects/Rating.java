package de.MCmoderSD.openai.objects;

import com.openai.models.moderations.Moderation;

import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a content moderation rating that encapsulates the results of OpenAI's content
 * moderation analysis. It provides detailed information about different categories of
 * potentially harmful content and their confidence scores.
 */
@SuppressWarnings("unused")
public class Rating implements Serializable {

    // Type
    private final boolean flagged;

    // Flags
    private final Flag harassment;
    private final Flag harassmentThreatening;
    private final Flag hate;
    private final Flag hateThreatening;
    private final Flag illicit;
    private final Flag illicitViolent;
    private final Flag selfHarm;
    private final Flag selfHarmInstructions;
    private final Flag selfHarmIntent;
    private final Flag sexual;
    private final Flag sexualMinors;
    private final Flag violence;
    private final Flag violenceGraphic;

    /**
     * Constructs a Rating object from a Moderation response.
     *
     * @param moderation The moderation result from which to extract rating data
     */
    public Rating(Moderation moderation) {

        // Extract Data
        var flags = moderation.categories();
        var scores = moderation.categoryScores();

        // Set Type
        flagged = moderation.flagged();

        // Set Flags
        harassment = new Flag(flags.harassment(), scores.harassment());
        harassmentThreatening = new Flag(flags.harassmentThreatening(), scores.harassmentThreatening());
        hate = new Flag(flags.hate(), scores.hate());
        hateThreatening = new Flag(flags.hateThreatening(), scores.hateThreatening());
        illicit = new Flag(flags.illicit().orElse(false), flags.illicit().isPresent() ? scores.illicit() : 0d);
        illicitViolent = new Flag(flags.illicitViolent().orElse(false), flags.illicitViolent().isPresent() ? scores.illicitViolent() : 0d);
        selfHarm = new Flag(flags.selfHarm(), scores.selfHarm());
        selfHarmInstructions = new Flag(flags.selfHarmInstructions(), scores.selfHarmInstructions());
        selfHarmIntent = new Flag(flags.selfHarmIntent(), scores.selfHarmIntent());
        sexual = new Flag(flags.sexual(), scores.sexual());
        sexualMinors = new Flag(flags.sexualMinors(), scores.sexualMinors());
        violence = new Flag(flags.violence(), scores.violence());
        violenceGraphic = new Flag(flags.violenceGraphic(), scores.violenceGraphic());
    }

    /**
     * Represents a content policy flag with its violation status and confidence score.
     */
    public record Flag(boolean flagged, double score) implements Serializable {

        /**
         * Converts the confidence score to a percentage representation.
         *
         * @param scale The number of decimal places to round to
         * @return The score as a percentage with specified precision
         */
        public BigDecimal asPercentage(int scale) {
            return BigDecimal.valueOf(score).movePointRight(2).setScale(scale, RoundingMode.HALF_UP);
        }
    }

    /**
     * Determines if a flag should be included in the output based on the requested data scope.
     *
     * @param flag The flag to check
     * @param data The data scope criteria
     * @return true if the flag should be included, false otherwise
     */
    private static boolean inScope(Flag flag, Data data) {
        return data.equals(Data.ALL) || (data.equals(Data.POSITIVE) && flag.flagged) || data.equals(Data.NEGATIVE);
    }

    /**
     * Returns whether the content was flagged for any violation.
     *
     * @return true if the content was flagged, false otherwise
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Gets the harassment flag details.
     *
     * @return The harassment Flag object
     */
    public Flag getHarassment() {
        return harassment;
    }

    /**
     * Gets the threatening harassment flag details.
     *
     * @return The threatening harassment Flag object
     */
    public Flag getHarassmentThreatening() {
        return harassmentThreatening;
    }

    /**
     * Gets the hate content flag details.
     *
     * @return The hate Flag object
     */
    public Flag getHate() {
        return hate;
    }

    /**
     * Gets the threatening hate content flag details.
     *
     * @return The threatening hate Flag object
     */
    public Flag getHateThreatening() {
        return hateThreatening;
    }

    /**
     * Gets the illicit content flag details.
     *
     * @return The illicit Flag object
     */
    public Flag getIllicit() {
        return illicit;
    }

    /**
     * Gets the violent illicit content flag details.
     *
     * @return The violent illicit Flag object
     */
    public Flag getIllicitViolent() {
        return illicitViolent;
    }

    /**
     * Gets the self-harm content flag details.
     *
     * @return The self-harm Flag object
     */
    public Flag getSelfHarm() {
        return selfHarm;
    }

    /**
     * Gets the self-harm instructions content flag details.
     *
     * @return The self-harm instructions Flag object
     */
    public Flag getSelfHarmInstructions() {
        return selfHarmInstructions;
    }

    /**
     * Gets the self-harm intent content flag details.
     *
     * @return The self-harm intent Flag object
     */
    public Flag getSelfHarmIntent() {
        return selfHarmIntent;
    }

    /**
     * Gets the sexual content flag details.
     *
     * @return The sexual content Flag object
     */
    public Flag getSexual() {
        return sexual;
    }

    /**
     * Gets the sexual content involving minors flag details.
     *
     * @return The sexual content involving minors Flag object
     */
    public Flag getSexualMinors() {
        return sexualMinors;
    }

    /**
     * Gets the violence content flag details.
     *
     * @return The violence Flag object
     */
    public Flag getViolence() {
        return violence;
    }

    /**
     * Gets the graphic violence content flag details.
     *
     * @return The graphic violence Flag object
     */
    public Flag getViolenceGraphic() {
        return violenceGraphic;
    }

    /**
     * Generates a formatted string representation of the rating data based on specified scope.
     *
     * @param data The scope of data to include (ALL, POSITIVE, or NEGATIVE)
     * @return A formatted string containing the requested rating information
     */
    public String getData(Data data) {
        StringBuilder output = new StringBuilder();
        if (inScope(harassment, data)) output.append("Harassment: ").append(harassment.asPercentage(2)).append("%\n");
        if (inScope(harassmentThreatening, data)) output.append("Harassment Threatening: ").append(harassmentThreatening.asPercentage(2)).append("%\n");
        if (inScope(hate, data)) output.append("Hate: ").append(hate.asPercentage(2)).append("%\n");
        if (inScope(hateThreatening, data)) output.append("Hate Threatening: ").append(hateThreatening.asPercentage(2)).append("%\n");
        if (inScope(illicit, data)) output.append("Illicit: ").append(illicit.asPercentage(2)).append("%\n");
        if (inScope(illicitViolent, data)) output.append("Illicit Violent: ").append(illicitViolent.asPercentage(2)).append("%\n");
        if (inScope(selfHarm, data)) output.append("Self Harm: ").append(selfHarm.asPercentage(2)).append("%\n");
        if (inScope(selfHarmInstructions, data)) output.append("Self Harm Instructions: ").append(selfHarmInstructions.asPercentage(2)).append("%\n");
        if (inScope(selfHarmIntent, data)) output.append("Self Harm Intent: ").append(selfHarmIntent.asPercentage(2)).append("%\n");
        if (inScope(sexual, data)) output.append("Sexual: ").append(sexual.asPercentage(2)).append("%\n");
        if (inScope(sexualMinors, data)) output.append("Sexual Minors: ").append(sexualMinors.asPercentage(2)).append("%\n");
        if (inScope(violence, data)) output.append("Violence: ").append(violence.asPercentage(2)).append("%\n");
        if (inScope(violenceGraphic, data)) output.append("Violence Graphic: ").append(violenceGraphic.asPercentage(2)).append("%\n");
        return output.toString();
    }

    /**
     * Defines the scope of data to be included in rating reports.
     */
    public enum Data {
        ALL, POSITIVE, NEGATIVE
    }

    /**
     * Serializes this Rating instance to a byte array.
     *
     * @return The serialized Rating as a byte array
     * @throws IOException If an I/O error occurs during serialization
     */
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(data);
        stream.writeObject(this);
        stream.flush();
        return data.toByteArray();
    }

    /**
     * Deserializes a Rating instance from a byte array.
     *
     * @param bytes The byte array containing the serialized Rating
     * @return The deserialized Rating instance
     * @throws IOException If an I/O error occurs during deserialization
     * @throws ClassNotFoundException If the class of the serialized object cannot be found
     */
    public static Rating fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        return (Rating) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
    }
}