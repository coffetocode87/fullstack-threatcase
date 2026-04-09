<template>
  <div ref="chartRef" class="chart-container"></div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from "vue";
import * as echarts from "echarts";
import api from "../api/axios";

const chartRef = ref(null);
let chart = null;

// =======================
// INIT CHART
// =======================
const initChart = () => {
  if (!chartRef.value) return;

  // dispose kalau ada instance lama
  if (chart && !chart.isDisposed()) {
    chart.dispose();
  }

  chart = echarts.init(chartRef.value);
};

// =======================
// LOAD DATA + RENDER
// =======================
const loadChart = async () => {
  try {
    const res = await api.get("/api/analytics/platforms");
    const raw = res.data || [];

    console.log("PLATFORM DATA:", raw);

    // mapping data aman (handle backend beda format)
    const data = raw.map((d) => ({
      name: d.platform || "Unknown",
      value: d.total ?? d.count ?? 0,
    }));

    initChart();

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

    // set option chart
    chart.setOption({
      title: {
        text: "Threats by Platform",
        left: "center",
      },

      tooltip: {
        trigger: "axis",
      },

      grid: {
        left: "3%",
        right: "4%",
        bottom: "10%",
        containLabel: true,
      },

      xAxis: {
        type: "category",
        data: data.map((d) => d.name),
      },

      yAxis: {
        type: "value",
      },

      series: [
        {
          name: "Threats",
          type: "bar",
          data: data.map((d) => d.value),

          label: {
            show: true,
            position: "top",
          },

          emphasis: {
            focus: "series",
          },
        },
      ],

      animation: true,
      animationDuration: 800,
    });
  } catch (err) {
    console.error("Platform chart error:", err);
  }
};

// =======================
// RESIZE HANDLER
// =======================
const handleResize = () => {
  if (chart && !chart.isDisposed()) {
    setTimeout(() => {
      chart.resize();
    }, 100);
  }
};

// =======================
// LIFECYCLE
// =======================
onMounted(async () => {
  await nextTick(); // pastikan DOM ready
  await loadChart();

  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  if (chart && !chart.isDisposed()) {
    chart.dispose();
  }

  window.removeEventListener("resize", handleResize);
});
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 400px; /* WAJIB */
}
</style>
