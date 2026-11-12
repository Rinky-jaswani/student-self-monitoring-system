package com.studyspark.controller;

import com.studyspark.dto.*;
import com.studyspark.service.RecommendationFeedbackService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/recommendation-feedback")
public class RecommendationFeedbackController {

    private final RecommendationFeedbackService feedbackService;

    public RecommendationFeedbackController(
            RecommendationFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackResponse save(
            @RequestBody FeedbackRequest request,
            Principal principal) {

        return feedbackService.saveFeedback(
                principal.getName(), request);
    }

    @GetMapping("/me")
    public List<FeedbackResponse> getMyFeedback(
            Principal principal) {

        return feedbackService.getFeedback(principal.getName());
    }

    @GetMapping("/summary")
    public FeedbackSummary getSummary(Principal principal) {

        return feedbackService.getSummary(principal.getName());
    }
}