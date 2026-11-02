package com.studyspark.repository;

import com.studyspark.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByStudentIdOrderByExamDateDesc(Long studentId);

    List<Score> findBySubjectIdOrderByExamDateDesc(Long subjectId);

    List<Score> findByStudentIdAndSubjectIdOrderByExamDateDesc(
            Long studentId, Long subjectId
    );
}