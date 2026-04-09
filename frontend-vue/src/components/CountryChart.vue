<template>
  <div ref="chartRef" style="width: 100%; height: 400px"></div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import * as echarts from "echarts";
import api from "../api/axios";

const chartRef = ref(null);
let chart = null;

// Resize handler
const handleResize = () => {
  if (chart) chart.resize();
};

// Load chart
const loadChart = async () => {
  try {
    const res = await api.get("/api/analytics/countries");
    const raw = res.data || [];

    console.log("COUNTRY DATA:", raw); // 🔥 debug wajib

    // mapping data (safe)
    const data = raw.map((d) => ({
      value: d.total || 0,
      name: d.country || "Unknown",
    }));

    // dispose chart lama
    if (chart) {
      chart.dispose();
    }

    // pastikan DOM ready
    if (!chartRef.value) return;

    chart = echarts.init(chartRef.value);

    // ❗ handle kalau kosong
    if (data.length === 0) {
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
        text: "Threats by Country",
        left: "center",
      },

      tooltip: {
        trigger: "item",
      },

      legend: {
        bottom: 0,
      },

      series: [
        {
          name: "Threats",
          type: "pie",
          radius: "60%",
          data: data,

          // 🔥 biar lebih bagus
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
            },
          },

          label: {
            formatter: "{b}: {c}",
          },
        },
      ],
    });
  } catch (err) {
    console.error("Country chart load failed", err);
  }
};

// Lifecycle
onMounted(() => {
  loadChart();
  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  if (chart) {
    chart.dispose();
  }
  window.removeEventListener("resize", handleResize);
});
</script>
