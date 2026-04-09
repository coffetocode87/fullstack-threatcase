<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from "vue";
import * as echarts from "echarts";
import api from "../api/axios";

// props untuk dynamic filter (siap integrasi TimeFilter)
const props = defineProps({
  days: {
    type: Number,
    default: 7,
  },
});

const chartRef = ref(null);
let chart = null;

// Resize handler
const handleResize = () => {
  if (chart) chart.resize();
};

// Load chart
const loadChart = async () => {
  try {
    const res = await api.get(`/api/threats/stats/daily?days=${props.days}`);
    const data = res.data || [];

    // dispose chart lama
    if (chart) {
      chart.dispose();
    }

    chart = echarts.init(chartRef.value);

    // Handle empty data
    if (data.length === 0) {
      chart.setOption({
        title: {
          text: "No Data Available",
          left: "center",
        },
      });
      return;
    }

    const dates = data.map((d) => d.date);
    const counts = data.map((d) => d.count);

    chart.setOption({
      title: {
        text: "Threats Over Time",
      },

      tooltip: {
        trigger: "axis",
      },

      xAxis: {
        type: "category",
        data: dates,
      },

      yAxis: {
        type: "value",
      },

      series: [
        {
          name: "Threats",
          data: counts,
          type: "line",
          smooth: true,
          areaStyle: {}, // bikin lebih visual 🔥
        },
      ],
    });
  } catch (err) {
    console.error("Threat chart error", err);
  }
};

// Watch perubahan days (biar reactive)
watch(
  () => props.days,
  () => {
    loadChart();
  },
);

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

<template>
  <div ref="chartRef" style="width: 100%; height: 400px"></div>
</template>
