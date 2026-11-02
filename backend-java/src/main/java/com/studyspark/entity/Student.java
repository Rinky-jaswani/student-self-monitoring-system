package com.studyspark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Maps to the "students" table defined in
 * docs/system_architecture_and_db_design.md (section 4.2).
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain letters and spaces only")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    @Column(name = "username", nullable = false, unique = true, length = 30)
    private String username;

    @Min(value = 5, message = "Age must be at least 5")
    @Max(value = 100, message = "Age must be at most 100")
    @Column(name = "age")
    private Integer age;

    @Pattern(regexp = "^[0-9]{7,15}$", message = "Phone must be 7-15 digits, numbers only")
    @Column(name = "phone", length = 15)
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 150, message = "Email must be at most 150 characters")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // Only the BCrypt hash is ever stored, never the plain password.
    @NotBlank(message = "Password hash is required")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Size(max = 100, message = "Course must be at most 100 characters")
    @Column(name = "course", length = 100)
    private String course;

    // 'school' | 'university' | 'coaching' | 'online_self_learner'
    @Pattern(regexp = "^(school|university|coaching|online_self_learner)$",
             message = "learnerType must be one of: school, university, coaching, online_self_learner")
    @Column(name = "learner_type", length = 20)
    private String learnerType;

    @Size(max = 20, message = "avatarId must be at most 20 characters")
    @Column(name = "avatar_id", length = 20)
    private String avatarId = "avatar_1";

    // Shown on the leaderboard instead of the real name.
    @Size(max = 50, message = "displayName must be at most 50 characters")
    @Column(name = "display_name", length = 50)
    private String displayName;

    // Opt-in only: students are hidden from the leaderboard by default.
    @Column(name = "leaderboard_opt_in", nullable = false)
    private boolean leaderboardOptIn = false;

    // Used to enforce the 14-day username change cooldown.
    @Column(name = "username_changed_at")
    private LocalDateTime usernameChangedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Student() {
    }

    // JPA does not apply SQL DEFAULT values, so the dates are set here.
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (usernameChangedAt == null) {
            usernameChangedAt = now;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getLearnerType() { return learnerType; }
    public void setLearnerType(String learnerType) { this.learnerType = learnerType; }

    public String getAvatarId() { return avatarId; }
public void setAvatarId(String avatarId) { this.avatarId = avatarId; }

public String getDisplayName() { return displayName; }
public void setDisplayName(String displayName) { this.displayName = displayName; }

public boolean isLeaderboardOptIn() { return leaderboardOptIn; }
public void setLeaderboardOptIn(boolean leaderboardOptIn) {
    this.leaderboardOptIn = leaderboardOptIn;
}

public LocalDateTime getUsernameChangedAt() {
    return usernameChangedAt;
}

public void setUsernameChangedAt(LocalDateTime usernameChangedAt) {
    this.usernameChangedAt = usernameChangedAt;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}
}