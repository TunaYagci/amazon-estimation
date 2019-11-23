package com.tunayagci.amazon.estimation.service;

import com.tunayagci.amazon.estimation.generic.EstimateDTO;

/**
 * An interface to get a score (0-100) of a keyword that is being searched on Amazon.
 */
public interface EstimationService {

    /**
     * Recursively sends request to Amazon and returns a score of 0 to 100, by its volume.
     * <p>
     * Score is estimated by the first encounter of keyword's letters.
     * <p>
     * Detailed algorithm explanation follows:
     * Keyword is split into small chunks, as a binary search algorithm.
     * Each chunk is either populated with more letters or decreased according to its existence in suggestions.
     * <p>
     * Ex:
     * Keyword: "iphone charger"
     * Binary splitting completed as "iphone" -> "iph" -> "ip" -> "i"
     * For keywords that has a suggestion containing itself on the first letter, score is calculated as 100
     * For keywords that has no suggestion in any chunk of its keyword, score is calculated as 0
     * <p>
     * Keyword matching is done by {@link String#equalsIgnoreCase(String)}
     * So for any suggestions that has a prefix for this keyword but not equal isn't considered as a "match".
     *
     * @param keyword the search keyword
     * @return an EstimationDTO which contains the keyword that is being requested and the score.
     */
    EstimateDTO estimate(String keyword);
}
