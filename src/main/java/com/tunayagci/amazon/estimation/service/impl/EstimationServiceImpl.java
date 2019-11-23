package com.tunayagci.amazon.estimation.service.impl;

import com.tunayagci.amazon.estimation.generic.EstimateDTO;
import com.tunayagci.amazon.estimation.service.AmazonSuggestionService;
import com.tunayagci.amazon.estimation.service.EstimationService;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstimationServiceImpl implements EstimationService {

    private static final int MAX_REQUEST_PER_KEYWORD = 10;

    private AmazonSuggestionService amazonSuggestionService;

    public EstimationServiceImpl(AmazonSuggestionService amazonSuggestionService) {
        this.amazonSuggestionService = amazonSuggestionService;
    }

    public EstimateDTO estimate(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("Search keyword cannot be null");
        } else if (keyword.contains("*")) {
            throw new IllegalArgumentException("Search keyword cannot contain star(*) char");
        } else if (keyword.length() < 2) {
            throw new IllegalArgumentException("Keyword cannot be shorter than 2 letters");
        }

        KeywordLookup lookup = binarySearch(keyword);
        final int percentage = getPercentageByLetterIndex(lookup);
        return new EstimateDTO(keyword, percentage);
    }

    private KeywordLookup binarySearch(String keyword) {
        int index;
        int length = keyword.length();
        index = length / 2;
        Integer maxIndex = null;
        Integer minIndex = 0;
        int requestCount = 0;
        boolean isExist = false;
        int bestIndex = Integer.MAX_VALUE;

        while ((maxIndex == null || index != maxIndex) && requestCount++ < MAX_REQUEST_PER_KEYWORD) {
            final String keywordChunk = keyword.substring(0, index);
            int previousIndex = index;

            if (suggestionsContainsKeyword(keyword, keywordChunk)) {
                bestIndex = Math.min(bestIndex, index);
                isExist = true;
                if (index == 1 || maxIndex != null && maxIndex - minIndex == 2 || index == length) {
                    break;
                }
                index -= (index - minIndex) / 2;
                maxIndex = previousIndex;
            } else {
                if (index == length || maxIndex != null && maxIndex - minIndex == 2) {
                    break;
                }
                index += Math.max(1, (Optional.ofNullable(maxIndex).orElse(length) - index) / 2);
                minIndex = previousIndex;
            }
        }
        return new KeywordLookup(isExist, bestIndex);
    }

    private int getPercentageByLetterIndex(KeywordLookup lookup) {
        if (!lookup.isExist()) {
            return 0;
        } else if (lookup.getBestIndex() == 1) {
            return 100;
        }
        return Math.max(0, 100 - Math.min(99, lookup.getBestIndex() * 3));
    }

    @VisibleForTesting
    private List<String> getSuggestions(String keywordChunk) {
        return amazonSuggestionService.retrieveSuggestions(keywordChunk);
    }

    private boolean suggestionsContainsKeyword(String keyword, String keywordChunk) {
        final List<String> suggestionList = getSuggestions(keywordChunk);
        return suggestionList.stream().anyMatch(f -> f.equalsIgnoreCase(keyword.toLowerCase()));
    }

    class KeywordLookup {
        boolean isExist;
        int bestIndex;

        KeywordLookup(boolean isExist, int bestIndex) {
            this.isExist = isExist;
            this.bestIndex = bestIndex;
        }

        boolean isExist() {
            return isExist;
        }

        int getBestIndex() {
            return bestIndex;
        }
    }

}
