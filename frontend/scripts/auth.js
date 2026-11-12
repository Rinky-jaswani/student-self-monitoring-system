/**
 * StudySpark shared auth helpers (plain JavaScript, no framework).
 * Loaded by auth.html and dashboard.html.
 */

const API_BASE = 'http://localhost:8080';
const TOKEN_KEY = 'studyspark_token';
const STUDENT_KEY = 'studyspark_student';

function getToken() {
  try { return localStorage.getItem(TOKEN_KEY); } catch (e) { return null; }
}

function getStudent() {
  try { return JSON.parse(localStorage.getItem(STUDENT_KEY) || 'null'); } catch (e) { return null; }
}

function saveSession(authResponse) {
  localStorage.setItem(TOKEN_KEY, authResponse.accessToken);
  localStorage.setItem(STUDENT_KEY, JSON.stringify(authResponse.student || {}));
}

function clearSession() {
  try {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(STUDENT_KEY);
  } catch (e) { /* ignore */ }
}

function logout() {
  clearSession();
  window.location.replace('auth.html');
}

/** Call at the top of any protected page. Redirects to login if no token. */
function requireAuth() {
  if (!getToken()) {
    document.documentElement.style.display = 'none';
    window.location.replace('auth.html');
    return false;
  }
  return true;
}

/**
 * fetch() wrapper: adds JSON headers and "Authorization: Bearer <token>",
 * parses the JSON body, and throws an Error with .status / .fields / .network.
 * A 401 on a protected endpoint clears the token and returns to the login page.
 */
async function apiFetch(path, options = {}) {
  const headers = Object.assign({}, options.headers || {});
  if (options.body) headers['Content-Type'] = 'application/json';
  const token = getToken();
  if (token) headers['Authorization'] = 'Bearer ' + token;

  let res;
  try {
    res = await fetch(API_BASE + path, Object.assign({}, options, { headers }));
  } catch (e) {
    const err = new Error('Cannot reach the server. Make sure the backend is running on ' + API_BASE + '.');
    err.network = true;
    throw err;
  }

  let data = null;
  const text = await res.text();
  if (text) { try { data = JSON.parse(text); } catch (e) { data = null; } }

  if (res.status === 401 && token && !path.startsWith('/api/auth/')) {
    clearSession();
    window.location.replace('auth.html');
  }

  if (!res.ok) {
    const err = new Error((data && data.error) || ('Request failed (' + res.status + ').'));
    err.status = res.status;
    err.fields = (data && data.fields) || null;
    throw err;
  }
  return data;
  
}
