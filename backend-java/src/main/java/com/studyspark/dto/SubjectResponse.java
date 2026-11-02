package com.studyspark.dto;

import com.studyspark.entity.Subject;

public class SubjectResponse {

    private Long id;
    private String name;
    private String category;

    public SubjectResponse(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.category = subject.getCategory();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}