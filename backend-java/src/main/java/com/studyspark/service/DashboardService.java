package com.studyspark.service;

import com.studyspark.dto.*;
import com.studyspark.entity.*;
import com.studyspark.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final StudyLogRepository studyLogRepository;
    private final ScoreRepository scoreRepository;

    public DashboardService(
            StudentRepository studentRepository,
            StudyLogRepository studyLogRepository,
            ScoreRepository scoreRepository) {

        this.studentRepository = studentRepository;
        this.studyLogRepository = studyLogRepository;
        this.scoreRepository = scoreRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(String email) {

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        List<StudyLog> logs =
                studyLogRepository
                .findByStudentIdOrderByLogDateDesc(student.getId());

        List<Score> scores =
                scoreRepository
                .findByStudentIdOrderByExamDateDesc(student.getId());

        // Total study hours
        double totalHours = logs.stream()
                .mapToDouble(StudyLog::getHoursSpent)
                .sum();

        // Average academic score
        BigDecimal totalObtained = scores.stream()
                .map(Score::getScoreObtained)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMaximum = scores.stream()
                .map(Score::getMaxScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageScore = BigDecimal.ZERO;

        if (totalMaximum.compareTo(BigDecimal.ZERO) > 0) {
            averageScore = totalObtained
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalMaximum, 2, RoundingMode.HALF_UP);
        }

        // Subject-wise performance
        Map<Long, List<Score>> groupedScores =
                scores.stream().collect(
                        Collectors.groupingBy(
                                score -> score.getSubject().getId(),
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                );

        List<DashboardResponse.SubjectPerformance> performance =
                groupedScores.entrySet().stream()
                .map(entry -> {

                    List<Score> subjectScores = entry.getValue();

                    BigDecimal obtained = subjectScores.stream()
                            .map(Score::getScoreObtained)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal maximum = subjectScores.stream()
                            .map(Score::getMaxScore)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal percentage = obtained
                            .multiply(BigDecimal.valueOf(100))
                            .divide(maximum, 2, RoundingMode.HALF_UP);

                    Score first = subjectScores.get(0);

                    return new DashboardResponse.SubjectPerformance(
                            entry.getKey(),
                            first.getSubject().getName(),
                            percentage
                    );
                })
                .toList();

        // Recent activity (latest 5 logs)
        List<StudyLogResponse> recentActivity = logs.stream()
                .limit(5)
                .map(StudyLogResponse::new)
                .toList();

        return new DashboardResponse(
                Math.round(totalHours * 100.0) / 100.0,
                logs.size(),
                averageScore,
                performance,
                recentActivity
        );
    }
}