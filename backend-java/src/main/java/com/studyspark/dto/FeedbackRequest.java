package com.studyspark.dto;

public record FeedbackRequest(
        Long subjectId,
        String recommendationTitle,
        String status,
        Integer usefulnessRating
) {}