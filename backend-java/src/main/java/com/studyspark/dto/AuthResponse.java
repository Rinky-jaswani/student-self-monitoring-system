package com.studyspark.dto;

public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private StudentResponse student;

    public AuthResponse(String accessToken, StudentResponse student) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.student = student;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public StudentResponse getStudent() {
        return student;
    }
}