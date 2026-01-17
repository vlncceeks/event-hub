<script setup>
import { useAuth } from "@/store/auth";
import { defineEmits } from "vue";

const emit = defineEmits(["navigate"]);
const { isAuthenticated, logout, isAdmin } = useAuth();

const go = (view) => emit("navigate", view);
</script>

<template>
  <ul class="nav">
    <li class="nav-item">
      <button class="nav-link btn btn-link" @click="go('events')">
        Мероприятия
      </button>
    </li>

    <li class="nav-item" v-if="!isAuthenticated">
      <button class="nav-link btn btn-link" @click="go('login')">
        Войти
      </button>
    </li>

    <li class="nav-item" v-if="isAuthenticated">
      <button class="nav-link btn btn-link" @click="go('profile')">
        Профиль
      </button>
    </li>

    <li class="nav-item" v-if="isAuthenticated">
      <button class="nav-link btn btn-link" @click="logout">
        Выйти
      </button>
    </li>
    <div v-if="isAdmin">
      <button class="nav-link btn btn-link" @click="go('users')">
        Пользователи
      </button>
    </div>
  </ul>

</template>
