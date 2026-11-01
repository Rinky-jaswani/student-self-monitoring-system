package com.studyspark.checkin;

import java.util.List;

public class CheckinRequest {
    private Long studentId;
    private String studentMessage;
    private List<StudyLogSummary> recentLogs;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentMessage() { return studentMessage; }
    public void setStudentMessage(String studentMessage) { this.studentMessage = studentMessage; }

    public List<StudyLogSummary> getRecentLogs() { return recentLogs; }
    public void setRecentLogs(List<StudyLogSummary> recentLogs) { this.recentLogs = recentLogs; }
}
