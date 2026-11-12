package com.studyspark.dto;

import java.time.LocalDateTime;

public record FeedbackResponse(
        Long id,
        Long subjectId,
        String recommendationTitle,
        String status,
        Integer usefulnessRating,
        LocalDateTime createdAt
) {}