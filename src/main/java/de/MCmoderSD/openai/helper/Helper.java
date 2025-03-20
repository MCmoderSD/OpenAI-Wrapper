package de.MCmoderSD.openai.helper;

import com.openai.models.ChatModel;
import com.openai.models.audio.AudioModel;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.audio.speech.SpeechModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessage;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;

public class Helper {

    // Constants
    public static final int FILE_SIZE_LIMIT = 25 * 1024 * 1024; // 25MB


    // Methods
    public static boolean checkParameter(@Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n) {
        if (maxTokens != null && maxTokens <= 0) throw new InvalidParameterException("maxTokens must be greater than 0");                                                   // maxTokens
        if (Temperature != null && (Temperature < 0 || Temperature > 2))throw new InvalidParameterException("Temperature must be between 0 and 2");                         // Temperature
        if (topP != null && (topP < 0 || topP > 1)) throw new InvalidParameterException("topP must be between 0 and 1");                                                    // topP
        if (frequencyPenalty != null && (frequencyPenalty < 0 || frequencyPenalty > 2)) throw new InvalidParameterException("frequencyPenalty must be between 0 and 2");    // frequencyPenalty
        if (presencePenalty != null && (presencePenalty < 0 || presencePenalty > 2)) throw new InvalidParameterException("presencePenalty must be between 0 and 2");        // presencePenalty
        if (n != null && n <= 0) throw new InvalidParameterException("n must be greater than 0");                                                                           // n
        return true;
    }

    public static boolean checkParameter(@Nullable Double temperature, byte[] data) {
        if (temperature != null && (temperature < 0 || temperature > 2)) throw new InvalidParameterException("Temperature must be between 0 and 2");    // Temperature
        if (data.length < 4) throw new InvalidParameterException("Data must be at least 4 bytes");                                                      // Input
        return true;
    }

    public static boolean checkParameter(@Nullable Double speed, String input)  {
        if (speed != null && (speed < 0.25 || speed > 4)) throw new InvalidParameterException("Speed must be between 0.25 and 4");  // Speed
        if (input.isBlank()) throw new InvalidParameterException("Input must not be empty");                                        // Input
        return true;
    }

    public static ChatCompletionMessage getMessage(ChatCompletion completion) {
        if (completion.choices().isEmpty()) return null;
        else return completion.choices().getFirst().message();
    }

    public static String getContent(ChatCompletionMessage message) {
        if (message == null) return null;
        StringBuilder content = new StringBuilder();
        message.content().ifPresent(content::append);
        if (content.isEmpty()) return null;
        else return content.toString();
    }

    public static ChatModel getChatModel(String model) {
        if (model == null || model.isBlank()) return null;
        else {
            try {
                return ChatModel.Companion.of(model);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public static AudioModel getAudioModel(String model) {
        if (model == null || model.isBlank()) return null;
        else {
            try {
                return AudioModel.Companion.of(model);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public static SpeechModel getSpeechModel(String model) {
        if (model == null || model.isBlank()) return null;
        else {
            try {
                return SpeechModel.Companion.of(model);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public static SpeechCreateParams.Voice getVoice(String voice) {
        if (voice == null || voice.isBlank()) return null;
        else {
            try {
                return SpeechCreateParams.Voice.Companion.of(voice);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public static SpeechCreateParams.ResponseFormat getResponseFormat(String format) {
        if (format == null || format.isBlank()) return null;
        else {
            try {
                return SpeechCreateParams.ResponseFormat.Companion.of(format);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}