<script setup>

import EventDetails from "@/components/EventDetails.vue";
import EventList from "@/components/EventList.vue";
import {ref} from "vue";
import EventForm from "@/components/EventForm.vue";
import { useAuth } from "@/store/auth";

const { isAdmin } = useAuth();

const selectedEventId = ref(null);

const showEvent = (id) => {
  selectedEventId.value = id;
};

const goBack = () => {
  selectedEventId.value = null;
};
</script>

<template>
  <EventForm v-if="isAdmin"></EventForm>
  <EventList v-if="!selectedEventId" @select-event="showEvent" />
  <EventDetails v-else :eventId="selectedEventId" @back="goBack" />
</template>

<style scoped>

</style>