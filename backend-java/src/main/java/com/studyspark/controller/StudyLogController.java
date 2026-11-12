package com.studyspark.controller;

import com.studyspark.dto.StudyLogCreateRequest;
import com.studyspark.dto.StudyLogResponse;
import com.studyspark.service.StudyLogService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/api/study-logs")
public class StudyLogController {

    private final StudyLogService studyLogService;

    public StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    // Create a new study log
    @PostMapping
    public ResponseEntity<StudyLogResponse> createStudyLog(
            @Valid @RequestBody StudyLogCreateRequest request) {

        StudyLogResponse response =
                studyLogService.createStudyLog(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all study logs
    @GetMapping
    public List<StudyLogResponse> getAllStudyLogs() {
        return studyLogService.getAllStudyLogs();
    }

    // Get study logs of a particular student
    @GetMapping("/student/{studentId}")
    public List<StudyLogResponse> getStudyLogsByStudent(
            @PathVariable Long studentId) {

        return studyLogService.getStudyLogsByStudent(studentId);
    }

    @DeleteMapping("/{logId}")
public ResponseEntity<Void> deleteStudyLog(
        @PathVariable Long logId,
        Principal principal) {

    studyLogService.deleteStudyLog(logId, principal.getName());

    return ResponseEntity.noContent().build();
}
}
