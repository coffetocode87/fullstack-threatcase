<template>
  <div class="layout">
    <!-- SIDEBAR -->

    <aside class="sidebar">
      <h2>Threat SOC</h2>

      <nav>
        <router-link to="/dashboard">Dashboard</router-link>
        <router-link to="/threats">Threats</router-link>
      </nav>
    </aside>

    <!-- MAIN -->

    <div class="main">
      <header class="header">
        <button @click="logout">Logout</button>
      </header>

      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const logout = () => {
  localStorage.removeItem("token");
  router.push("/login");
};
</script>

<style>
.layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 220px;
  background: #111827;
  color: white;
  padding: 20px;
}

.sidebar h2 {
  margin-bottom: 20px;
}

.sidebar a {
  display: block;
  color: white;
  text-decoration: none;
  margin-bottom: 10px;
  padding: 8px;
  border-radius: 6px;
}

.sidebar a:hover {
  background: #1f2937;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  background: white;
  padding: 10px 20px;
  display: flex;
  justify-content: flex-end;
  border-bottom: 1px solid #e5e7eb;
}

.content {
  flex: 1;
  background: #f3f4f6;
  overflow: auto;
  padding: 20px;
}

.router-link-active {
  background: #374151;
}
</style>
