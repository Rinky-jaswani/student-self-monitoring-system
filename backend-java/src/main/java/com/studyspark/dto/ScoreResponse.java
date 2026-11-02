package com.studyspark.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ScoreResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private String examType;
    private BigDecimal scoreObtained;
    private BigDecimal maxScore;
    private BigDecimal percentage;
    private LocalDate examDate;

    public ScoreResponse(Long id, Long studentId, String studentName,
                         Long subjectId, String subjectName,
                         String examType, BigDecimal scoreObtained,
                         BigDecimal maxScore, BigDecimal percentage,
                         LocalDate examDate) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.examType = examType;
        this.scoreObtained = scoreObtained;
        this.maxScore = maxScore;
        this.percentage = percentage;
        this.examDate = examDate;
    }

    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public Long getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }
    public String getExamType() { return examType; }
    public BigDecimal getScoreObtained() { return scoreObtained; }
    public BigDecimal getMaxScore() { return maxScore; }
    public BigDecimal getPercentage() { return percentage; }
    public LocalDate getExamDate() { return examDate; }
}