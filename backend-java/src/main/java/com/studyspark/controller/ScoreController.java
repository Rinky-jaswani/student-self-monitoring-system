package com.studyspark.controller;

import com.studyspark.dto.ScoreCreateRequest;
import com.studyspark.dto.ScoreResponse;
import com.studyspark.service.ScoreService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScoreResponse createScore(
            @Valid @RequestBody ScoreCreateRequest request) {
        return scoreService.createScore(request);
    }

    @GetMapping
    public List<ScoreResponse> getAllScores() {
        return scoreService.getAllScores();
    }

    @GetMapping("/student/{studentId}")
    public List<ScoreResponse> getScoresByStudent(
            @PathVariable Long studentId) {
        return scoreService.getScoresByStudent(studentId);
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}")
    public List<ScoreResponse> getScoresByStudentAndSubject(
            @PathVariable Long studentId,
            @PathVariable Long subjectId) {
        return scoreService.getScoresByStudentAndSubject(
                studentId, subjectId);
    }
}