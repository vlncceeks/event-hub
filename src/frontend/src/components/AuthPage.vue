<script setup>
import { ref } from "vue";
import { useAuth } from "@/store/auth";

const { login: authLogin } = useAuth();

const isLogin = ref(true);
const error = ref(null);

const loginData = ref({
  email: "",
  password: "",
});

const registerData = ref({
  username: "",
  email: "",
  password: "",
});

const submitLogin = async () => {
  error.value = null;

  try {
    const res = await fetch("http://localhost:8081/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(loginData.value),
    });

    if (!res.ok) throw new Error("Неверный email или пароль");

    const data = await res.json();

    authLogin(data.token, {
      id: data.user.id,
      username: data.user.username,
      email: data.user.email,
      role: data.user.role,
    });

  } catch (e) {
    error.value = e.message;
  }
};

const submitRegister = async () => {
  error.value = null;

  try {
    const res = await fetch("http://localhost:8081/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(registerData.value),
    });

    if (!res.ok) throw new Error("Ошибка регистрации");

    const data = await res.json();

    authLogin(data.token, {
      id: data.id,
      email: data.email,
      username: data.username,
      role: data.role,
    });

  } catch (e) {
    error.value = e.message;
  }
};
</script>


<template>
  <div class="container mt-5" style="max-width: 420px;">
    <div class="card shadow-sm">
      <div class="card-body">

        <h4 class="card-title text-center mb-3">
          {{ isLogin ? "Вход" : "Регистрация" }}
        </h4>

        <div v-if="error" class="alert alert-danger">
          {{ error }}
        </div>

        <!-- LOGIN -->
        <form v-if="isLogin" @submit.prevent="submitLogin">
        <div class="mb-3">
            <input v-model="loginData.email" class="form-control" type="email" placeholder="Email" required />
          </div>
          <div class="mb-3">
            <input v-model="loginData.password" class="form-control" type="password" placeholder="Пароль" required />
          </div>
          <button class="btn btn-primary w-100">Войти</button>
        </form>

        <!-- REGISTER -->
        <form v-else @submit.prevent="submitRegister">
          <div class="mb-3">
            <input v-model="registerData.username" class="form-control" placeholder="Имя" required />
          </div>
          <div class="mb-3">
            <input v-model="registerData.email" class="form-control" type="email" placeholder="Email" required />
          </div>
          <div class="mb-3">
            <input v-model="registerData.password" class="form-control" type="password" placeholder="Пароль" required />
          </div>
          <button class="btn btn-success w-100">Зарегистрироваться</button>
        </form>

        <div class="text-center mt-3">
          <button class="btn btn-link" @click="isLogin = !isLogin">
            {{ isLogin ? "Создать аккаунт" : "У меня уже есть аккаунт" }}
          </button>
        </div>

      </div>
    </div>
  </div>
</template>
