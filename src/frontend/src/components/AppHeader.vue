<script setup>
import { useAuth } from "@/store/auth";
import { useRouter } from "vue-router";

const router = useRouter();
const { isAuthenticated, logout } = useAuth();

const handleLogout = () => {
  logout();
  router.push("/login"); // или "/" — куда нужно
};
</script>

<template>
  <ul class="nav">

    <li class="nav-item">
      <router-link class="nav-link" to="/events">
        Мероприятия
      </router-link>
    </li>

    <li class="nav-item" v-if="isAuthenticated">
      <router-link class="nav-link" to="/UserProfile">
        Профиль
      </router-link>
    </li>

    <li class="nav-item" v-if="isAuthenticated">
      <button
          class="nav-link btn btn-link"
          type="button"
          @click="handleLogout"
      >
        Выйти
      </button>
    </li>

  </ul>
</template>

<style scoped>
button.nav-link {
  cursor: pointer;
}
</style>
