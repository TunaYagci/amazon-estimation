package com.tunayagci.amazon.estimation.service;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * An interface for retrieving search suggestions from Amazon
 */
public interface AmazonSuggestionService {

    /**
     * Retrieves an array of top 10 searched keywords on Amazon, by keyword
     * Uses Amazon's unlisted Search API for this purpose
     * {@code https://completion.amazon.com/search/complete}
     *
     * Service replaces "space" char with "+" char upon request.
     *
     * @param keyword the text to search
     * @return the List of top 10 keywords.
     * May return an empty list, or maximum of 10 elements.
     */
    @NonNull List<String> retrieveSuggestions(String keyword);
}
