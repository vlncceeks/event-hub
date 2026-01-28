<script setup>
import {ref} from "vue";
import {useAuth} from "@/store/auth";

import EventList from "@/components/EventList.vue";
import EventDetails from "@/components/EventDetails.vue";
import EventForm from "@/components/EventForm.vue";

const {isAdmin} = useAuth();

const currentView = ref("list");

const selectedEventId = ref(null);
const eventToEdit = ref(null);

const showEvent = (id) => {
  selectedEventId.value = id;
  currentView.value = "details";
};

const goBack = () => {
  selectedEventId.value = null;
  eventToEdit.value = null;
  currentView.value = "list";
};

const openEditForm = (event) => {
  eventToEdit.value = event;
  currentView.value = "edit";
};

const createEvent = () => {
  eventToEdit.value = null;
  currentView.value = "edit";
};

const onSaved = () => {
  goBack();
};
</script>

<template>
  <!-- КНОПКА СОЗДАНИЯ -->
  <div class="container mt-3" v-if="isAdmin && currentView === 'list'">
    <button class="btn btn-primary mb-3" @click="createEvent">
      + Создать мероприятие
    </button>
  </div>

  <!-- СПИСОК -->
  <EventList
      v-if="currentView === 'list'"
      @select-event="showEvent"
  />

  <!-- ДЕТАЛИ -->
  <EventDetails
      v-if="currentView === 'details'"
      :eventId="selectedEventId"
      @back="goBack"
      @edit="openEditForm"
  />

  <!-- ФОРМА -->
  <EventForm
      v-if="currentView === 'edit'"
      :eventToEdit="eventToEdit"
      @saved="onSaved"
  />
</template>
