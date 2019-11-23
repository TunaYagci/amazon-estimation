package com.tunayagci.amazon.estimation.generic;

public class EstimateDTO {
    private final String keyword;
    private final int score;

    public EstimateDTO(String keyword, int score) {
        this.keyword = keyword;
        this.score = score;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getScore() {
        return score;
    }
}
