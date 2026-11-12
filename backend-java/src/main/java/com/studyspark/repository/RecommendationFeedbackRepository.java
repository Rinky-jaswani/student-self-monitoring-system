package com.studyspark.repository;

import com.studyspark.entity.RecommendationFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecommendationFeedbackRepository
        extends JpaRepository<RecommendationFeedback, Long> {

    List<RecommendationFeedback> findByStudentIdOrderByCreatedAtDesc(
            Long studentId
    );
}