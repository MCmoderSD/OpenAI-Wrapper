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

    /**
     * Returns the search context size based on the provided string.
     *
     * @param string The string representation of the search context size.
     * @return The corresponding SearchContextSize enum value, or null if not found.
     */
    public static SearchContextSize getSearchContextSize(String string) {
        if (string == null || string.isBlank()) return null;
        for (SearchContextSize size : SearchContextSize.values()) if (size.name().equalsIgnoreCase(string)) return size;
        return null;
    }
}