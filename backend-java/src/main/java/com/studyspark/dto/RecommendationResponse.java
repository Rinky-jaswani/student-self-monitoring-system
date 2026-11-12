package com.studyspark.dto;

public record RecommendationResponse(
        Long subjectId,
        String subjectName,
        String priority,
        String title,
        String description,
        String reason,
        Double suggestedHours
) {}