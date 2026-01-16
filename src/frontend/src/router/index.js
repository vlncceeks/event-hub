import { createRouter, createWebHistory } from "vue-router";
import EventList from "@/components/EventList.vue";
import Profile from "@/components/UserProfile.vue";

const routes = [
    {
        path: "/",
        redirect: "/events",
    },
    {
        path: "/events",
        name: "events",
        component: EventList,
    },
    {
        path: "/profile",
        name: "profile",
        component: Profile,
        meta: { requiresAuth: true },
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
