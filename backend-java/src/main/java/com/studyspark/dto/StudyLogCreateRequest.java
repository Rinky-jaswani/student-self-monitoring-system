package com.studyspark.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class StudyLogCreateRequest {

    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be positive")
    private Long studentId;

    @NotNull(message = "Subject ID is required")
    @Positive(message = "Subject ID must be positive")
    private Long subjectId;

    @NotNull(message = "Study date is required")
    private LocalDate logDate;

    @NotNull(message = "Study hours are required")
    @DecimalMin(value = "0.1", message = "Study hours must be greater than zero")
    @DecimalMax(value = "24.0", message = "Study hours cannot exceed 24")
    private Double hoursSpent;

    @Size(max = 200, message = "Topic cannot exceed 200 characters")
    private String topic;

    private String notes;

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

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Double getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(Double hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}