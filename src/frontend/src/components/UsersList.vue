<template>
  <div class="users">
    <h2>Users</h2>

    <div v-if="loading">Loading...</div>
    <div v-else-if="error" class="error">{{ error }}</div>

    <ul v-else>
      <li v-for="user in users" :key="user.id">
        <strong>{{ user.username }}</strong> — {{ user.email }} — {{ user.role }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const users = ref([])
const loading = ref(true)
const error = ref(null)

const fetchUsers = async () => {
  try {
    const response = await fetch('http://localhost:8081/users')

    if (!response.ok) {
      throw new Error('Failed to load users')
    }

    users.value = await response.json()
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

onMounted(fetchUsers)
</script>

<style scoped>
.users {
  max-width: 500px;
}

.error {
  color: red;
}
</style>
