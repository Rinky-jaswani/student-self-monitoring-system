package com.studyspark.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_feedback")
public class RecommendationFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    private Long subjectId;

    @Column(nullable = false, length = 200)
    private String recommendationTitle;

    @Column(nullable = false, length = 20)
    private String status;

    private Integer usefulnessRating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

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

    public String getRecommendationTitle() {
        return recommendationTitle;
    }

    public void setRecommendationTitle(String recommendationTitle) {
        this.recommendationTitle = recommendationTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUsefulnessRating() {
        return usefulnessRating;
    }

    public void setUsefulnessRating(Integer usefulnessRating) {
        this.usefulnessRating = usefulnessRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}