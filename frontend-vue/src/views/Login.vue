<template>
  <div class="login-page">
    <div class="login-card">
      <h2>🔐 Threat Intelligence Pantek</h2>
      <p class="subtitle">Secure Access Portal</p>

      <div class="input-group">
        <input v-model="username" placeholder="Username" />
      </div>

      <div class="input-group">
        <input v-model="password" type="password" placeholder="Password" />
      </div>

      <button @click="login" :disabled="loading">
        <span v-if="!loading">Login</span>
        <span v-else class="loading">Logging in...</span>
      </button>

      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import api from "../api/axios";

const router = useRouter();

const username = ref("");
const password = ref("");
const loading = ref(false);
const error = ref("");

const login = async () => {
  try {
    loading.value = true;
    error.value = "";

    console.log("Request login dikirim...");

    const res = await api.post("/auth/login", {
      username: username.value,
      password: password.value,
    });

    console.log("Response:", res);

    localStorage.setItem("token", res.data.token);

    console.log("Token disimpan:", res.data.token);

    router.push("/dashboard");
  } catch (err) {
    console.error("LOGIN ERROR:", err);

    error.value =
      err.response?.data?.message ||
      err.message ||
      "Login failed. Check username/password.";
  } finally {
    loading.value = false;
  }
};
</script>

<style>
/* =========================
   BACKGROUND
   ========================= */
.login-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;

  background: radial-gradient(circle at top, #0f172a, #020617);
  font-family: "Inter", Arial, sans-serif;
}

/* =========================
   CARD
   ========================= */
.login-card {
  width: 350px;
  padding: 40px;

  background: rgba(15, 23, 42, 0.8);
  backdrop-filter: blur(15px);

  border-radius: 20px;

  box-shadow:
    0 0 30px rgba(56, 189, 248, 0.2),
    0 20px 60px rgba(0, 0, 0, 0.7);

  text-align: center;

  border: 1px solid rgba(255, 255, 255, 0.05);

  animation: fadeIn 0.8s ease;
}

/* TITLE */
.login-card h2 {
  margin-bottom: 5px;
  font-size: 24px;
  color: #38bdf8;
}

/* SUBTITLE */
.subtitle {
  font-size: 13px;
  color: #94a3b8;
  margin-bottom: 25px;
}

/* =========================
   INPUT
   ========================= */
.input-group {
  margin-bottom: 15px;
}

.input-group input {
  width: 100%;
  padding: 12px 14px;

  border-radius: 10px;
  border: 1px solid #334155;

  background: #020617;
  color: #e2e8f0;

  outline: none;
  transition: 0.3s;
}

.input-group input:focus {
  border-color: #38bdf8;
  box-shadow: 0 0 10px rgba(56, 189, 248, 0.5);
}

/* =========================
   BUTTON
   ========================= */
button {
  width: 100%;
  padding: 12px;

  border-radius: 10px;
  border: none;

  background: linear-gradient(90deg, #38bdf8, #6366f1);
  color: white;

  font-weight: 600;
  cursor: pointer;

  transition: 0.3s;
}

button:hover {
  transform: translateY(-2px);
  box-shadow: 0 0 20px rgba(99, 102, 241, 0.6);
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* =========================
   ERROR
   ========================= */
.error {
  margin-top: 10px;
  color: #f87171;
  font-size: 13px;
}

/* =========================
   LOADING EFFECT
   ========================= */
.loading {
  animation: blink 1s infinite;
}

@keyframes blink {
  50% {
    opacity: 0.5;
  }
}

/* =========================
   ANIMATION
   ========================= */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
