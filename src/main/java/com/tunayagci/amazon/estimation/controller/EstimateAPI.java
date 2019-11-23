package com.tunayagci.amazon.estimation.controller;

import com.tunayagci.amazon.estimation.generic.EstimateDTO;
import com.tunayagci.amazon.estimation.service.EstimationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EstimateAPI {

    private EstimationService estimationService;

    public EstimateAPI(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    @GetMapping("/estimate")
    public EstimateDTO estimate(@RequestParam("keyword") String keyword) {
        return estimationService.estimate(keyword);
    }
}
