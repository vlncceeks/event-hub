<template>
  <div class="container mt-4">
    <h1 class="mb-4">Мероприятия</h1>

    <div v-if="loading" class="text-center">Загрузка...</div>
    <div v-else-if="events.length === 0" class="alert alert-info">
      Нет доступных мероприятий
    </div>

    <div class="row">
      <div class="col-md-4 mb-4" v-for="event in events" :key="event.id">
        <div class="card h-100">
          <img
              v-if="event.imageUrl"
              :src="event.imageUrl"
              class="card-img-top"
              alt="event image"
          />

          <div class="card-body d-flex flex-column">
            <h5 class="card-title">{{ event.title }}</h5>
            <p class="text-muted">{{ formatDate(event.date) }}</p>
            <p>{{ event.description }}</p>

            <p>
              Свободных мест:
              <strong>{{ event.availableSeats }}</strong> /
              {{ event.totalSeats }}
            </p>

            <button
                class="btn btn-outline-primary mt-auto"
                @click="$emit('select-event', event.id)"
            >
              Подробнее
            </button>


            <button
                v-if="event.registered === false"
                class="btn btn-success mt-2"
                :disabled="event.availableSeats === 0 || registeringId === event.id"
                @click="register(event)"
            >
              {{ event.availableSeats === 0 ? "Мест нет" : "Записаться" }}
            </button>

            <button
                class="btn btn-outline-danger mt-2"
                v-if="event.registered === true"
                @click="cancelRegistration(event.id)"
            >
              Отменить
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuth } from "@/store/auth";

const events = ref([]);
const loading = ref(true);
const registeringId = ref(null);
const error = ref(null);

const { token, isAuthenticated } = useAuth();
const API_URL = "http://localhost:8081";


const loadEvents = async () => {
  try {
    const res = await fetch(`${API_URL}/events`, {
      headers: token.value ? { Authorization: `Bearer ${token.value}` } : {},
    });

    if (!res.ok) throw new Error("Ошибка загрузки мероприятий");

    const data = await res.json();

    events.value = data.map(e => ({
      ...e,
      registered: null,
    }));

    if (isAuthenticated.value) {
      await checkRegistrations();
    }
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
};

const checkRegistrations = async () => {
  await Promise.all(
      events.value.map(async (event) => {
        try {
          const res = await fetch(
              `${API_URL}/events/${event.id}/registrations/me`,
              {
                headers: {
                  Authorization: `Bearer ${token.value}`,
                },
              }
          );

          if (!res.ok) return;

          const data = await res.json();
          event.registered = data.registered;
        } catch (e) {
          console.error(e);
        }
      })
  );
};


const register = async (event) => {
  registeringId.value = event.id;

  try {
    const res = await fetch(
        `${API_URL}/events/${event.id}/register`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token.value}`,
          },
        }
    );

    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.message || "Ошибка записи");
    }

    event.availableSeats--;
    event.registered = true;
  } catch (e) {
    console.error(e);
  } finally {
    registeringId.value = null;
  }
};

const formatDate = (date) =>
    new Date(date).toLocaleString("ru-RU", {
      dateStyle: "medium",
      timeStyle: "short",
    });

const cancelRegistration = async (eventId) => {
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

    const event = events.value.find(e => e.id === eventId);
    if (event) {
      event.registered = false;
      event.availableSeats++;
    }
  } catch (e) {
    console.error(e);
    error.value = e.message;
  }
};

onMounted(loadEvents);
</script>
