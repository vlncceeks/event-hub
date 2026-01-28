<script setup>
import { ref, onMounted, watch, defineProps, defineEmits } from "vue";
import { useAuth } from "@/store/auth";

const props = defineProps({
  eventId: Number,
});

const emit = defineEmits(["back", "edit"]);

const { token, isAdmin, isAuthenticated } = useAuth();

const event = ref(null);
const registering = ref(false);
const deleting = ref(false);
const error = ref(null);

const API_URL = "http://localhost:8081";

const loadEvent = async () => {
  try {
    const res = await fetch(`${API_URL}/events/${props.eventId}`, {
      headers: token.value ? { Authorization: `Bearer ${token.value}` } : {},
    });
    if (!res.ok) throw new Error("Не удалось загрузить мероприятие");
    event.value = await res.json();

    if (isAuthenticated.value) {
      await checkRegistration();
    }
  } catch (e) {
    console.error(e);
  }
};

const register = async () => {
  registering.value = true;
  try {
    const res = await fetch(`${API_URL}/events/${event.value.id}/register`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token.value}` },
    });
    if (!res.ok) throw new Error("Ошибка записи");

    event.value.availableSeats--;
    event.value.registered = true;
  } catch (e) {
    console.error(e);
  } finally {
    registering.value = false;
  }
};

const checkRegistration = async () => {
  try {
    const res = await fetch(
        `${API_URL}/events/${event.value.id}/registrations/me`,
        {
          headers: {
            Authorization: `Bearer ${token.value}`,
          },
        }
    );

    if (!res.ok) return;

    const data = await res.json();
    event.value.registered = data.registered;
  } catch (e) {
    console.error(e);
  }
}

const deleteEvent = async () => {
  if (!confirm("Вы уверены, что хотите удалить мероприятие?")) return;

  deleting.value = true;
  try {
    const res = await fetch(`${API_URL}/events/${event.value.id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token.value}` },
    });

    if (!res.ok) throw new Error("Ошибка удаления");

    emit("back");
  } catch (e) {
    console.error(e);
  } finally {
    deleting.value = false;
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

    if (event.value) {
      event.value.registered = false;
      event.value.availableSeats++;
    }
  } catch (e) {
    console.error(e);
    error.value = e.message;
  }
};

onMounted(loadEvent);
watch(() => props.eventId, loadEvent);
</script>

<template>
  <div class="container mt-4" v-if="event">
    <button class="btn btn-link mb-3" @click="$emit('back')">
      &larr; Назад к списку
    </button>

    <div class="card">
      <img
          v-if="event.imageUrl"
          :src="event.imageUrl"
          class="card-img-top"
          alt="event image"
      />

      <div class="card-body">
        <div class="d-flex justify-content-between align-items-start">
          <div>
            <h3 class="card-title">{{ event.title }}</h3>
            <p class="text-muted">{{ formatDate(event.date) }}</p>
          </div>

          <div v-if="isAdmin" class="btn-group">
            <button
                class="btn btn-outline-primary btn-sm"
                @click="emit('edit', event)"
            >
              Редактировать
            </button>
            <button
                class="btn btn-outline-danger btn-sm"
                :disabled="deleting"
                @click="deleteEvent"
            >
              Удалить
            </button>
          </div>
        </div>

        <p class="mt-3">{{ event.description }}</p>

        <p>
          Свободных мест:
          <strong>{{ event.availableSeats }}</strong> /
          {{ event.totalSeats }}
        </p>

        <div>
          <button
              v-if="event.registered === false"
              class="btn btn-success"
              :disabled="event.availableSeats === 0 || registering"
              @click="register"
          >
            {{ event.availableSeats === 0 ? "Мест нет" : "Записаться" }}
          </button>

          <button
              class="btn btn-outline-danger"
              v-if="event.registered === true"
              @click="cancelRegistration(event.id)"
          >
            Отменить
          </button>
        </div>
      </div>
    </div>
  </div>

  <div v-else class="text-center mt-5">
    Загрузка...
  </div>
</template>
