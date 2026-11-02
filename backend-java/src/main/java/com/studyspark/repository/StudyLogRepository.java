package com.studyspark.repository;

import com.studyspark.entity.StudyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {

    List<StudyLog> findByStudentId(Long studentId);

    List<StudyLog> findBySubjectId(Long subjectId);

    List<StudyLog> findByStudentIdOrderByLogDateDesc(Long studentId);
}