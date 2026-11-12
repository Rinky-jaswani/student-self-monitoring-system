package com.studyspark.dto;

public record FeedbackSummary(
        long total,
        long completed,
        long partlyCompleted,
        long skipped,
        double adherenceRate
) {}