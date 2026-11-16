package com.studyspark.service;

import com.studyspark.dto.AuthLoginRequest;
import com.studyspark.dto.AuthResponse;
import com.studyspark.dto.StudentCreateRequest;
import com.studyspark.dto.StudentResponse;
import com.studyspark.entity.Student;
import com.studyspark.repository.StudentRepository;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            StudentService studentService,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(StudentCreateRequest request) {

        StudentResponse student =
                studentService.registerStudent(request);

        String token = jwtService.generateToken(student.getEmail());

        return new AuthResponse(token, student);
    }

    public AuthResponse login(AuthLoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                student.getPasswordHash())) {

            throw new BadCredentialsException(
                    "Invalid email or password");
        }

        StudentResponse studentResponse =
                new StudentResponse(student);

        String token = jwtService.generateToken(student.getEmail());

        return new AuthResponse(token, studentResponse);
    }
}