<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from "vue";
import * as echarts from "echarts";
import api from "../api/axios";

const chartRef = ref(null);
let chart = null;

// =======================
// RESIZE
// =======================
const handleResize = () => {
  if (chart && !chart.isDisposed()) {
    chart.resize();
  }
};

// =======================
// LOAD CHART
// =======================
const loadChart = async () => {
  try {
    const res = await api.get("/api/analytics/threat-types");
    let raw = res.data || [];

    console.log("THREAT TYPES RAW:", raw); // 🔥 DEBUG WAJIB

    // 🔥 SORT AMAN (biar gak undefined)
    raw = raw.sort(
      (a, b) => (b.count || b.total || 0) - (a.count || a.total || 0),
    );

    // 🔥 MAPPING SUPER AMAN
    const data = raw.map((d) => ({
      name: d.threatType || d.type || d.threat_type || "Unknown",

      value: d.count || d.total || d.value || 0,
    }));

    // dispose chart lama
    if (chart && !chart.isDisposed()) {
      chart.dispose();
    }

    if (!chartRef.value) return;

    chart = echarts.init(chartRef.value);

    // kalau kosong
    if (!data.length) {
      chart.setOption({
        title: {
          text: "No Data Available",
          left: "center",
          top: "center",
        },
      });
      return;
    }

    chart.setOption({
      title: {
        text: "Top Threat Rules",
        left: "center",
      },

      tooltip: {
        trigger: "axis",
      },

      grid: {
        left: 100, // 🔥 biar label panjang gak kepotong
      },

      xAxis: {
        type: "value",
      },

      yAxis: {
        type: "category",
        data: data.map((d) => d.name),
      },

      series: [
        {
          name: "Threats",
          type: "bar",
          data: data.map((d) => d.value),

          label: {
            show: true,
            position: "right",
          },

          emphasis: {
            focus: "series",
          },
        },
      ],

      animation: true,
    });
  } catch (err) {
    console.error("Top threat rules chart error", err);
  }
};

// =======================
// LIFECYCLE
// =======================
onMounted(async () => {
  await nextTick(); // 🔥 penting biar DOM siap
  loadChart();
  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  if (chart && !chart.isDisposed()) {
    chart.dispose();
  }

  window.removeEventListener("resize", handleResize);
});
</script>

<template>
  <div ref="chartRef" style="width: 100%; height: 400px"></div>
</template>
