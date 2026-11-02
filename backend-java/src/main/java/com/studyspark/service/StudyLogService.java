package com.studyspark.service;

import com.studyspark.dto.StudyLogCreateRequest;
import com.studyspark.dto.StudyLogResponse;
import com.studyspark.entity.Student;
import com.studyspark.entity.Subject;
import com.studyspark.entity.StudyLog;
import com.studyspark.repository.StudentRepository;
import com.studyspark.repository.SubjectRepository;
import com.studyspark.repository.StudyLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public StudyLogService(
            StudyLogRepository studyLogRepository,
            StudentRepository studentRepository,
            SubjectRepository subjectRepository) {

        this.studyLogRepository = studyLogRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public StudyLogResponse createStudyLog(StudyLogCreateRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Subject not found"));

        StudyLog studyLog = new StudyLog();

        studyLog.setStudent(student);
        studyLog.setSubject(subject);
        studyLog.setLogDate(request.getLogDate());
        studyLog.setHoursSpent(request.getHoursSpent());
        studyLog.setTopic(request.getTopic());
        studyLog.setNotes(request.getNotes());

        StudyLog savedLog = studyLogRepository.save(studyLog);

        return new StudyLogResponse(savedLog);
    }

    @Transactional(readOnly = true)
    public List<StudyLogResponse> getAllStudyLogs() {

        return studyLogRepository.findAll()
                .stream()
                .map(StudyLogResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudyLogResponse> getStudyLogsByStudent(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student not found");
        }

        return studyLogRepository
                .findByStudentIdOrderByLogDateDesc(studentId)
                .stream()
                .map(StudyLogResponse::new)
                .toList();
    }
}