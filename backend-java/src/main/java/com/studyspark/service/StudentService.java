package com.studyspark.service;

import com.studyspark.dto.StudentCreateRequest;
import com.studyspark.dto.StudentResponse;
import com.studyspark.entity.Student;
import com.studyspark.repository.StudentRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public StudentResponse registerStudent(StudentCreateRequest request) {

        String email = request.getEmail().trim().toLowerCase();
        String username = request.getUsername().trim();

        if (studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        if (studentRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        Student student = new Student();

        student.setName(request.getName().trim());
        student.setUsername(username);
        student.setEmail(email);
        student.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        student.setAge(request.getAge());
        student.setPhone(request.getPhone());
        student.setCourse(request.getCourse());
        student.setLearnerType(request.getLearnerType());

        student.setAvatarId("avatar_1");
        student.setLeaderboardOptIn(false);

        Student savedStudent = studentRepository.save(student);

        return new StudentResponse(savedStudent);
    }
}