"""
StudySpark AI Microservice
Flask + Ollama + Lightweight RAG
"""

import os
import re
import requests

from flask import Flask, request, jsonify
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

OLLAMA_BASE_URL = os.environ.get(
    "OLLAMA_BASE_URL", "http://localhost:11434"
)

OLLAMA_MODEL = os.environ.get("OLLAMA_MODEL", "llama3.2")


# --------------------------------------------------
# 1. BUILT-IN STUDY KNOWLEDGE BASE
# --------------------------------------------------

KNOWLEDGE_BASE = [
    {
        "subject": "Operating Systems",
        "topic": "Process Management",
        "content": (
            "A process is a program in execution. "
            "Process management involves process creation, scheduling, "
            "execution and termination. Common process states include "
            "New, Ready, Running, Waiting and Terminated. "
            "CPU scheduling algorithms include FCFS, SJF, Priority "
            "Scheduling and Round Robin."
        )
    },
    {
        "subject": "Operating Systems",
        "topic": "CPU Scheduling",
        "content": (
            "CPU scheduling selects a process from the ready queue "
            "for CPU execution. FCFS executes processes in arrival order. "
            "SJF selects the process with the shortest burst time. "
            "Round Robin assigns each process a fixed time quantum. "
            "Important scheduling metrics include waiting time, "
            "turnaround time and response time."
        )
    },
    {
        "subject": "Operating Systems",
        "topic": "Memory Management",
        "content": (
            "Memory management controls how processes use main memory. "
            "Paging divides memory into fixed-size pages and frames. "
            "Segmentation divides programs into logical segments. "
            "Virtual memory allows programs to use more memory than "
            "the available physical RAM. Page replacement algorithms "
            "include FIFO and LRU."
        )
    },
    {
        "subject": "Operating Systems",
        "topic": "File Systems",
        "content": (
            "A file system organizes and manages files on storage devices. "
            "It supports file creation, deletion, reading, writing and "
            "directory management. Common file systems include NTFS, "
            "FAT32 and ext4. File allocation methods include contiguous, "
            "linked and indexed allocation."
        )
    },
    {
        "subject": "Java",
        "topic": "Object Oriented Programming",
        "content": (
            "Object-oriented programming organizes software using classes "
            "and objects. The four major OOP principles are encapsulation, "
            "inheritance, polymorphism and abstraction. A class defines "
            "structure and behavior, while an object is an instance of "
            "a class."
        )
    },
    {
        "subject": "Java",
        "topic": "Exception Handling",
        "content": (
            "Exception handling manages runtime errors in Java. "
            "Java uses try, catch, finally, throw and throws. "
            "Checked exceptions are checked at compile time, while "
            "unchecked exceptions occur at runtime. "
            "Finally blocks are commonly used for cleanup."
        )
    },
    {
        "subject": "DBMS",
        "topic": "Database Normalization",
        "content": (
            "Normalization organizes relational database tables to reduce "
            "redundancy and improve data integrity. First Normal Form "
            "requires atomic values. Second Normal Form removes partial "
            "dependencies. Third Normal Form removes transitive dependencies."
        )
    },
    {
        "subject": "DBMS",
        "topic": "SQL",
        "content": (
            "SQL is used to manage relational databases. SELECT retrieves "
            "data, INSERT adds records, UPDATE modifies records and DELETE "
            "removes records. JOIN combines rows from related tables. "
            "Primary keys uniquely identify records and foreign keys "
            "maintain relationships between tables."
        )
    },
    {
        "subject": "Data Structures",
        "topic": "Arrays and Linked Lists",
        "content": (
            "An array stores elements in contiguous memory locations "
            "and supports index-based access. A linked list stores "
            "elements in nodes connected using references. Arrays provide "
            "fast indexed access, while linked lists support flexible "
            "insertion and deletion."
        )
    },
    {
        "subject": "Web Development",
        "topic": "HTML CSS JavaScript",
        "content": (
            "HTML defines the structure of a web page. CSS controls "
            "presentation, layout and styling. JavaScript adds dynamic "
            "behavior and interactivity. Together they form the foundation "
            "of frontend web development."
        )
    }
]


# --------------------------------------------------
# 2. RETRIEVAL: FIND RELEVANT KNOWLEDGE
# --------------------------------------------------

def tokenize(text):
    return set(re.findall(r"[a-zA-Z0-9]+", text.lower()))


def retrieve_knowledge(query, recent_logs):
    query_text = query or ""

    # Include actual logged subjects and topics in retrieval query.
    for log in recent_logs:
        query_text += " "
        query_text += str(log.get("subject", ""))
        query_text += " "
        query_text += str(log.get("topic", ""))

    query_words = tokenize(query_text)

    if not query_words:
        return []

    scored_documents = []

    for document in KNOWLEDGE_BASE:
        document_text = (
            document["subject"] + " " +
            document["topic"] + " " +
            document["content"]
        )

        document_words = tokenize(document_text)

        score = len(query_words.intersection(document_words))

        if score > 0:
            scored_documents.append((score, document))

    scored_documents.sort(
        key=lambda item: item[0],
        reverse=True
    )

    return [
        document
        for score, document in scored_documents[:3]
    ]


# --------------------------------------------------
# 3. STUDENT ACTIVITY CONTEXT
# --------------------------------------------------

def build_context(recent_logs):
    if not recent_logs:
        return "No recent study activity has been logged."

    lines = []

    for log in recent_logs[-5:]:
        lines.append(
            f"- Date: {log.get('date', 'Not provided')}, "
            f"Subject: {log.get('subject', 'Not provided')}, "
            f"Topic: {log.get('topic', 'Not provided')}, "
            f"Hours: {log.get('hours', 'Not provided')}"
        )

    return "Student's actual recent activity:\n" + "\n".join(lines)


# --------------------------------------------------
# 4. LLM RESPONSE WITH RETRIEVED CONTEXT
# --------------------------------------------------

def canned_reply(student_message):
    return (
        "Hey! I'm having trouble connecting to my AI model right now. "
        "Your study activity is still available. Please try again shortly."
    )


def generate_reply(recent_logs, student_message):

    retrieved_docs = retrieve_knowledge(
        student_message,
        recent_logs
    )

    activity_context = build_context(recent_logs)

    if retrieved_docs:
        knowledge_context = "\n\n".join(
            [
                f"Subject: {doc['subject']}\n"
                f"Topic: {doc['topic']}\n"
                f"Information: {doc['content']}"
                for doc in retrieved_docs
            ]
        )
    else:
        knowledge_context = (
            "No relevant information was found in the study knowledge base."
        )

    user_prompt = f"""
STUDENT ACTIVITY:
{activity_context}

RETRIEVED STUDY KNOWLEDGE:
{knowledge_context}

STUDENT MESSAGE:
{student_message}

INSTRUCTIONS:
1. Respond as Spark, a supportive study companion.
2. Use retrieved knowledge when it is relevant.
3. Never claim the student studied a topic unless it appears in their
   actual activity or message.
4. If no relevant knowledge was retrieved, clearly say so.
5. Do not invent facts, topics, marks or study history.
6. Give one practical and relevant study suggestion.
7. Keep the response within 2-4 sentences.
"""

    try:
        response = requests.post(
            f"{OLLAMA_BASE_URL}/api/chat",
            json={
                "model": OLLAMA_MODEL,
                "messages": [
                    {
                        "role": "system",
                        "content": (
                            "You are Spark, an AI study companion. "
                            "Be accurate, encouraging and concise. "
                            "Never invent student activity."
                        )
                    },
                    {
                        "role": "user",
                        "content": user_prompt
                    }
                ],
                "stream": False
            },
            timeout=90
        )

        response.raise_for_status()

        return response.json()["message"]["content"], retrieved_docs

    except requests.exceptions.RequestException as error:
        app.logger.error("Ollama request failed: %s", error)
        return canned_reply(student_message), []


# --------------------------------------------------
# 5. CHECK-IN API
# --------------------------------------------------

@app.route("/checkin", methods=["POST"])
def checkin():

    data = request.get_json(force=True) or {}

    recent_logs = data.get("recentLogs", [])
    student_message = data.get("studentMessage", "")

    reply, retrieved_docs = generate_reply(
        recent_logs,
        student_message
    )

    return jsonify({
        "reply": reply,
        "rag_enabled": True,
        "retrieved_context": [
            {
                "subject": doc["subject"],
                "topic": doc["topic"]
            }
            for doc in retrieved_docs
        ]
    })


# --------------------------------------------------
# 6. HEALTH API
# --------------------------------------------------

@app.route("/health", methods=["GET"])
def health():

    ollama_up = False

    try:
        response = requests.get(
            f"{OLLAMA_BASE_URL}/api/tags",
            timeout=3
        )
        ollama_up = response.status_code == 200

    except requests.exceptions.RequestException:
        pass

    return jsonify({
        "status": "ok",
        "ollama_reachable": ollama_up,
        "model": OLLAMA_MODEL,
        "rag_enabled": True,
        "knowledge_documents": len(KNOWLEDGE_BASE)
    })


if __name__ == "__main__":
    app.run(port=5000, debug=True)