# StudySpark
## AI-Powered Student Self-Monitoring and Performance Analysis System

**StudySpark** is a web-based academic self-monitoring platform designed to help students record study activities, monitor academic performance, review progress, and receive AI-assisted academic guidance.

The system combines a student-facing web interface, a Java Spring Boot backend, a PostgreSQL database, and a Python Flask AI service integrated with the locally running Llama 3.2 model through Ollama.

---

## 1. Project Overview

Students often find it difficult to consistently monitor their study activities, identify topics requiring additional attention, and understand their academic progress.

StudySpark provides a centralized platform where students can:

- Manage their academic profile.
- Organize subjects.
- Record daily study activities.
- Track academic scores.
- View study and performance information.
- Explore personalized study recommendations.
- Interact with Spark, an AI-powered study companion.

The project also explores the research direction of student self-monitoring and student-action-driven recommendation adaptation.

**Working application name:** StudySpark

**Project title:** AI Powered Student Self Monitoring & Performance Analysis System

---

## 2. Key Features

| Module | Description |
|---|---|
| Student Authentication | Student registration and login with password protection and JWT-based authentication. |
| Student Dashboard | Centralized view of academic activity and progress. |
| Subject Management | Add and manage academic subjects. |
| Study Tracker | Record study sessions, topics, dates, study hours and notes. |
| Academic Performance | Record and review test and examination scores. |
| Progress Visualization | Display academic and study-related information through dashboard charts. |
| Recommendations | Student-facing interface for reviewing study recommendations. |
| Spark AI Study Companion | AI chat interface for academic questions and study guidance. |
| Context-Aware AI Support | Recent study activity can be supplied as context for AI responses. |
| RAG-Based Knowledge Retrieval | Retrieves relevant information from a built-in academic knowledge base before generating AI responses. |
| Student Engagement | Supporting interface elements such as mascot and mini-games. |

---

## 3. Technology Stack

| Layer | Technology |
|---|---|
| Frontend | HTML5, CSS3, JavaScript |
| Core Backend | Java, Spring Boot 3.3.2 |
| Backend Architecture | REST APIs |
| Authentication | JWT and password hashing |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| AI Service | Python, Flask |
| Large Language Model | Llama 3.2 |
| Local Model Runtime | Ollama |
| Retrieval | Keyword-based knowledge retrieval |
| Version Control | Git and GitHub |
| Development Tools | VS Code, Postman |

**Important implementation note:** The current RAG implementation uses keyword-based retrieval from a built-in academic knowledge base. It does not currently use pgvector or embedding-based vector search.

---

## 4. System Architecture

StudySpark follows a multi-component architecture.

### Frontend Layer
The browser-based interface provides student registration, login, dashboard access, study activity management, academic progress views, recommendations and AI chat.

### Java Backend Layer
The Spring Boot backend handles:
- Authentication and authorization.
- Student and academic data management.
- Subject, study-log and score operations.
- Database communication.
- Communication with the Flask AI service.

### AI Service Layer
The Flask service:
- Receives academic questions and relevant student context.
- Retrieves relevant information from the academic knowledge base.
- Constructs the prompt using retrieved context.
- Communicates with the local Llama 3.2 model through Ollama.
- Returns the generated response.

### Database Layer
PostgreSQL stores the application's persistent student and academic information.

### Architecture Flow

```text
                 STUDENT
                    |
                    v
          FRONTEND (HTML/CSS/JS)
                    |
                    | HTTP / JSON
                    v
          JAVA SPRING BOOT API
             /            \
            /              \
           v                v
    PostgreSQL          Flask AI Service
    Database                  |
                              v
                     Keyword-Based Retrieval
                              |
                              v
                       Context Builder
                              |
                              v
                        Ollama Runtime
                              |
                              v
                         Llama 3.2
                              |
                              v
                    Generated AI Response
                              |
                              v
                    Java Backend API
                              |
                              v
                       Frontend Chat
```

---

## 5. Spark AI and RAG Workflow

Spark is the AI-powered study companion integrated into the student dashboard.

The current AI interaction follows this workflow:

1. The student enters an academic question in the chat interface.
2. The frontend sends the question to the Java backend.
3. Recent study logs are included as student-specific context.
4. Java forwards the request to the Flask AI service.
5. Flask retrieves relevant documents from the built-in academic knowledge base using keyword matching.
6. Retrieved information and recent study context are used to construct the prompt.
7. Ollama runs the Llama 3.2 model to generate a response.
8. Flask returns the response to Java.
9. Java sends the response back to the frontend.
10. The student sees the AI-generated answer in the dashboard.

### Current Knowledge Base

The built-in knowledge base contains 10 academic documents covering:

- Operating Systems: Process Management, CPU Scheduling, Memory Management and File Systems.
- Java: Object-Oriented Programming and Exception Handling.
- DBMS: Normalization and SQL.
- Data Structures: Arrays and Linked Lists.
- Web Development: HTML, CSS and JavaScript.

The Flask health endpoint can be used to inspect the AI service and RAG status.

---

## 6. Student Self-Monitoring Workflow

The application supports the following student workflow:

```text
          Student Registration / Login
                     |
                     v
                Dashboard
                     |
          +----------+----------+
          |          |          |
          v          v          v
       Subjects   Study Logs   Scores
          |          |          |
          +----------+----------+
                     |
                     v
              Academic Overview
                     |
          +----------+----------+
          |                     |
          v                     v
    Recommendations        Spark AI Chat
          |                     |
          v                     v
    Progress Review       AI Guidance
```

The broader research framework is:

**Monitor → Analyse → Recommend → Act → Re-monitor → Adapt**

This framework represents the research direction. The complete experimental adaptation mechanism and its evaluation should be treated separately from the currently implemented application features.

---

## 7. Repository Structure

```text
student-monitoring-system/
│
├── ai-service-flask/
│   ├── app.py
│   └── requirements.txt
│
├── backend-java/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/studyspark/
│           └── resources/
│
├── frontend/
│   ├── pages/
│   ├── scripts/
│   └── styles/
│
├── docs/
│
├── practice/
│
└── README.md
```

---

## 8. Running the Project Locally

The application currently uses separate local services.

### Prerequisites

- Java and Maven
- PostgreSQL
- Python
- Ollama
- Llama 3.2 model
- A modern web browser

### Step 1: Start PostgreSQL

Ensure PostgreSQL is running and the StudySpark database is configured.

Database connection settings must match the local backend configuration.

### Step 2: Start Ollama

Ensure Ollama is installed and the required model is available.

```powershell
ollama pull llama3.2
ollama serve
```

### Step 3: Start the Flask AI Service

```powershell
cd ai-service-flask

python -m venv venv

.\venv\Scripts\Activate.ps1

pip install -r requirements.txt

python app.py
```

AI service:

```text
http://localhost:5000
```

Health check:

```text
http://localhost:5000/health
```

### Step 4: Start the Java Backend

Open another terminal:

```powershell
cd backend-java

mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

### Step 5: Start the Frontend

Open another terminal:

```powershell
cd frontend

python -m http.server 5500
```

Open the authentication page:

```text
http://localhost:5500/pages/auth.html
```

The frontend, Java backend, Flask service and Ollama runtime must be running for the complete local application workflow.

**Configuration note:** Set database credentials, JWT configuration and service URLs according to the local environment. Never commit passwords, JWT secrets or other credentials to GitHub.

---

## 9. Development and Implementation Status

The following table distinguishes implemented application modules from broader research objectives.

| Component | Current Status |
|---|---|
| Student Registration and Login | Implemented |
| JWT Authentication | Implemented |
| PostgreSQL Integration | Implemented |
| Subject Management | Implemented |
| Study Activity Logging | Implemented |
| Academic Score Management | Implemented |
| Dashboard Interface | Implemented |
| Progress Charts | Integrated in dashboard |
| Recommendations Interface | Implemented |
| Spark AI Chat Interface | Implemented |
| Java-to-Flask Integration | Implemented |
| Ollama Llama 3.2 Integration | Implemented |
| Keyword-Based RAG Retrieval | Implemented |
| Recent Study Log Context for AI | Integrated |
| Complete Student-Action-Driven Adaptive Engine | Not established as implemented |
| Static-versus-Adaptive Experimental Evaluation | Pending research methodology and evaluation |

---

## 10. Current Limitations and Scope

- The application is currently configured for local development.
- Public internet deployment has not yet been established.
- The RAG implementation uses keyword-based retrieval rather than vector embeddings.
- The built-in knowledge base covers selected academic topics and is not an exhaustive academic resource.
- AI responses depend on the availability of the local Ollama service and the configured model.
- The complete research-oriented recommendation, adherence and adaptation loop requires separate implementation and evaluation.
- The effectiveness of the system in improving long-term student performance has not been established.

---

## 11. Future Enhancements

Potential future improvements include:

- More extensive academic knowledge coverage.
- Improved retrieval techniques.
- Structured recommendation and adherence records.
- Explicit rule-based recommendation adaptation.
- Research data export and evaluation support.
- Deployment to a suitable hosting environment.
- Additional student engagement and learning-support features.

These are future directions and should not be interpreted as already implemented functionality.

---

## 12. Research Positioning

The project is centered on student self-monitoring, academic performance analysis and personalized learning support.

Its broader research direction investigates how student activity after receiving recommendations can inform subsequent monitoring and recommendation decisions.

The research framework is:

**Monitor → Analyse → Recommend → Act → Re-monitor → Adapt**

The AI assistant and RAG pipeline support academic guidance, while the proposed student-action-driven adaptation mechanism remains a distinct research objective requiring explicit implementation and evaluation.

---

## 13. Repository

**GitHub Repository:**  
https://github.com/Rinky-jaswani/student-self-monitoring-system

**Project Name:** StudySpark  
**Project Domain:** Educational Technology / AI-Assisted Learning  
**Development Stack:** Java, Spring Boot, PostgreSQL, Python, Flask, HTML, CSS and JavaScript.
