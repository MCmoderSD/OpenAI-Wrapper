package de.MCmoderSD.openai.helper;

import com.openai.models.ChatModel;
import com.openai.models.ReasoningEffort;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.chat.completions.ChatCompletionContentPart;
import com.openai.models.chat.completions.ChatCompletionContentPartImage;
import com.openai.models.chat.completions.ChatCompletionContentPartImage.ImageUrl;
import com.openai.models.images.ImageGenerateParams;
import com.openai.models.images.ImageModel;

import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;
import java.util.ArrayList;

@SuppressWarnings("SameReturnValue")
public class Helper {

    // Constants
    public static final int FILE_SIZE_LIMIT = 26214400; // 25MB = (25 * 1024 * 1024)
    public static final int IMAGE_SIZE_LIMIT = 20480000; // 20MB = (20 * 1024 * 1024)

    // Check Parameters
    public static boolean checkParameter(@Nullable Long maxTokens, @Nullable Double Temperature, @Nullable Double topP, @Nullable Double frequencyPenalty, @Nullable Double presencePenalty, @Nullable Long n, @Nullable ArrayList<String> images) {
        if (maxTokens != null && maxTokens <= 0) throw new InvalidParameterException("maxTokens must be greater than 0");                                                   // maxTokens
        if (Temperature != null && (Temperature < 0 || Temperature > 2))throw new InvalidParameterException("Temperature must be between 0 and 2");                         // Temperature
        if (topP != null && (topP < 0 || topP > 1)) throw new InvalidParameterException("topP must be between 0 and 1");                                                    // topP
        if (frequencyPenalty != null && (frequencyPenalty < 0 || frequencyPenalty > 2)) throw new InvalidParameterException("frequencyPenalty must be between 0 and 2");    // frequencyPenalty
        if (presencePenalty != null && (presencePenalty < 0 || presencePenalty > 2)) throw new InvalidParameterException("presencePenalty must be between 0 and 2");        // presencePenalty
        if (n != null && n <= 0) throw new InvalidParameterException("n must be greater than 0");                                                                           // n
        if (images != null && images.isEmpty()) throw new InvalidParameterException("Images must not be empty");                                                            // Images
        else if (images != null && !checkImages(images)) throw new InvalidParameterException("Images must be less than 20MB");                                              // Images
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

    public static boolean checkParameter(@Nullable Long dimensions, String prompt) {
        if (dimensions != null && (dimensions < 1 || dimensions > 3072)) throw new InvalidParameterException("Dimensions must be between 1 and 3072");  // Dimensions
        if (prompt.isBlank()) throw new InvalidParameterException("Prompt must not be empty");                                                          // Prompt
        return true;
    }

    // Check Files
    public static boolean checkImages(ArrayList<String> images) {
        for (String image : images) {
            if (image == null || image.isBlank()) throw new InvalidParameterException("Image must not be empty");
            else if (image.length() > IMAGE_SIZE_LIMIT) throw new InvalidParameterException("Image must be less than 20MB");
        }
        return true;
    }

    public static ChatCompletionContentPart addImage(String base64, @Nullable ImageUrl.Detail detail) {
        if (base64 == null || base64.isBlank()) throw new InvalidParameterException("Base64 must not be empty");
        var imageUrl = ImageUrl.builder().url(base64);
        if (detail != null) imageUrl.detail(detail);
        return ChatCompletionContentPart.ofImageUrl(ChatCompletionContentPartImage.builder().imageUrl(imageUrl.build()).build());
    }

    // Get Models
    public static ChatModel getChatModel(String model) {
        if (model == null || model.isBlank()) return null;
        try {
            return ChatModel.Companion.of(model);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static ImageModel getImageModel(String model) {
        if (model == null || model.isBlank()) return null;
        try {
            return ImageModel.Companion.of(model);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static ReasoningEffort getReasoningEffort(String reasoningEffort) {
        if (reasoningEffort == null || reasoningEffort.isBlank()) return null;
        try {
            return ReasoningEffort.Companion.of(reasoningEffort);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static SpeechCreateParams.Voice getVoice(String voice) {
        if (voice == null || voice.isBlank()) return null;
        try {
            return SpeechCreateParams.Voice.Companion.of(voice);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static SpeechCreateParams.ResponseFormat getResponseFormat(String format) {
        if (format == null || format.isBlank()) return null;
        try {
            return SpeechCreateParams.ResponseFormat.Companion.of(format);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static ImageGenerateParams.Size getSize(String size) {
        if (size == null || size.isBlank()) return null;
        try {
            return ImageGenerateParams.Size.Companion.of(size);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static ImageGenerateParams.Quality getQuality(String quality) {
        if (quality == null || quality.isBlank()) return null;
        try {
            return ImageGenerateParams.Quality.Companion.of(quality);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static ImageGenerateParams.Style getStyle(String style) {
        if (style == null || style.isBlank()) return null;
        try {
            return ImageGenerateParams.Style.Companion.of(style);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}