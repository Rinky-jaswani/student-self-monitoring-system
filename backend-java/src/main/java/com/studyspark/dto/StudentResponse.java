package com.studyspark.dto;

import com.studyspark.entity.Student;

public class StudentResponse {

    private Long id;
    private String name;
    private String username;
    private String email;
    private Integer age;
    private String course;
    private String learnerType;
    private String avatarId;
    private String displayName;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.username = student.getUsername();
        this.email = student.getEmail();
        this.age = student.getAge();
        this.course = student.getCourse();
        this.learnerType = student.getLearnerType();
        this.avatarId = student.getAvatarId();
        this.displayName = student.getDisplayName();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Integer getAge() { return age; }
    public String getCourse() { return course; }
    public String getLearnerType() { return learnerType; }
    public String getAvatarId() { return avatarId; }
    public String getDisplayName() { return displayName; }
}