<script setup>
import { computed } from "vue";

const props = defineProps({
  severity: {
    type: String,
    default: "UNKNOWN",
  },
});

// Normalize severity
const normalizedSeverity = computed(() =>
  (props.severity || "UNKNOWN").toUpperCase(),
);

// Class mapping
const severityClass = computed(() => {
  switch (normalizedSeverity.value) {
    case "CRITICAL":
      return "critical";
    case "HIGH":
      return "high";
    case "MEDIUM":
      return "medium";
    case "LOW":
      return "low";
    default:
      return "unknown";
  }
});
</script>

<template>
  <span :class="['badge', severityClass]">
    {{ normalizedSeverity }}
  </span>
</template>

<style>
.badge {
  padding: 4px 10px;
  border-radius: 6px;
  color: white;
  font-size: 12px;
  font-weight: bold;
}

/* Known severity */
.critical {
  background: #dc2626;
}

.high {
  background: #ea580c;
}

.medium {
  background: #ca8a04;
}

.low {
  background: #16a34a;
}

/* Fallback */
.unknown {
  background: #6b7280;
}
</style>
