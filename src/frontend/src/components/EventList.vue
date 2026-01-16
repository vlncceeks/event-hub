<template>
  <div class="events">
    <h1>Мероприятия</h1>

    <div v-if="loading">Загрузка...</div>

    <div v-else-if="events.length === 0">
      Нет доступных мероприятий
    </div>

    <div class="event-list">
      <div
          v-for="event in events"
          :key="event.id"
          class="event-card"
      >
        <img
            v-if="event.imageUrl"
            :src="event.imageUrl"
            alt="event image"
            class="event-image"
        />

        <h2>{{ event.title }}</h2>
        <p class="date">{{ formatDate(event.date) }}</p>
        <p class="description">{{ event.description }}</p>

        <p class="seats">
          Свободных мест:
          <strong>{{ event.availableSeats }}</strong> /
          {{ event.totalSeats }}
        </p>

        <button
            :disabled="event.availableSeats === 0 || registeringId === event.id"
            @click="register(event.id)"
        >
          {{ event.availableSeats === 0 ? "Мест нет" : "Записаться" }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const events = ref([]);
const loading = ref(true);
const registeringId = ref(null);

const API_URL = "http://localhost:8081";

const loadEvents = async () => {
  try {
    const res = await fetch(`${API_URL}/events`);

    if (!res.ok) {
      throw new Error("Ошибка загрузки мероприятий");
    }

    events.value = await res.json();
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
};

const register = async (eventId) => {
  registeringId.value = eventId;

  try {
    const res = await fetch(
        `${API_URL}/events/${eventId}/register`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
    );

    if (!res.ok) {
      const error = await res.json();
      throw new Error(error.message || "Ошибка записи");
    }

    const event = events.value.find(e => e.id === eventId);
    if (event) event.availableSeats--;

    alert("Вы успешно записались!");
  } catch (e) {
    alert(e.message);
  } finally {
    registeringId.value = null;
  }
};

const formatDate = (date) => {
  return new Date(date).toLocaleString("ru-RU", {
    dateStyle: "medium",
    timeStyle: "short",
  });
};

onMounted(loadEvents);
</script>


<style scoped>
.events {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.event-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.event-card {
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 16px;
  background: #fff;
  display: flex;
  flex-direction: column;
}

.event-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 10px;
}

.date {
  color: #666;
  font-size: 14px;
}

.description {
  flex-grow: 1;
  margin: 10px 0;
}

.seats {
  margin-bottom: 10px;
}

button {
  padding: 10px;
  border: none;
  border-radius: 6px;
  background: #42b883;
  color: white;
  cursor: pointer;
}

button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
