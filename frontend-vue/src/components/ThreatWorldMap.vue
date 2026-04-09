<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from "vue";
import * as echarts from "echarts";
import world from "../assets/world.json";
import api from "../api/axios";

// register map
if (!echarts.getMap("world")) {
  echarts.registerMap("world", world);
}

const mapRef = ref(null);
let chart = null;

// =======================
// INIT CHART
// =======================
const initChart = () => {
  if (!mapRef.value) return;

  if (chart && !chart.isDisposed()) {
    chart.dispose();
  }

  chart = echarts.init(mapRef.value);
};

// =======================
// LOAD MAP
// =======================
const loadMap = async () => {
  try {
    const res = await api.get("/api/analytics/countries");
    const raw = res.data || [];

    console.log("MAP DATA:", raw);

    // mapping ISO code ke nama negara
    const countryMap = {
      ID: "Indonesia",
      US: "United States",
      SG: "Singapore",
      MY: "Malaysia",
      CN: "China",
      JP: "Japan",
    };

    const data = raw.map((c) => ({
      name: countryMap[c.country] || c.country, // 🔥 fix di sini
      value: c.total ?? 0,
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

    // 🔥 FIX UTAMA DI SINI
    const values = data.map((d) => d.value || 0);
    const maxValue = values.length ? Math.max(...values) : 1;

    chart.setOption({
      title: {
        text: "Threat Attacks by Country",
        left: "center",
      },

      tooltip: {
        trigger: "item",
      },

      visualMap: {
        min: 0,
        max: maxValue || 1,

        left: "left",
        bottom: 10,

        text: ["High", "Low"],
        calculable: true,

        // 🔥 WAJIB BANGET (INI YANG FIX ERROR)
        inRange: {
          color: ["#e0f3f8", "#08589e"],
        },
      },

      series: [
        {
          name: "Threats",
          type: "map",
          map: "world",
          roam: true,

          emphasis: {
            label: {
              show: true,
            },
          },

          data: data,
        },
      ],
    });
  } catch (err) {
    console.error("World map error:", err);
  }
};

// =======================
// RESIZE
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
  await nextTick();
  await loadMap();

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
  <div ref="mapRef" style="width: 100%; height: 500px"></div>
</template>
