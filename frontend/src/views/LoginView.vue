<template>
  <div class="auth-container">
    <div class="card auth-card">
      <h2 class="auth-title">Bienvenido de nuevo 👋</h2>
      <p class="auth-subtitle">Ingresa a tu cuenta para gestionar tus publicaciones</p>

      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label class="form-label">Nombre de usuario</label>
          <input 
            v-model="username" 
            type="text" 
            class="form-input" 
            placeholder="Tu usuario" 
            required 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Contraseña</label>
          <input 
            v-model="password" 
            type="password" 
            class="form-input" 
            placeholder="••••••••" 
            required 
          />
        </div>

        <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
          {{ loading ? 'Iniciando sesión...' : 'Iniciar Sesión' }}
        </button>
      </form>

      <div class="auth-footer">
        ¿No tienes una cuenta? <router-link to="/register" class="link">Regístrate aquí</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authState } from '../store/auth'

const router = useRouter()

const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref(null)

const handleLogin = async () => {
  loading.value = true
  error.value = null

  const result = await authState.login(username.value, password.value)
  loading.value = false

  if (result.success) {
    router.push('/')
  } else {
    error.value = result.message
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(80vh - 80px);
  padding: 40px 20px;
}

.auth-card {
  width: 100%;
  max-width: 440px;
  padding: 36px;
}

.auth-title {
  font-size: 1.8rem;
  font-weight: 800;
  text-align: center;
  margin-bottom: 6px;
}

.auth-subtitle {
  color: var(--text-secondary);
  text-align: center;
  font-size: 0.95rem;
  margin-bottom: 24px;
}

.btn-block {
  width: 100%;
  padding: 12px;
  font-size: 1rem;
  margin-top: 10px;
}

.auth-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.link {
  color: var(--accent-primary);
  font-weight: 600;
}
.link:hover {
  text-decoration: underline;
}
</style>
