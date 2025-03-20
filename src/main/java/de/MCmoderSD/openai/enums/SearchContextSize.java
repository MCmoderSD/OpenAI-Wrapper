package de.MCmoderSD.openai.enums;

import com.openai.models.chat.completions.ChatCompletionCreateParams;

/**
 * Enum representing different search context sizes for web search options.
 */
@SuppressWarnings("unused")
public enum SearchContextSize {

    // Enum Values
    LOW(ChatCompletionCreateParams.WebSearchOptions.SearchContextSize.LOW),
    MEDIUM(ChatCompletionCreateParams.WebSearchOptions.SearchContextSize.MEDIUM),
    HIGH(ChatCompletionCreateParams.WebSearchOptions.SearchContextSize.HIGH);

    // Attributes
    private final ChatCompletionCreateParams.WebSearchOptions.SearchContextSize searchContextSize;

    /**
     * Constructor for SearchContextSize enum.
     *
     * @param searchContextSize The search context size.
     */
    SearchContextSize(ChatCompletionCreateParams.WebSearchOptions.SearchContextSize searchContextSize) {
        this.searchContextSize = searchContextSize;
    }

    /**
     * Returns the search context size.
     *
     * @return The search context size.
     */
    public ChatCompletionCreateParams.WebSearchOptions.SearchContextSize size() {
        return searchContextSize;
    }
}