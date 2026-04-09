import { createRouter, createWebHistory } from "vue-router";

import Login from "../views/Login.vue";
import Dashboard from "../views/Dashboard.vue";
import ThreatList from "../views/ThreatList.vue";
import ThreatDetail from "../views/ThreatDetail.vue";
import Layout from "../components/Layout.vue";

const routes = [
  {
    path: "/login",
    component: Login,
  },

  {
    path: "/",
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: "",
        redirect: "/dashboard",
      },
      {
        path: "dashboard",
        component: Dashboard,
      },
      {
        path: "threats",
        component: ThreatList,
      },
      {
        path: "threats/:id",
        component: ThreatDetail,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// ✅ AUTH GUARD (VERSI BARU)
router.beforeEach((to) => {
  const token = localStorage.getItem("token");

  if (to.matched.some((record) => record.meta.requiresAuth) && !token) {
    return "/login";
  }

  if (to.path === "/login" && token) {
    return "/dashboard";
  }

  return true;
});

export default router;