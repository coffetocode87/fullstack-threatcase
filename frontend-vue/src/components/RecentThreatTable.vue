<template>
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Type</th>
        <th>Country</th>
        <th>Platform</th>
        <th>Severity</th>
      </tr>
    </thead>

    <tbody>
      <!-- Loading -->
      <tr v-if="loading">
        <td colspan="5">Loading data...</td>
      </tr>

      <!-- Error -->
      <tr v-else-if="error">
        <td colspan="5">Failed to load data</td>
      </tr>

      <!-- Empty -->
      <tr v-else-if="threats.length === 0">
        <td colspan="5">No threats found</td>
      </tr>

      <!-- Data -->
      <tr v-else v-for="t in threats" :key="t.id">
        <td>{{ t.id }}</td>
        <td>{{ t.type }}</td> 
        <td>{{ t.country }}</td>
        <td>{{ t.platform }}</td>
        <td>
          <SeverityBadge :severity="t.severity" />
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "../api/axios";
import SeverityBadge from "./SeverityBadge.vue";

const threats = ref([]);
const loading = ref(true);
const error = ref(false);

// mapping country biar bagus (opsional)
const countryMap = {
  ID: "Indonesia",
};

const loadThreats = async () => {
  loading.value = true;
  error.value = false;

  try {
    const res = await api.get("/api/threats/recent");
    const raw = res.data || [];

    console.log("THREATS RAW:", raw); // 🔥 debug

    // 🔥 mapping biar aman
    threats.value = raw.map((t) => ({
      id: t.id,
      type: t.threatType || "Unknown",
      country: countryMap[t.country] || t.country || "-",
      platform: t.platform || t.os || "-", // 🔥 FIX DI SINI
      severity: t.severity || "LOW", // fallback kalau belum ada
    }));
  } catch (err) {
    console.error("Recent threats error", err);
    error.value = true;
  } finally {
    loading.value = false;
  }
};

onMounted(loadThreats);
</script>

<style>
table {
  width: 100%;
  border-collapse: collapse;
}

th {
  text-align: left;
  padding: 10px;
  background: #f3f4f6;
}

td {
  padding: 10px;
  border-bottom: 1px solid #eee;
}

tr:hover {
  background: #f9fafb;
}
</style>
