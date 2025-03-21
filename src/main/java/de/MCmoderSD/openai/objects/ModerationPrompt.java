package de.MCmoderSD.openai.objects;

import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateResponse;
import com.openai.models.moderations.ModerationModel;
import de.MCmoderSD.openai.helper.Helper;

import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a moderation request and response from OpenAI's content moderation API.
 * This class encapsulates both the input text to be moderated and the resulting
 * moderation data, including content policy violation assessments.
 */
@SuppressWarnings("unused")
public class ModerationPrompt implements Serializable {

    // Parameters
    private final String input;
    private final ModerationCreateResponse output;

    // Data
    private final String id;
    private final ModerationModel model;

    // Content
    private final ArrayList<Moderation> moderations;
    private final ArrayList<Rating> ratings;

    /**
     * Constructs a new ModerationPrompt with the provided input text and moderation response.
     *
     * @param input  The text content submitted for moderation
     * @param output The response received from OpenAI's moderation API
     */
    public ModerationPrompt(String input, ModerationCreateResponse output) {

        // Initialize Parameters
        this.input = input;
        this.output = output;

        // Extract Data
        id = output.id();
        model = Helper.getModerationModel(output.model());

        // Extract Content
        moderations = new ArrayList<>(output.results());
        ratings = new ArrayList<>();
        moderations.forEach(moderation -> ratings.add(new Rating(moderation)));
    }

    /**
     * Gets the original text input that was submitted for moderation.
     *
     * @return The input text
     */
    public String getInput() {
        return input;
    }

    /**
     * Gets the raw moderation response from the API.
     *
     * @return The full moderation API response
     */
    public ModerationCreateResponse getOutput() {
        return output;
    }

    /**
     * Gets the unique identifier for this moderation request.
     *
     * @return The request ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the moderation model used for this content analysis.
     *
     * @return The moderation model
     */
    public ModerationModel getModel() {
        return model;
    }

    /**
     * Gets the list of moderation results for the input content.
     *
     * @return List of moderation results
     */
    public ArrayList<Moderation> getModerations() {
        return moderations;
    }

    /**
     * Gets the list of enhanced rating objects created from the moderation results.
     *
     * @return List of rating objects with additional functionality
     */
    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    /**
     * Serializes this ModerationPrompt instance to a byte array.
     *
     * @return A byte array containing the serialized ModerationPrompt
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
     * Deserializes a ModerationPrompt instance from a byte array.
     *
     * @param bytes The byte array containing the serialized ModerationPrompt
     * @return The deserialized ModerationPrompt instance
     * @throws IOException            If an I/O error occurs during deserialization
     * @throws ClassNotFoundException If the class of the serialized object cannot be found
     */
    public static ModerationPrompt fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        return (ModerationPrompt) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
    }
}