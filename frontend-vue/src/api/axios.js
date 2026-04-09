import axios from "axios";

const api = axios.create({
  baseURL: "https://webhook-threatcast-springboot.rmldev.my.id/",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token"); // ✅ FIX

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default api;