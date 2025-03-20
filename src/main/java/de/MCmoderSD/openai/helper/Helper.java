package de.MCmoderSD.openai.helper;

import com.openai.models.ChatModel;
import com.openai.models.audio.AudioModel;
import com.openai.models.audio.speech.SpeechCreateParams;
import com.openai.models.audio.speech.SpeechModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionMessage;

public class Helper {

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