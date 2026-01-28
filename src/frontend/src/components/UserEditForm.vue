<script setup>
import { ref, onMounted, defineEmits} from "vue";
import { useAuth } from "@/store/auth";

const emit = defineEmits(["back", "updated"]);
const { user, token } = useAuth();

const form = ref({
  username: "",
  email: "",
  password: ""
});

const loading = ref(false);
const error = ref(null);
const success = ref(false);

const API_URL = "http://localhost:8081";

onMounted(() => {
  if (user.value) {
    form.value.username = user.value.username;
    form.value.email = user.value.email;
  }
});

const submit = async () => {
  loading.value = true;
  error.value = null;
  success.value = false;

  try {
    const res = await fetch(`${API_URL}/users/me`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token.value}`
      },
      body: JSON.stringify(form.value)
    });

    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.message || "Ошибка обновления профиля");
    }

    const updatedUser = await res.json();
    success.value = true;
    emit("updated", updatedUser);
  } catch (e) {
    error.value = e.message;
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="card shadow-sm">
    <div class="card-body">
      <h4 class="card-title mb-3">Редактирование профиля</h4>

      <div v-if="error" class="alert alert-danger">{{ error }}</div>
      <div v-if="success" class="alert alert-success">
        Данные успешно обновлены
      </div>

      <form @submit.prevent="submit">
        <div class="mb-3">
          <label class="form-label">Имя пользователя</label>
          <input v-model="form.username" type="text" class="form-control" />
        </div>

        <div class="mb-3">
          <label class="form-label">Email</label>
          <input v-model="form.email" type="email" class="form-control" />
        </div>

        <div class="mb-3">
          <label class="form-label">Новый пароль</label>
          <input
              v-model="form.password"
              type="password"
              class="form-control"
              placeholder="Оставьте пустым, если не хотите менять"
          />
        </div>

        <div class="d-flex gap-2">
          <button class="btn btn-primary" type="submit" :disabled="loading">
            {{ loading ? "Сохранение..." : "Сохранить" }}
          </button>

          <button class="btn btn-outline-secondary" type="button" @click="$emit('back')">
            Назад
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
