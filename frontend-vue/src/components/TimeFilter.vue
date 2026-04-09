<script setup>
import { ref } from "vue";

const emit = defineEmits(["change"]);

const selected = ref(7);

const handleChange = (e) => {
  const value = Number(e.target.value);
  selected.value = value;

  emit("change", value); // 🔥 WAJIB
};
</script>

<template>
  <select @change="handleChange" :value="selected">
    <option :value="7">Last 7 Days</option>
    <option :value="30">Last 30 Days</option>
    <option :value="90">Last 90 Days</option>
  </select>
</template>
