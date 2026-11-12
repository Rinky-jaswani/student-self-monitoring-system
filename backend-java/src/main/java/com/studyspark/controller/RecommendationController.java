package com.studyspark.controller;

import com.studyspark.dto.RecommendationResponse;
import com.studyspark.service.RecommendationService;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/me")
    public List<RecommendationResponse> getMyRecommendations(
            Principal principal) {

        return recommendationService.getRecommendations(
                principal.getName()
        );
    }
}