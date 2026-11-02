package com.studyspark.controller;

import com.studyspark.dto.StudentCreateRequest;
import com.studyspark.dto.StudentResponse;
import com.studyspark.repository.StudentRepository;
import com.studyspark.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public StudentController(
            StudentRepository studentRepository,
            StudentService studentService) {

        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    // Get all students
    @GetMapping
    public List<StudentResponse> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::new)
                .toList();
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long id) {

        return studentRepository.findById(id)
                .map(student -> ResponseEntity.ok(
                        new StudentResponse(student)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Register a new student
    @PostMapping
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody StudentCreateRequest request) {

        StudentResponse response =
                studentService.registerStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}