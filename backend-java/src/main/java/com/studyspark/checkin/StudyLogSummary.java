package com.studyspark.checkin;

/**
 * A lightweight summary of one study_logs row, just enough context
 * for the AI to reference specifically. Once Postgres is wired up
 * (build order step 1), this will be populated by a real query instead
 * of the hardcoded sample data currently used in CheckinController.
 */
public class StudyLogSummary {
    private String date;
    private String subject;
    private String topic;
    private double hours;

    public StudyLogSummary() {}

    public StudyLogSummary(String date, String subject, String topic, double hours) {
        this.date = date;
        this.subject = subject;
        this.topic = topic;
        this.hours = hours;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public double getHours() { return hours; }
    public void setHours(double hours) { this.hours = hours; }
}
