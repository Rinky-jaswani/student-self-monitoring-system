"""
StudySpark AI microservice (Flask).

Currently implements just /checkin (see docs/system_architecture_and_db_design.md
section 8.1). /analyze, /chat, /suggest-resources, /career-roadmap follow the
same pattern once you're ready to build them (§9 build order).

Uses Ollama (https://ollama.com) to run an LLM locally — no API key, no cost,
works offline once the model is downloaded. If Ollama isn't running or the
model isn't pulled yet, this falls back to a canned reply so the rest of the
app (Java <-> Flask <-> frontend) is still fully testable.
"""

import os
import requests
from flask import Flask, request, jsonify
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

OLLAMA_BASE_URL = os.environ.get("OLLAMA_BASE_URL", "http://localhost:11434")
# llama3.2 (3B) is a good default — small enough to run on a normal laptop
# (~2GB). If your machine can handle more, try "llama3.1" (8B) for better replies.
OLLAMA_MODEL = os.environ.get("OLLAMA_MODEL", "llama3.2")

SYSTEM_PROMPT = (
    "You are Spark, a warm, encouraging AI study companion inside a student "
    "self-monitoring app. You are not a customer support bot — you sound like "
    "a supportive friend who has been paying attention to what the student has "
    "been studying. Keep replies short: 2-4 sentences. Reference their actual "
    "recent activity specifically rather than speaking generically. End with "
    "one light, open question that invites them to say more about their day."
)


def build_context(recent_logs):
    """Turn the student's recent activity into a short text block for the prompt."""
    if not recent_logs:
        return "The student hasn't logged any study activity in the last few days."
    lines = [
        f"- {log.get('date')}: {log.get('hours')}h on {log.get('subject')} ({log.get('topic')})"
        for log in recent_logs[-5:]
    ]
    return "Recent study activity:\n" + "\n".join(lines)


def canned_reply(student_message):
    """
    Used when Ollama isn't reachable (not installed, not running, or the
    model hasn't been pulled yet), so the rest of the app is still testable.
    """
    if student_message:
        return (
            "Hey! (Demo mode — Ollama isn't running, see README for setup.) "
            "Thanks for sharing that. Based on your recent logs, you're building "
            "a solid routine — what felt hardest today?"
        )
    return (
        "Hey! (Demo mode — Ollama isn't running, see README for setup.) "
        "How did today's studying go?"
    )


def generate_reply(recent_logs, student_message):
    context = build_context(recent_logs)
    user_prompt = (
        f"{context}\n\nStudent says: "
        f"\"{student_message or '(no message yet, just opened the app)'}\"\n\n"
        f"Respond as Spark."
    )

    try:
        response = requests.post(
            f"{OLLAMA_BASE_URL}/api/chat",
            json={
                "model": OLLAMA_MODEL,
                "messages": [
                    {"role": "system", "content": SYSTEM_PROMPT},
                    {"role": "user", "content": user_prompt},
                ],
                "stream": False,
            },
            timeout=60,  # local generation can be slow on the first request/CPU-only machines
        )
        response.raise_for_status()
        return response.json()["message"]["content"]
    except requests.exceptions.ConnectionError:
        app.logger.warning("Ollama not reachable at %s — is `ollama serve` running?", OLLAMA_BASE_URL)
        return canned_reply(student_message)
    except requests.exceptions.RequestException as e:
        app.logger.error(f"Ollama request failed: {e}")
        return canned_reply(student_message)


@app.route("/checkin", methods=["POST"])
def checkin():
    data = request.get_json(force=True) or {}
    recent_logs = data.get("recentLogs", [])
    student_message = data.get("studentMessage", "")

    reply = generate_reply(recent_logs, student_message)
    return jsonify({"reply": reply})


@app.route("/health", methods=["GET"])
def health():
    ollama_up = False
    try:
        r = requests.get(f"{OLLAMA_BASE_URL}/api/tags", timeout=3)
        ollama_up = r.status_code == 200
    except requests.exceptions.RequestException:
        pass

    return jsonify({
        "status": "ok",
        "ollama_reachable": ollama_up,
        "model": OLLAMA_MODEL,
    })


if __name__ == "__main__":
    app.run(port=5000, debug=True)
