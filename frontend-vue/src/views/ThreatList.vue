<template>
  <div class="page">
    <h2>Threat List</h2>

    <!-- FILTER -->
    <div class="filters">
      <input v-model="ruleId" placeholder="Rule ID" />
      <input v-model="threatType" placeholder="Threat Type" />
      <button @click="search">Search</button>
    </div>

    <!-- TABLE -->
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Rule</th>
          <th>Type</th>
          <th>Platform</th>
          <th>Country</th>
          <th>Date</th>
          <th>Severity</th>
          <th>Action</th>
        </tr>
      </thead>

      <tbody>
        <!-- Loading -->
        <tr v-if="loading">
          <td colspan="8">Loading...</td>
        </tr>

        <!-- Error -->
        <tr v-else-if="error">
          <td colspan="8">Failed to load data</td>
        </tr>

        <!-- Empty -->
        <tr v-else-if="threats.length === 0">
          <td colspan="8">No data found</td>
        </tr>

        <!-- Data -->
        <tr v-else v-for="t in threats" :key="t.id">
          <td>{{ t.id }}</td>
          <td>{{ t.ruleId }}</td>
          <td>{{ t.threatType }}</td>
          <td>{{ t.platform }}</td>
          <td>{{ t.country }}</td>
          <td>{{ t.threatTimestamp }}</td>
          <td>
            <SeverityBadge :severity="t.severity" />
          </td>
          <td>
            <button @click="viewDetail(t.id)">View</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- PAGINATION -->
    <div class="pagination">
      <button @click="prevPage" :disabled="page === 0">Prev</button>

      <span>Page {{ page + 1 }} / {{ totalPages }}</span>

      <button @click="nextPage" :disabled="page >= totalPages - 1">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import api from "../api/axios";
import SeverityBadge from "../components/SeverityBadge.vue";

const router = useRouter();

const threats = ref([]);
const loading = ref(false);
const error = ref(false);

const page = ref(0);
const size = 10;
const totalPages = ref(0);

const ruleId = ref("");
const threatType = ref("");

// 🔥 format tanggal biar rapi
const formatDate = (date) => {
  if (!date) return "-";
  return new Date(date).toLocaleString();
};

// 🔥 LOAD DATA
const loadThreats = async () => {
  loading.value = true;
  error.value = false;

  try {
    const params = {
      page: page.value,
      size: size,
    };

    // 🔥 kirim hanya kalau ada isi
    if (ruleId.value.trim() !== "") {
      params.ruleId = ruleId.value;
    }

    if (threatType.value.trim() !== "") {
      params.threatType = threatType.value;
    }

    console.log("PARAMS:", params);

    const res = await api.get("/api/threats", { params });

    console.log("API RESULT:", res.data);

    const raw = res.data.content || [];

    // 🔥 mapping biar aman
    threats.value = raw.map((t) => ({
      id: t.id,
      ruleId: t.ruleId || "-",
      threatType: t.threatType || "Unknown",
      platform: t.platform || t.os || "-", // 🔥 FIX DI SINI
      country: t.country || "-",
      threatTimestamp: formatDate(t.threatTimestamp),
      severity: t.severity || "LOW",
    }));

    totalPages.value = res.data.totalPages || 1;
  } catch (err) {
    console.error("Failed load threats", err);
    error.value = true;
  } finally {
    loading.value = false;
  }
};

// 🔥 SEARCH RESET PAGE
const search = () => {
  page.value = 0;
  loadThreats();
};

// 🔥 PAGINATION
const nextPage = () => {
  if (page.value < totalPages.value - 1) {
    page.value++;
    loadThreats();
  }
};

const prevPage = () => {
  if (page.value > 0) {
    page.value--;
    loadThreats();
  }
};

// 🔥 DETAIL
const viewDetail = (id) => {
  router.push(`/threats/${id}`);
};

onMounted(loadThreats);
</script>

<style>
/* =========================
   PAGE
   ========================= */
.page {
  padding: 30px;
  font-family: "Inter", sans-serif;
  background: #0f172a;
  color: #e5e7eb;
  min-height: 100vh;
}

/* =========================
   TITLE
   ========================= */
.page h2 {
  font-size: 24px;
  margin-bottom: 20px;
  font-weight: 600;
}

/* =========================
   FILTER BOX
   ========================= */
.filters {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;

  background: #111827;
  padding: 15px;
  border-radius: 12px;

  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.filters input {
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #374151;
  background: #1f2937;
  color: white;
  outline: none;
  transition: 0.2s;
}

.filters input:focus {
  border-color: #38bdf8;
  box-shadow: 0 0 0 1px #38bdf8;
}

.filters button {
  padding: 10px 16px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #22c55e, #16a34a);
  color: white;
  cursor: pointer;
  font-weight: 600;
  transition: 0.3s;
}

.filters button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(34, 197, 94, 0.4);
}

/* =========================
   TABLE CONTAINER
   ========================= */
table {
  width: 100%;
  border-collapse: collapse;
  background: #111827;
  border-radius: 12px;
  overflow: hidden;

  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

/* =========================
   HEADER
   ========================= */
th {
  text-align: left;
  padding: 14px;
  background: #1f2937;
  color: #9ca3af;
  font-size: 13px;
  letter-spacing: 0.5px;
}

/* =========================
   ROWS
   ========================= */
td {
  padding: 14px;
  border-bottom: 1px solid #1f2937;
  font-size: 14px;
}

/* =========================
   HOVER EFFECT
   ========================= */
tr:hover td {
  background: #1f2937;
  transition: 0.2s;
}

/* =========================
   BUTTON ACTION
   ========================= */
td button {
  padding: 6px 12px;
  border-radius: 6px;
  border: none;
  background: linear-gradient(135deg, #38bdf8, #0ea5e9);
  color: white;
  cursor: pointer;
  transition: 0.2s;
}

td button:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 10px rgba(56, 189, 248, 0.4);
}

/* =========================
   STATUS ROW (LOADING / ERROR)
   ========================= */
tbody tr td[colspan] {
  text-align: center;
  color: #9ca3af;
  padding: 20px;
}

/* =========================
   PAGINATION
   ========================= */
.pagination {
  margin-top: 20px;

  display: flex;
  justify-content: space-between;
  align-items: center;

  background: #111827;
  padding: 12px 16px;
  border-radius: 10px;
}

.pagination button {
  padding: 8px 14px;
  border-radius: 8px;
  border: none;
  background: #1f2937;
  color: white;
  cursor: pointer;
  transition: 0.2s;
}

.pagination button:hover:not(:disabled) {
  background: #374151;
}

.pagination button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.pagination span {
  font-size: 14px;
  color: #9ca3af;
}
</style>
