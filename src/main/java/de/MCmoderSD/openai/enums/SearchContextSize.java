package de.MCmoderSD.openai.enums;

import com.openai.models.chat.completions.ChatCompletionCreateParams;

@SuppressWarnings("unused")
public enum SearchContextSize {

    // Enum Values
    LOW(ChatCompletionCreateParams.WebSearchOptions.SearchContextSize.LOW),
    MEDIUM(ChatCompletionCreateParams.WebSearchOptions.SearchContextSize.MEDIUM),
    HIGH(ChatCompletionCreateParams.WebSearchOptions.SearchContextSize.HIGH);

    // Attributes
    private final ChatCompletionCreateParams.WebSearchOptions.SearchContextSize searchContextSize;

    // Constructor
    SearchContextSize(ChatCompletionCreateParams.WebSearchOptions.SearchContextSize searchContextSize) {
        this.searchContextSize = searchContextSize;
    }

    // Getter
    public ChatCompletionCreateParams.WebSearchOptions.SearchContextSize size() {
        return searchContextSize;
    }
}