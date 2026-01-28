<script setup>
import UserEditForm from "@/components/UserEditForm.vue";
import { onMounted, ref } from "vue";
import { useAuth } from "@/store/auth";

const { isAuthenticated, user, token } = useAuth();

const registrations = ref([]);
const loading = ref(true);
const error = ref(null);
const cancellingId = ref(null);
const view = ref("profile");

const API_URL = "http://localhost:8081";

const openEdit = () => {
  view.value = "edit";
};

const backToProfile = () => {
  view.value = "profile";
};

const handleUpdated = (updatedUser) => {
  user.value = updatedUser;
  localStorage.setItem("user", JSON.stringify(updatedUser));
  view.value = "profile";
};

const fetchRegistrations = async () => {
  loading.value = true;
  try {
    const response = await fetch(`${API_URL}/users/me/registrations`, {
      headers: {
        Authorization: `Bearer ${token.value}`,
      },
    });

    if (!response.ok) throw new Error("Не удалось загрузить записи");

    registrations.value = await response.json();
  } catch (e) {
    error.value = e.message;
  } finally {
    loading.value = false;
  }
};

const cancelRegistration = async (eventId) => {
  cancellingId.value = eventId;
  try {
    const res = await fetch(`${API_URL}/events/${eventId}/register`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token.value}`,
      },
    });

    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.message || "Ошибка отмены записи");
    }

    registrations.value = registrations.value.filter(r => r.event.id !== eventId);

  } catch (e) {
    console.error(e);
    error.value = e.message;
  } finally {
    cancellingId.value = null;
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

        <div v-if="view === 'profile'">
          <div class="card shadow-sm mb-4">
            <div class="card-body">
              <h4 class="card-title mb-3">Личный кабинет</h4>
              <ul class="list-group list-group-flush">
                <li class="list-group-item">
                  <strong>Имя пользователя:</strong> {{ user?.username }}
                </li>
                <li class="list-group-item">
                  <strong>Email:</strong> {{ user?.email }}
                </li>
              </ul>

              <button class="btn btn-outline-primary" @click="openEdit">
                Редактировать профиль
              </button>
            </div>
          </div>

          <div class="card shadow-sm">
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

                <div class="d-flex gap-2">
                  <span class="badge bg-success" v-if="!cancellingId || cancellingId !== reg.event.id">
                    Записан
                  </span>

                  <button
                      class="btn btn-outline-danger btn-sm"
                      v-if="!cancellingId || cancellingId !== reg.event.id"
                      @click="cancelRegistration(reg.event.id)"
                  >
                    Отменить
                  </button>

                  <div v-else class="spinner-border spinner-border-sm text-danger" role="status"></div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        </div>

        <UserEditForm
            v-else
            @back="backToProfile"
            @updated="handleUpdated"
        />
      </div>
    </div>
  </div>
</template>
