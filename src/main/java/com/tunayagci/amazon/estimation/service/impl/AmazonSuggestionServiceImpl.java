package com.tunayagci.amazon.estimation.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tunayagci.amazon.estimation.service.AmazonSuggestionService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmazonSuggestionServiceImpl implements AmazonSuggestionService {

    private static final String SEARCH_API_PATH = "https://completion.amazon.com/search/complete";
    private static final RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Override
    @NonNull
    public List<String> retrieveSuggestions(String keyword) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SEARCH_API_PATH)
                .encode()
                .queryParam("q", keyword.replace(" ", "+"))
                .queryParam("mkt", "1")
                .queryParam("search-alias", "aps");

        final String response = restTemplate.getForObject(builder.toUriString(), String.class);
        if (response == null) {
            throw new RuntimeException("Cannot process response from Amazon");
        }
        ArrayNode arrayNode;
        try {
            arrayNode = (ArrayNode) new ObjectMapper().readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot process Amazon response", e);
        }
        final ArrayNode suggestionsAsArrayNode = (ArrayNode) arrayNode.get(1);
        List<String> suggestions = new ArrayList<>();
        suggestionsAsArrayNode.elements().forEachRemaining(f -> suggestions.add(f.asText()));
        return suggestions;
    }
}
