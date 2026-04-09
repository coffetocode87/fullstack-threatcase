<template>
  <div class="page">
    <h2>Threat Detail</h2>

    <button @click="goBack">⬅ Back</button>

    <!-- LOADING -->
    <div v-if="loading">Loading...</div>

    <!-- ERROR -->
    <div v-else-if="error">Failed to load</div>

    <!-- DATA -->
    <div v-else class="card">
      <div class="section">
        <h3>Basic Info</h3>
        <p><b>Rule ID:</b> {{ data.ruleId }}</p>
        <p><b>Rule Name:</b> {{ data.ruleName }}</p>
      </div>

      <div class="section">
        <h3>Application</h3>
        <p><b>App:</b> {{ data.appName }}</p>
      </div>

      <div class="section">
        <h3>Threat</h3>
        <p><b>Type:</b> {{ data.threatType }}</p>
        <p><b>ID:</b> {{ data.threatId }}</p>
      </div>

      <div class="section">
        <h3>Device</h3>
        <p><b>OS:</b> {{ data.os }} {{ data.osVersion }}</p>
        <p><b>Device:</b> {{ data.deviceModel }}</p>
      </div>

      <pre>{{ data.raw }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import api from "../api/axios";

const route = useRoute();
const router = useRouter();

const data = ref(null);
const loading = ref(true);
const error = ref(false);

// 🔥 LOAD + PARSE SEKALIGUS (NO COMPUTED LAGI)
const loadThreat = async () => {
  loading.value = true;
  error.value = false;

  try {
    const id = route.params.id;

    const res = await api.get(`/api/threats/${id}`);

    console.log("FULL:", res.data);

    let raw = res.data.rawJson;

    let parsed = {};

    try {
      parsed = JSON.parse(raw);

      if (typeof parsed === "string") {
        parsed = JSON.parse(parsed);
      }
    } catch (e) {
      console.error("JSON ERROR:", e);
    }

    const t = parsed.threats?.[0] || {};

    // 🔥 SATU OBJECT FINAL
    data.value = {
      ruleId: parsed.ruleId || "-",
      ruleName: parsed.ruleName || "-",
      appName: parsed.appName || "-",

      threatType: t.threatType || "-",
      threatId: t.id || "-",

      os: t.os || "-",
      osVersion: t.osVersion || "-",

      deviceModel: t.deviceModel || "-",

      raw: JSON.stringify(parsed, null, 2),
    };
  } catch (err) {
    console.error("ERROR:", err);
    error.value = true;
  } finally {
    loading.value = false;
  }
};

const goBack = () => {
  router.push("/threats");
};

onMounted(loadThreat);
</script>

<style>
/* =========================
   PAGE
   ========================= */
.page {
  padding: 30px;
  background: #0f172a;
  color: #e5e7eb;
  min-height: 100vh;
  font-family: "Inter", sans-serif;
}

/* =========================
   TITLE + BACK
   ========================= */
.page h2 {
  font-size: 24px;
  margin-bottom: 15px;
}

.page button {
  background: #1f2937;
  color: white;
  border: none;
  padding: 8px 14px;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 20px;
  transition: 0.2s;
}

.page button:hover {
  background: #374151;
  transform: translateX(-3px);
}

/* =========================
   CARD CONTAINER
   ========================= */
.card {
  background: linear-gradient(135deg, #111827, #1f2937);
  padding: 25px;
  border-radius: 16px;

  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.5);

  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* =========================
   SECTION
   ========================= */
.section {
  background: rgba(255, 255, 255, 0.03);
  padding: 15px;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

/* =========================
   SECTION TITLE
   ========================= */
.section h3 {
  margin-bottom: 10px;
  font-size: 14px;
  color: #9ca3af;
  letter-spacing: 0.5px;
}

/* =========================
   TEXT
   ========================= */
.section p {
  margin: 6px 0;
  font-size: 14px;
}

.section b {
  color: #38bdf8;
}

/* =========================
   RAW JSON (TERMINAL STYLE)
   ========================= */
pre {
  grid-column: span 2;

  background: #020617;
  color: #22c55e;

  padding: 20px;
  border-radius: 12px;

  overflow-x: auto;
  font-size: 13px;

  box-shadow: inset 0 0 20px rgba(0, 255, 100, 0.1);
}

/* =========================
   LOADING / ERROR
   ========================= */
.page div {
  font-size: 14px;
}

/* =========================
   RESPONSIVE
   ========================= */
@media (max-width: 768px) {
  .card {
    grid-template-columns: 1fr;
  }

  pre {
    grid-column: span 1;
  }
}
</style>
