<template>
  <div class="container mt-4" v-if="event">
    <button class="btn btn-link mb-3" @click="$emit('back')">&larr; Назад к списку</button>

    <div class="card">
      <img v-if="event.imageUrl" :src="event.imageUrl" class="card-img-top" alt="event image" />
      <div class="card-body">
        <h3 class="card-title">{{ event.title }}</h3>
        <p class="text-muted">{{ formatDate(event.date) }}</p>
        <p>{{ event.description }}</p>
        <p>
          Свободных мест: <strong>{{ event.availableSeats }}</strong> / {{ event.totalSeats }}
        </p>

        <button
            v-if="!event.registered"
            class="btn btn-success"
            :disabled="event.availableSeats === 0 || registering"
            @click="register"
        >
          {{ event.availableSeats === 0 ? "Мест нет" : "Записаться" }}
        </button>
        <span v-else class="badge bg-secondary">Вы уже записаны</span>
      </div>
    </div>
  </div>

  <div v-else class="text-center mt-5">
    Загрузка...
  </div>
</template>

<script setup>
import { ref, onMounted, watch, defineProps, defineEmits } from "vue";
import { useAuth } from "@/store/auth";

const props = defineProps({
  eventId: Number,
});
defineEmits(["back"]);
const { token } = useAuth();
const event = ref(null);
const registering = ref(false);

const API_URL = "http://localhost:8081";

const loadEvent = async () => {
  try {
    const res = await fetch(`${API_URL}/events/${props.eventId}`, {
      headers: token.value ? { Authorization: `Bearer ${token.value}` } : {},
    });
    if (!res.ok) throw new Error("Не удалось загрузить мероприятие");
    event.value = await res.json();
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

const formatDate = (date) =>
    new Date(date).toLocaleString("ru-RU", { dateStyle: "medium", timeStyle: "short" });

onMounted(loadEvent);

watch(() => props.eventId, loadEvent);
</script>
