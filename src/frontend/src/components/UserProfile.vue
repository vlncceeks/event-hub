<script setup>
import { onMounted, ref } from "vue";
import { useAuth } from "@/store/auth";

const { isAuthenticated, user, token } = useAuth();

const registrations = ref([]);
const loading = ref(true);
const error = ref(null);

const fetchRegistrations = async () => {
  try {
    const response = await fetch("http://localhost:8080/users/me/registrations", {
      headers: {
        Authorization: `Bearer ${token.value}`,
      },
    });

    if (!response.ok) {
      throw new Error("Не удалось загрузить записи");
    }

    registrations.value = await response.json();
  } catch (e) {
    error.value = e.message;
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  if (isAuthenticated.value) {
    fetchRegistrations();
  }
});
</script>

<template>
  <div class="container mt-4">
    <div class="row justify-content-center">
      <div class="col-md-8">

        <div v-if="!isAuthenticated" class="alert alert-warning text-center">
          Для просмотра личного кабинета необходимо войти в систему
        </div>

        <div v-else class="card shadow-sm mb-4">
          <div class="card-body">
            <h4 class="card-title mb-3">Личный кабинет</h4>

            <ul class="list-group list-group-flush">
              <li class="list-group-item">
                <strong>ID:</strong> {{ user.id }}
              </li>
              <li class="list-group-item">
                <strong>Имя пользователя:</strong> {{ user.username }}
              </li>
              <li class="list-group-item">
                <strong>Email:</strong> {{ user.email }}
              </li>
              <li class="list-group-item">
                <strong>Роль:</strong>
                <span
                    class="badge"
                    :class="user.role === 'ADMIN' ? 'bg-danger' : 'bg-primary'"
                >
                  {{ user.role }}
                </span>
              </li>
            </ul>
          </div>
        </div>

        <div v-if="isAuthenticated" class="card shadow-sm">
          <div class="card-body">
            <h5 class="card-title mb-3">Мои записи на мероприятия</h5>

            <div v-if="loading" class="text-center">
              <div class="spinner-border" role="status"></div>
            </div>

            <div v-else-if="error" class="alert alert-danger">
              {{ error }}
            </div>

            <div v-else-if="registrations.length === 0" class="alert alert-info">
              У вас пока нет записей на мероприятия
            </div>

            <ul v-else class="list-group">
              <li
                  v-for="reg in registrations"
                  :key="reg.id"
                  class="list-group-item d-flex justify-content-between align-items-center"
              >
                <div>
                  <div class="fw-bold">{{ reg.event.title }}</div>
                  <small class="text-muted">
                    {{ new Date(reg.event.date).toLocaleString() }}
                  </small>
                </div>

                <span class="badge bg-success">
                  Записан
                </span>
              </li>
            </ul>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped>

</style>