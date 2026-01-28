<script setup>
import { ref, onMounted, defineProps, defineEmits } from "vue";
import { useAuth } from "@/store/auth";

const { token, isAdmin } = useAuth();

const props = defineProps({
  eventToEdit: { type: Object, default: null }
});

const emit = defineEmits(["saved"]);

const form = ref({
  title: "",
  description: "",
  date: "",
  totalSeats: 1,
  imageUrl: null
});

const loading = ref(false);
const error = ref(null);

onMounted(() => {
  if (props.eventToEdit) {
    form.value = {
      title: props.eventToEdit.title || "",
      description: props.eventToEdit.description || "",
      date: props.eventToEdit.date ? props.eventToEdit.date.slice(0, 16) : "",
      totalSeats: props.eventToEdit.totalSeats || 1,
      imageUrl: props.eventToEdit.imageUrl || null
    };
  }
});

const submitForm = async () => {
  error.value = null;

  if (!isAdmin.value) {
    error.value = "Только администратор может создавать/редактировать мероприятия";
    return;
  }

  loading.value = true;

  try {
    const url = props.eventToEdit
        ? `http://localhost:8081/events/${props.eventToEdit.id}`
        : "http://localhost:8081/events";
    const method = props.eventToEdit ? "PUT" : "POST";

    // Преобразуем пустую строку imageUrl в null
    const payload = {
      ...form.value,
      date: form.value.date ? new Date(form.value.date).toISOString() : null,
      imageUrl: form.value.imageUrl?.trim() || null
    };

    const res = await fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token.value}`
      },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const err = await res.json();
      throw new Error(err.message || "Ошибка сохранения мероприятия");
    }

    const savedEvent = await res.json();
    emit("saved", savedEvent);

    // Сброс формы
    form.value = { title: "", description: "", date: "", totalSeats: 1, imageUrl: null };
  } catch (e) {
    error.value = e.message;
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="card shadow-sm mb-4">
    <div class="card-body">
      <h5 class="card-title mb-3">
        {{ props.eventToEdit ? "Редактировать мероприятие" : "Создать мероприятие" }}
      </h5>

      <div v-if="error" class="alert alert-danger">{{ error }}</div>

      <form @submit.prevent="submitForm">
        <div class="mb-3">
          <label class="form-label">Название</label>
          <input v-model="form.title" type="text" class="form-control" required />
        </div>

        <div class="mb-3">
          <label class="form-label">Описание</label>
          <textarea v-model="form.description" class="form-control" rows="3"></textarea>
        </div>

        <div class="mb-3">
          <label class="form-label">Дата и время</label>
          <input v-model="form.date" type="datetime-local" class="form-control" required />
        </div>

        <div class="mb-3">
          <label class="form-label">Количество мест</label>
          <input v-model.number="form.totalSeats" type="number" min="1" class="form-control" required />
        </div>

        <div class="mb-3">
          <label class="form-label">URL изображения</label>
          <input v-model="form.imageUrl" type="text" class="form-control" placeholder="https://..." />
        </div>

        <button type="submit" class="btn btn-primary" :disabled="loading">
          {{ loading ? "Сохранение..." : (props.eventToEdit ? "Сохранить" : "Создать") }}
        </button>
      </form>
    </div>
  </div>
</template>
