<script setup>
import { ref, onMounted, watch } from "vue";
import api from "../api/axios";

import SummaryCard from "../components/SummaryCard.vue";
import ThreatChart from "../components/ThreatChart.vue";
import PlatformChart from "../components/PlatformChart.vue";
import CountryChart from "../components/CountryChart.vue";
import RecentThreatTable from "../components/RecentThreatTable.vue";
import TopThreatRulesChart from "../components/TopThreatRulesChart.vue";
import ThreatWorldMap from "../components/ThreatWorldMap.vue";
import TimeFilter from "../components/TimeFilter.vue";

const summary = ref({
  totalThreats: 0,
  todayThreats: 0,
  topThreatType: "-",
  topCountry: "-",
});

const days = ref(7);

// 🔥 LOAD DASHBOARD
const loadDashboard = async () => {
  try {
    const { data } = await api.get(`/api/dashboard/summary?days=${days.value}`);

    summary.value = {
      totalThreats: Number(data.totalThreats ?? 0),
      todayThreats: Number(data.todayThreats ?? 0),
      topThreatType: data.topThreatType ?? "-",
      topCountry: data.topCountry ?? "-",
    };
  } catch (err) {
    console.error("ERROR LOAD DASHBOARD:", err);
  }
};

// 🔥 HANDLE TIME FILTER
const changeTimeRange = (value) => {
  console.log("CHANGE DAYS:", value);
  days.value = Number(value); // 🔥 pastikan number
};

// 🔥 AUTO RELOAD SAAT DAYS BERUBAH
watch(days, () => {
  loadDashboard();
});

// INIT
onMounted(() => {
  loadDashboard();
});
</script>

<template>
  <div class="dashboard">
    <h1>Threat Intelligence Dashboard</h1>

    <!-- 🔥 TIME FILTER -->
    <TimeFilter @change="changeTimeRange" />

    <!-- SUMMARY -->
    <div class="cards">
      <SummaryCard title="Total Threats" :value="summary.totalThreats" />
      <SummaryCard title="Today Threats" :value="summary.todayThreats" />
      <SummaryCard title="Top Threat" :value="summary.topThreatType" />
      <SummaryCard title="Top Country" :value="summary.topCountry" />
    </div>

    <!-- CHARTS -->
    <div class="charts">
      <div class="chart">
        <ThreatChart :days="days" />
      </div>

      <div class="chart">
        <PlatformChart :days="days" />
      </div>

      <div class="chart">
        <CountryChart :days="days" />
      </div>

      <div class="chart">
        <TopThreatRulesChart :days="days" />
      </div>
    </div>

    <!-- MAP -->
    <div class="chart full-width">
      <ThreatWorldMap :days="days" />
    </div>

    <!-- TABLE -->
    <div class="table">
      <RecentThreatTable :days="days" />
    </div>
  </div>
</template>

<style>
/* =========================
   GLOBAL DASHBOARD
   ========================= */
.dashboard {
  padding: 30px;
  font-family: Arial;
  background: #f3f4f6;
}

/* =========================
   SUMMARY CARDS (FIX UTAMA)
   ========================= */
.cards {
  display: flex;   /* 🔥 GANTI DARI GRID */
  gap: 20px;
  margin-bottom: 30px;
}

.cards > * {
  flex: 1;
}

/* =========================
   CHARTS GRID
   ========================= */
.charts {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 30px;
}

/* =========================
   CHART BOX
   ========================= */
.chart {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

  position: relative;
  z-index: 1;
}

/* FULL WIDTH CHART (MAP) */
.full-width {
  grid-column: span 2;
  margin-bottom: 30px;
}

/* =========================
   TABLE
   ========================= */
.table {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

  position: relative;
  z-index: 1;
}
</style>
