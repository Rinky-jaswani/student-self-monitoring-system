package com.studyspark.dto;

import com.studyspark.entity.StudyLog;
import java.time.LocalDate;

public class StudyLogResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private LocalDate logDate;
    private Double hoursSpent;
    private String topic;
    private String notes;

    public StudyLogResponse(StudyLog studyLog) {

        this.id = studyLog.getId();

        this.studentId = studyLog.getStudent().getId();
        this.studentName = studyLog.getStudent().getName();

        this.subjectId = studyLog.getSubject().getId();
        this.subjectName = studyLog.getSubject().getName();

        this.logDate = studyLog.getLogDate();
        this.hoursSpent = studyLog.getHoursSpent();
        this.topic = studyLog.getTopic();
        this.notes = studyLog.getNotes();
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public Double getHoursSpent() {
        return hoursSpent;
    }

    public String getTopic() {
        return topic;
    }

    public String getNotes() {
        return notes;
    }
}