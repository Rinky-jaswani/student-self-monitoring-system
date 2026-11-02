package com.studyspark.service;

import com.studyspark.dto.SubjectCreateRequest;
import com.studyspark.dto.SubjectResponse;
import com.studyspark.entity.Subject;
import com.studyspark.repository.SubjectRepository;

import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public SubjectResponse createSubject(SubjectCreateRequest request) {

        String subjectName = request.getName().trim();

        if (subjectRepository.existsByName(subjectName)) {
            throw new IllegalArgumentException(
                    "Subject already exists."
            );
        }

        Subject subject = new Subject();

        subject.setName(subjectName);
        subject.setCategory(
                request.getCategory() == null
                        ? null
                        : request.getCategory().trim()
        );

        Subject savedSubject = subjectRepository.save(subject);

        return new SubjectResponse(savedSubject);
    }
}