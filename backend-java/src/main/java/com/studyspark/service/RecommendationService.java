package com.studyspark.service;

import com.studyspark.dto.RecommendationResponse;
import com.studyspark.entity.Score;
import com.studyspark.entity.Student;
import com.studyspark.entity.StudyLog;
import com.studyspark.entity.RecommendationFeedback;

import com.studyspark.repository.ScoreRepository;
import com.studyspark.repository.StudentRepository;
import com.studyspark.repository.StudyLogRepository;
import com.studyspark.repository.RecommendationFeedbackRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class RecommendationService {

    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final StudyLogRepository studyLogRepository;
    private final RecommendationFeedbackRepository feedbackRepository;

    public RecommendationService(
            StudentRepository studentRepository,
            ScoreRepository scoreRepository,
            StudyLogRepository studyLogRepository,
            RecommendationFeedbackRepository feedbackRepository) {

        this.studentRepository = studentRepository;
        this.scoreRepository = scoreRepository;
        this.studyLogRepository = studyLogRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponse> getRecommendations(String email) {

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Score> scores =
                scoreRepository.findByStudentIdOrderByExamDateDesc(student.getId());

        List<StudyLog> logs =
                studyLogRepository.findByStudentIdOrderByLogDateDesc(student.getId());

        List<RecommendationFeedback> feedbackHistory =
                feedbackRepository.findByStudentIdOrderByCreatedAtDesc(
                        student.getId());

        Map<Long, SubjectStats> statsMap = new LinkedHashMap<>();

        // Process assessment scores
        for (Score score : scores) {

            Long subjectId = score.getSubject().getId();

            SubjectStats stats = statsMap.computeIfAbsent(
                    subjectId,
                    id -> new SubjectStats(id, score.getSubject().getName())
            );

            BigDecimal percentage = score.getScoreObtained()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(score.getMaxScore(), 2, RoundingMode.HALF_UP);

            stats.totalScore = stats.totalScore.add(percentage);
            stats.scoreCount++;
        }

        // Process study logs
        for (StudyLog log : logs) {

            Long subjectId = log.getSubject().getId();

            SubjectStats stats = statsMap.computeIfAbsent(
                    subjectId,
                    id -> new SubjectStats(id, log.getSubject().getName())
            );

            stats.totalHours += log.getHoursSpent();
        }

        // Process feedback history
        Map<Long, FeedbackStats> feedbackMap = new HashMap<>();

        for (RecommendationFeedback feedback : feedbackHistory) {

            Long subjectId = feedback.getSubjectId();

            if (subjectId == null) {
                continue;
            }

            FeedbackStats stats = feedbackMap.computeIfAbsent(
                    subjectId,
                    id -> new FeedbackStats()
            );

            switch (feedback.getStatus()) {

                case "DONE" -> stats.completed++;

                case "PARTLY" -> stats.partlyCompleted++;

                case "SKIPPED" -> stats.skipped++;
            }

            if (feedback.getUsefulnessRating() != null) {
                stats.totalRating += feedback.getUsefulnessRating();
                stats.ratingCount++;
            }
        }

        List<RecommendationResponse> recommendations = new ArrayList<>();

        // Generate initial recommendations
        for (SubjectStats stats : statsMap.values()) {

            double average = stats.scoreCount > 0
                    ? stats.totalScore.doubleValue() / stats.scoreCount
                    : -1;

            if (average >= 0 && average < 60) {

                recommendations.add(new RecommendationResponse(
                        stats.subjectId,
                        stats.subjectName,
                        "HIGH",
                        "Strengthen your fundamentals",
                        "Revise important concepts and solve basic practice questions.",
                        "Your average score in this subject is " +
                                String.format("%.1f", average) + "%.",
                        3.0
                ));

            } else if (average >= 60 && average < 75) {

                recommendations.add(new RecommendationResponse(
                        stats.subjectId,
                        stats.subjectName,
                        "MEDIUM",
                        "Improve your practice consistency",
                        "Attempt topic-wise questions and revise concepts where you lose marks.",
                        "Your current average score is " +
                                String.format("%.1f", average) + "%.",
                        2.0
                ));

            } else if (average >= 75 && stats.totalHours < 3) {

                recommendations.add(new RecommendationResponse(
                        stats.subjectId,
                        stats.subjectName,
                        "MEDIUM",
                        "Maintain regular study sessions",
                        "Continue practising to maintain your performance.",
                        "Your score is good, but recorded study time is relatively low.",
                        1.5
                ));

            } else if (average >= 75) {

                recommendations.add(new RecommendationResponse(
                        stats.subjectId,
                        stats.subjectName,
                        "LOW",
                        "Keep your progress steady",
                        "Revise key topics and attempt occasional mock tests.",
                        "Your average score indicates good progress.",
                        1.0
                ));

            } else {

                recommendations.add(new RecommendationResponse(
                        stats.subjectId,
                        stats.subjectName,
                        "MEDIUM",
                        "Build a regular study routine",
                        "Log study sessions and practise important topics.",
                        "No assessment score is available for this subject yet.",
                        2.0
                ));
            }
        }

        if (recommendations.isEmpty()) {

            recommendations.add(new RecommendationResponse(
                    null,
                    "General",
                    "MEDIUM",
                    "Start tracking your learning",
                    "Add subjects, record study sessions and enter assessment scores.",
                    "More learning data is needed to generate personalised recommendations.",
                    1.0
            ));
        }

        // Adapt recommendations using feedback
        List<RecommendationResponse> adaptedRecommendations =
                new ArrayList<>();

        for (RecommendationResponse recommendation : recommendations) {

            Long subjectId = recommendation.subjectId();

            FeedbackStats feedback = subjectId == null
                    ? null : feedbackMap.get(subjectId);

            if (feedback == null) {
                adaptedRecommendations.add(recommendation);
                continue;
            }

            double suggestedHours = recommendation.suggestedHours();

            String description = recommendation.description();
            String reason = recommendation.reason();

            int totalAttempts = feedback.completed
                    + feedback.partlyCompleted
                    + feedback.skipped;

            double averageRating = feedback.ratingCount > 0
                    ? (double) feedback.totalRating / feedback.ratingCount
                    : 0;

            // Repeatedly skipped recommendations
            if (feedback.skipped >= 2 &&
                    feedback.skipped > feedback.completed) {

                suggestedHours = Math.max(0.5, suggestedHours * 0.75);

                description =
                        "Try a smaller task: revise one concept or solve just 3 practice questions. "
                        + "You can gradually increase the workload.";

                reason =
                        "Your previous recommendations were frequently skipped. "
                        + "The suggested workload has been reduced to make it more manageable.";

            }

            // Repeatedly partially completed recommendations
            else if (feedback.partlyCompleted >= 2 &&
                    feedback.partlyCompleted > feedback.completed) {

                suggestedHours = Math.max(0.5, suggestedHours * 0.75);

                description =
                        "Break this topic into shorter study sessions. "
                        + "Focus on completing one section at a time.";

                reason =
                        "Your feedback shows that previous tasks were only partly completed. "
                        + "A shorter study target may be more manageable.";

            }

            // Low usefulness ratings
            else if (feedback.ratingCount >= 2 &&
                    averageRating <= 2) {

                description =
                        "Try a different learning approach: use examples, "
                        + "short revision notes and topic-wise practice questions.";

                reason =
                        "Your previous usefulness ratings were low. "
                        + "The learning approach has been adjusted.";

            }

            // Consistent completion and positive ratings
            else if (feedback.completed >= 2 &&
                    feedback.ratingCount >= 2 &&
                    averageRating >= 4) {

                reason +=
                        " Your previous recommendations were completed "
                        + "and received positive usefulness ratings. "
                        + "Your current study targets will be maintained.";
            }

            adaptedRecommendations.add(new RecommendationResponse(
                    recommendation.subjectId(),
                    recommendation.subjectName(),
                    recommendation.priority(),
                    recommendation.title(),
                    description,
                    reason,
                    suggestedHours
            ));
        }

        adaptedRecommendations.sort(
                Comparator.comparingInt(r ->
                        switch (r.priority()) {
                            case "HIGH" -> 1;
                            case "MEDIUM" -> 2;
                            default -> 3;
                        }
                )
        );

        return adaptedRecommendations;
    }

    private static class SubjectStats {

        Long subjectId;
        String subjectName;
        BigDecimal totalScore = BigDecimal.ZERO;
        int scoreCount = 0;
        double totalHours = 0;

        SubjectStats(Long subjectId, String subjectName) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
        }
    }

    private static class FeedbackStats {

        int completed = 0;
        int partlyCompleted = 0;
        int skipped = 0;

        int totalRating = 0;
        int ratingCount = 0;
    }
}