package com.studyspark.controller;

import com.studyspark.dto.StudentResponse;
import com.studyspark.entity.Student;
import com.studyspark.repository.StudentRepository;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Get currently logged-in student's profile
    @GetMapping("/me")
    public ResponseEntity<StudentResponse> getMyProfile(
            Principal principal) {

        Student student = studentRepository
                .findByEmail(principal.getName())
                .orElse(null);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new StudentResponse(student));
    }
}