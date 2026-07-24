<template>
  <div class="auth-container">
    <div class="card auth-card">
      <h2 class="auth-title">Crear Cuenta ✨</h2>
      <p class="auth-subtitle">Únete a nuestra comunidad de escritores y desarrolladores</p>

      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>

      <div v-if="successMsg" class="alert alert-success">
        {{ successMsg }}
      </div>

      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label class="form-label">Nombre completo</label>
          <input 
            v-model="form.name" 
            type="text" 
            class="form-input" 
            placeholder="Ej. Juan Pérez" 
            required 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Nombre de usuario</label>
          <input 
            v-model="form.username" 
            type="text" 
            class="form-input" 
            placeholder="Ej. juanperez" 
            required 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Correo electrónico</label>
          <input 
            v-model="form.email" 
            type="email" 
            class="form-input" 
            placeholder="juan@ejemplo.com" 
            required 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Contraseña</label>
          <input 
            v-model="form.password" 
            type="password" 
            class="form-input" 
            placeholder="••••••••" 
            required 
          />
        </div>

        <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
          {{ loading ? 'Registrando...' : 'Registrarse' }}
        </button>
      </form>

      <div class="auth-footer">
        ¿Ya tienes cuenta? <router-link to="/login" class="link">Inicia sesión</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authState } from '../store/auth'

const router = useRouter()

const form = reactive({
  name: '',
  username: '',
  email: '',
  password: ''
})

const loading = ref(false)
const error = ref(null)
const successMsg = ref(null)

const handleRegister = async () => {
  loading.value = true
  error.value = null
  successMsg.value = null

  const result = await authState.register(form)
  loading.value = false

  if (result.success) {
    successMsg.value = '¡Usuario registrado con éxito! Redirigiendo al inicio de sesión...'
    setTimeout(() => {
      router.push('/login')
    }, 1500)
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
