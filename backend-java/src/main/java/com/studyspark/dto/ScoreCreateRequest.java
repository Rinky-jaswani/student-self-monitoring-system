package com.studyspark.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ScoreCreateRequest {

    @NotNull
    @Positive
    private Long studentId;

    @NotNull
    @Positive
    private Long subjectId;

    @NotBlank
    @Size(max = 50)
    private String examType;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal scoreObtained;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal maxScore;

    @NotNull
    private LocalDate examDate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public BigDecimal getScoreObtained() {
        return scoreObtained;
    }

    public void setScoreObtained(BigDecimal scoreObtained) {
        this.scoreObtained = scoreObtained;
    }

    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(BigDecimal maxScore) {
        this.maxScore = maxScore;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }
}