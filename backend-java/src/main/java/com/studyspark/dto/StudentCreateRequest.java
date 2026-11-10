package com.studyspark.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

public class StudentCreateRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

    @Min(5)
    @Max(100)
    private Integer age;

    private String phone;

    private String course;

    @Pattern(regexp = "^(school|university|coaching|online_self_learner)$")
    private String learnerType;

         @NotBlank
     @Size(min = 2, max = 100)
    @Pattern
(regexp = "^[A-Za-z ] + $", message = "Name must contain letters and spaces only")
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

        @Max(100)
    

    @Pattern(
        regexp = "^[0-9]{7,15}$",
        message = "Phone must be 7-15 digits, numbers only"
    )
   

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getLearnerType() { return learnerType; }
    public void setLearnerType(String learnerType) {
        this.learnerType = learnerType;
    }
}