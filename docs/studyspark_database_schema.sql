-- StudySpark: PostgreSQL database schema (core MVP)
-- Scope: students, subjects, study_logs, scores.
-- Designed for the Spring Boot/JPA backend. Review entity field names before
-- applying this manually if Hibernate is currently managing the schema.

CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(30) NOT NULL UNIQUE,
    age INTEGER,
    phone VARCHAR(20),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    course VARCHAR(120),
    learner_type VARCHAR(60),
    avatar_id VARCHAR(100),
    display_name VARCHAR(100),
    leaderboard_opt_in BOOLEAN NOT NULL DEFAULT FALSE,
    username_changed_at DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_students_age CHECK (age IS NULL OR (age BETWEEN 5 AND 100))
);

CREATE TABLE IF NOT EXISTS subjects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    student_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subjects_student
        FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT uq_subject_student_name UNIQUE (student_id, name)
);

CREATE TABLE IF NOT EXISTS study_logs (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT,
    topic VARCHAR(200),
    study_date DATE NOT NULL DEFAULT CURRENT_DATE,
    duration_minutes INTEGER NOT NULL DEFAULT 0,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_study_logs_student
        FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_study_logs_subject
        FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE SET NULL,
    CONSTRAINT chk_study_logs_duration CHECK (duration_minutes >= 0)
);

CREATE TABLE IF NOT EXISTS scores (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT,
    assessment_name VARCHAR(150),
    score NUMERIC(7,2) NOT NULL,
    max_score NUMERIC(7,2) NOT NULL DEFAULT 100,
    assessment_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_scores_student
        FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_scores_subject
        FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE SET NULL,
    CONSTRAINT chk_scores_nonnegative CHECK (score >= 0 AND max_score > 0),
    CONSTRAINT chk_scores_within_max CHECK (score <= max_score)
);

CREATE INDEX IF NOT EXISTS idx_subjects_student_id ON subjects(student_id);
CREATE INDEX IF NOT EXISTS idx_study_logs_student_date ON study_logs(student_id, study_date);
CREATE INDEX IF NOT EXISTS idx_study_logs_subject_id ON study_logs(subject_id);
CREATE INDEX IF NOT EXISTS idx_scores_student_date ON scores(student_id, assessment_date);
CREATE INDEX IF NOT EXISTS idx_scores_subject_id ON scores(subject_id);

-- Planned future modules (insights, chat_history, embeddings, streaks,
-- learning_preferences, game_sessions, token_transactions,
-- media_attachments, career_roadmaps) are intentionally not created here
-- until their entity fields and relationships are finalized.
