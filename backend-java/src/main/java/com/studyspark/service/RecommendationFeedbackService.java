package com.studyspark.service;

import com.studyspark.dto.*;
import com.studyspark.entity.*;
import com.studyspark.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecommendationFeedbackService {

    private final RecommendationFeedbackRepository feedbackRepository;
    private final StudentRepository studentRepository;

    public RecommendationFeedbackService(
            RecommendationFeedbackRepository feedbackRepository,
            StudentRepository studentRepository) {
        this.feedbackRepository = feedbackRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public FeedbackResponse saveFeedback(
            String email, FeedbackRequest request) {

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        if (request.recommendationTitle() == null ||
                request.recommendationTitle().isBlank()) {
            throw new IllegalArgumentException(
                    "Recommendation title is required");
        }

        String status = request.status() == null
                ? "" : request.status().toUpperCase();

        if (!List.of("DONE", "PARTLY", "SKIPPED").contains(status)) {
            throw new IllegalArgumentException("Invalid feedback status");
        }

        Integer rating = request.usefulnessRating();

        if (rating != null && (rating < 1 || rating > 5)) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5");
        }

        RecommendationFeedback feedback =
                new RecommendationFeedback();

        feedback.setStudentId(student.getId());
        feedback.setSubjectId(request.subjectId());
        feedback.setRecommendationTitle(
                request.recommendationTitle().trim());
        feedback.setStatus(status);
        feedback.setUsefulnessRating(rating);

        RecommendationFeedback saved =
                feedbackRepository.save(feedback);

        return toResponse(saved);
    }

    public List<FeedbackResponse> getFeedback(String email) {

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return feedbackRepository
                .findByStudentIdOrderByCreatedAtDesc(student.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public FeedbackSummary getSummary(String email) {

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        List<RecommendationFeedback> feedback =
                feedbackRepository
                .findByStudentIdOrderByCreatedAtDesc(student.getId());

        long total = feedback.size();

        long done = feedback.stream()
                .filter(f -> "DONE".equals(f.getStatus())).count();

        long partly = feedback.stream()
                .filter(f -> "PARTLY".equals(f.getStatus())).count();

        long skipped = feedback.stream()
                .filter(f -> "SKIPPED".equals(f.getStatus())).count();

        double adherence = total == 0 ? 0 :
                ((done + partly * 0.5) / total) * 100;

        return new FeedbackSummary(
                total, done, partly, skipped,
                Math.round(adherence * 10.0) / 10.0
        );
    }

    private FeedbackResponse toResponse(
            RecommendationFeedback feedback) {

        return new FeedbackResponse(
                feedback.getId(),
                feedback.getSubjectId(),
                feedback.getRecommendationTitle(),
                feedback.getStatus(),
                feedback.getUsefulnessRating(),
                feedback.getCreatedAt()
        );
    }
}