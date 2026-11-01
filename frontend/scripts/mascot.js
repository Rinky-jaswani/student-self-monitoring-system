/**
 * Spark mascot controller.
 * In production, mood is driven by GET /api/streaks?studentId= (see the
 * architecture doc's API contract). This demo simulates that with sample data.
 */

const ENCOURAGEMENT = {
  idle: [
    "Ready when you are.",
    "Log today's session whenever you're set.",
    "Let's keep the momentum going.",
  ],
  happy: [
    "Streak's alive — nice work!",
    "That's a new personal best.",
    "You're on a roll today.",
    "Look at that streak grow!",
  ],
  sad: [
    "Streak reset — happens to everyone. Log today to start a new one.",
    "No streak right now, but every day is a fresh start.",
  ],
};

function pick(list) {
  return list[Math.floor(Math.random() * list.length)];
}

/**
 * Sets mood on every .mascot element that shares a data-mascot-group,
 * and updates any linked speech bubble text.
 */
function setMascotMood(mood, groupId) {
  const mascots = groupId
    ? document.querySelectorAll(`.mascot[data-mascot-group="${groupId}"]`)
    : document.querySelectorAll('.mascot');

  mascots.forEach((el) => {
    el.setAttribute('data-mood', mood);
  });

  document.querySelectorAll(`[data-speech-for="${groupId}"]`).forEach((bubble) => {
    bubble.textContent = pick(ENCOURAGEMENT[mood] || ENCOURAGEMENT.idle);
  });
}

/**
 * Call this when a student logs a new activity, to reflect the
 * updated streak state immediately without waiting for a page reload.
 */
function celebrateNewEntry(groupId) {
  setMascotMood('happy', groupId);
  const mascotEl = document.querySelector(`.mascot[data-mascot-group="${groupId}"]`);
  if (!mascotEl) return;
  mascotEl.classList.add('celebrate-burst');
  setTimeout(() => mascotEl.classList.remove('celebrate-burst'), 900);
}

// Expose globally for inline page scripts
window.setMascotMood = setMascotMood;
window.celebrateNewEntry = celebrateNewEntry;
