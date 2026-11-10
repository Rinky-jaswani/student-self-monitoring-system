package com.studyspark.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        Double totalStudyHours,
        Integer totalStudySessions,
        BigDecimal averageScore,
        List<SubjectPerformance> subjectPerformance,
        List<StudyLogResponse> recentActivity
) {
    public record SubjectPerformance(
            Long subjectId,
            String subjectName,
            BigDecimal averagePercentage
    ) {}
}