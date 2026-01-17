<script setup>
import { ref, computed } from "vue";
import { useAuth } from "@/store/auth";

import AppHeader from "@/components/AppHeader.vue";
import UserProfile from "@/components/UserProfile.vue";
import AuthPage from "@/components/AuthPage.vue";
import UsersList from "@/components/UsersList.vue";
import ShowEvents from "@/components/ShowEvents.vue";

const { isAuthenticated, isAdmin } = useAuth();

const currentView = ref("events");

const navigate = (view) => {
  currentView.value = view;
};

const resolvedView = computed(() => {
  if (currentView.value === "profile" && !isAuthenticated.value) {
    return "login";
  }
  return currentView.value;
});


</script>

<template>
  <AppHeader @navigate="navigate" />

  <ShowEvents v-if="resolvedView === 'events'"/>
  <UserProfile v-if="resolvedView === 'profile'" />
  <AuthPage v-if="resolvedView === 'login'" />
  <div v-if="isAdmin">
    <UsersList v-if="resolvedView === 'users'" />
  </div>
</template>
