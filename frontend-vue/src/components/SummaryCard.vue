<script setup>
const props = defineProps({
  title: String,
  value: [String, Number],
});
</script>

<template>
  <div class="card">
    <h4 class="title">{{ title }}</h4>
    <p class="value">{{ value }}</p>
  </div>
</template>

<style scoped>
/* =========================
   CARD BASE
   ========================= */
.card {
  background: linear-gradient(135deg, #1f2937, #111827);
  color: #f9fafb;

  padding: 20px;
  border-radius: 16px;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  min-height: 110px;
  width: 100%;

  /* soft shadow */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);

  /* smooth hover */
  transition: all 0.3s ease;
}

/* =========================
   HOVER EFFECT (ELEGANT)
   ========================= */
.card:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
}

/* =========================
   TITLE
   ========================= */
.title {
  font-size: 13px;
  color: #9ca3af;
  margin-bottom: 8px;
  letter-spacing: 0.5px;
}

/* =========================
   VALUE (MAIN NUMBER)
   ========================= */
.value {
  font-size: 28px;
  font-weight: bold;
  color: #22c55e; /* 🔥 hijau cyber */
}

/* =========================
   OPTIONAL VARIANTS (BONUS)
   ========================= */
.card:nth-child(2) .value {
  color: #38bdf8; /* biru */
}

.card:nth-child(3) .value {
  color: #facc15; /* kuning */
}

.card:nth-child(4) .value {
  color: #fb7185; /* merah soft */
}
</style>
