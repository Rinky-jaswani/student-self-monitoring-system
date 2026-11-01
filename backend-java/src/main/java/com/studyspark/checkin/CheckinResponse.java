package com.studyspark.checkin;

public class CheckinResponse {
    private String reply;

    public CheckinResponse() {}

    public CheckinResponse(String reply) {
        this.reply = reply;
    }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
}
