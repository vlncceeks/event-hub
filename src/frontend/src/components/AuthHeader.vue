<script setup>
import { ref, onMounted } from 'vue'

const currentUser = ref(null)
const loading = ref(false)
const error = ref(null)

// Состояния для форм
const showLogin = ref(false)
const showRegister = ref(false)
const loginData = ref({ email: '', password: '' })
const registerData = ref({ email: '', password: '', username: '' })

const hasAuthToken = () => {
  return localStorage.getItem('token') !== null
}

const saveUserData = (token, userData) => {
  localStorage.setItem('token', token)
  localStorage.setItem('user_email', userData.email)
  localStorage.setItem('user_username', userData.username)
  if (userData.userId) {
    localStorage.setItem('user_id', userData.userId)
  }
}

const clearUserData = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user_email')
  localStorage.removeItem('user_username')
  localStorage.removeItem('user_id')
}

const fetchUser = async () => {
  const token = localStorage.getItem('token')

  if (!token) {
    currentUser.value = null
    loading.value = false
    return
  }

  loading.value = true
  try {
    const response = await fetch('http://localhost:8081/users/me', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })

    if (response.status === 401) {
      // Токен невалидный или истек
      clearUserData()
      currentUser.value = null
      return
    }

    if (!response.ok) {
      throw new Error('Failed to load user data')
    }

    currentUser.value = await response.json()

    // Обновляем данные в localStorage
    if (currentUser.value) {
      localStorage.setItem('user_email', currentUser.value.email)
      localStorage.setItem('user_username', currentUser.value.username)
    }

  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

const handleLogin = async () => {
  try {
    const response = await fetch('http://localhost:8081/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(loginData.value)
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(`Login failed: ${errorText}`)
    }

    const data = await response.json()

    saveUserData(data.token, {
      email: data.email,
      username: data.username,
      userId: data.userId
    })

    loginData.value = { email: '', password: '' }
    showLogin.value = false

    await fetchUser()

  } catch (err) {
    error.value = err.message
    console.error('Login error:', err)
  }
}

// Регистрация
const handleRegister = async () => {
  try {
    const response = await fetch('http://localhost:8081/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(registerData.value)
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(`Registration failed: ${errorText}`)
    }

    const data = await response.json()

    // Сохраняем токен и данные (после регистрации пользователь сразу залогинен)
    saveUserData(data.token, {
      email: data.email,
      username: data.username,
      userId: data.userId
    })

    // Сбрасываем форму и обновляем UI
    registerData.value = {email: '', password: '', username: ''}
    showRegister.value = false

    // Загружаем данные пользователя
    await fetchUser()

  } catch (err) {
    error.value = err.message
    console.error('Registration error:', err)
  }
}

const handleLogout = async () => {
  const token = localStorage.getItem('token')

  if (token) {
    try {
      const response = await fetch('http://localhost:8081/auth/logout', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      })

      if (!response.ok) {
        console.warn('Logout API call failed, but clearing local data anyway')
      }
    } catch (err) {
      console.error('Logout error:', err)
    }
  }

  clearUserData()
  currentUser.value = null
}

onMounted(async () => {
  if (hasAuthToken()) {
    await fetchUser()
  } else {
    loading.value = false
  }
})
</script>

<template>
  <div class="auth-container">
    <div v-if="loading" class="loading">
      Loading...
    </div>

    <div v-else-if="error" class="error">
      <p>{{ error }}</p>
      <button @click="error = null">Close</button>
    </div>

    <div v-else>
      <div v-if="currentUser" class="user-info">
        <p>Welcome, {{ currentUser.username || currentUser.email }}!</p>
        <button @click="handleLogout" class="logout-btn">Logout</button>
      </div>

      <div v-else class="auth-forms">
        <div class="auth-buttons">
          <button
              @click="showLogin = !showLogin; showRegister = false"
              class="auth-btn"
          >
            {{ showLogin ? 'Cancel Login' : 'Login' }}
          </button>
          <button
              @click="showRegister = !showRegister; showLogin = false"
              class="auth-btn"
          >
            {{ showRegister ? 'Cancel Registration' : 'Register' }}
          </button>
        </div>

        <form
            v-if="showLogin"
            @submit.prevent="handleLogin"
            class="auth-form"
        >
          <h3>Login</h3>
          <input
              v-model="loginData.email"
              type="email"
              placeholder="Email"
              required
          />
          <input
              v-model="loginData.password"
              type="password"
              placeholder="Password"
              required
          />
          <button type="submit">Login</button>
        </form>

        <form
            v-if="showRegister"
            @submit.prevent="handleRegister"
            class="auth-form"
        >
          <h3>Register</h3>
          <input
              v-model="registerData.username"
              type="text"
              placeholder="Username"
              required
          />
          <input
              v-model="registerData.email"
              type="email"
              placeholder="Email"
              required
          />
          <input
              v-model="registerData.password"
              type="password"
              placeholder="Password"
              required
          />
          <button type="submit">Register</button>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.error {
  background: #ffe6e6;
  color: #d32f2f;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 15px;
}

.error button {
  margin-top: 10px;
  padding: 5px 15px;
  background: #d32f2f;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.user-info {
  background: #f0f8ff;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}

.auth-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: center;
}

.auth-btn, .logout-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background: #007bff;
  color: white;
  font-weight: bold;
  transition: background 0.3s;
}

.auth-btn:hover, .logout-btn:hover {
  background: #0056b3;
}

.logout-btn {
  background: #dc3545;
}

.logout-btn:hover {
  background: #c82333;
}

.auth-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.auth-form h3 {
  margin-top: 0;
  margin-bottom: 15px;
  text-align: center;
}

.auth-form input {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

.auth-form button {
  width: 100%;
  padding: 10px;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.3s;
}

.auth-form button:hover {
  background: #218838;
}
</style>