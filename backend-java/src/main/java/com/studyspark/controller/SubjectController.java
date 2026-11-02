package com.studyspark.controller;

import com.studyspark.dto.SubjectCreateRequest;
import com.studyspark.dto.SubjectResponse;
import com.studyspark.repository.SubjectRepository;
import com.studyspark.service.SubjectService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final SubjectService subjectService;

    public SubjectController(
            SubjectRepository subjectRepository,
            SubjectService subjectService) {

        this.subjectRepository = subjectRepository;
        this.subjectService = subjectService;
    }

    // Get all subjects
    @GetMapping
    public List<SubjectResponse> getAllSubjects() {

        return subjectRepository.findAll()
                .stream()
                .map(SubjectResponse::new)
                .toList();
    }

    // Get subject by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(
            @PathVariable Long id) {

        return subjectRepository.findById(id)
                .map(subject -> ResponseEntity.ok(
                        new SubjectResponse(subject)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Create subject
    @PostMapping
    public ResponseEntity<SubjectResponse> createSubject(
            @Valid @RequestBody SubjectCreateRequest request) {

        SubjectResponse response =
                subjectService.createSubject(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}