package com.studyspark.service;

import com.studyspark.dto.ScoreCreateRequest;
import com.studyspark.dto.ScoreResponse;
import com.studyspark.entity.Score;
import com.studyspark.entity.Student;
import com.studyspark.entity.Subject;
import com.studyspark.repository.ScoreRepository;
import com.studyspark.repository.StudentRepository;
import com.studyspark.repository.SubjectRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public ScoreService(ScoreRepository scoreRepository,
                        StudentRepository studentRepository,
                        SubjectRepository subjectRepository) {
        this.scoreRepository = scoreRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public ScoreResponse createScore(ScoreCreateRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() ->
                        new RuntimeException("Subject not found"));

        if (request.getScoreObtained().compareTo(request.getMaxScore()) > 0) {
            throw new IllegalArgumentException(
                    "Obtained score cannot exceed maximum score"
            );
        }

        Score score = new Score();

        score.setStudent(student);
        score.setSubject(subject);
        score.setExamType(request.getExamType());
        score.setScoreObtained(request.getScoreObtained());
        score.setMaxScore(request.getMaxScore());
        score.setExamDate(request.getExamDate());

        Score savedScore = scoreRepository.save(score);

        return convertToResponse(savedScore);
    }

    @Transactional(readOnly = true)
    public List<ScoreResponse> getAllScores() {
        return scoreRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScoreResponse> getScoresByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        return scoreRepository
                .findByStudentIdOrderByExamDateDesc(studentId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScoreResponse> getScoresByStudentAndSubject(
            Long studentId, Long subjectId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        if (!subjectRepository.existsById(subjectId)) {
            throw new RuntimeException("Subject not found");
        }

        return scoreRepository
                .findByStudentIdAndSubjectIdOrderByExamDateDesc(
                        studentId, subjectId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private ScoreResponse convertToResponse(Score score) {

        BigDecimal percentage = score.getScoreObtained()
                .multiply(new BigDecimal("100"))
                .divide(score.getMaxScore(), 2, RoundingMode.HALF_UP);

        return new ScoreResponse(
                score.getId(),
                score.getStudent().getId(),
                score.getStudent().getName(),
                score.getSubject().getId(),
                score.getSubject().getName(),
                score.getExamType(),
                score.getScoreObtained(),
                score.getMaxScore(),
                percentage,
                score.getExamDate()
        );
    }
}