<template>
  <div v-if="isOpen" class="modal-backdrop" @click.self="close">
    <div class="modal-content card">
      <div class="modal-header">
        <h2>⚙️ Administrar mi Perfil</h2>
        <button class="btn-close" @click="close">&times;</button>
      </div>

      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>

      <div v-if="successMsg" class="alert alert-success">
        {{ successMsg }}
      </div>

      <form @submit.prevent="saveProfile">
        <div class="form-group">
          <label class="form-label">Nombre completo</label>
          <input 
            v-model="form.name" 
            type="text" 
            class="form-input" 
            placeholder="Tu nombre completo"
            required 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Nombre de usuario (no editable)</label>
          <input 
            :value="authState.user?.username" 
            type="text" 
            class="form-input disabled-input" 
            disabled 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Correo electrónico</label>
          <input 
            v-model="form.email" 
            type="email" 
            class="form-input" 
            placeholder="tu@email.com"
            required 
          />
        </div>

        <hr class="divider" />

        <h3 class="section-subtitle">🔐 Cambiar Contraseña (Opcional)</h3>

        <div class="form-group">
          <label class="form-label">Contraseña actual</label>
          <input 
            v-model="form.currentPassword" 
            type="password" 
            class="form-input" 
            placeholder="Requerida solo si cambias la contraseña" 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Nueva contraseña</label>
          <input 
            v-model="form.newPassword" 
            type="password" 
            class="form-input" 
            placeholder="••••••••" 
          />
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="close">Cancelar</button>
          <button type="submit" class="btn btn-primary" :disabled="saving">
            {{ saving ? 'Guardando...' : 'Guardar Cambios' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'
import { authState } from '../store/auth'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close'])

const saving = ref(false)
const error = ref(null)
const successMsg = ref(null)

const form = reactive({
  name: '',
  email: '',
  currentPassword: '',
  newPassword: ''
})

watch(() => props.isOpen, (newVal) => {
  if (newVal && authState.user) {
    form.name = authState.user.name || ''
    form.email = authState.user.email || ''
    form.currentPassword = ''
    form.newPassword = ''
    error.value = null
    successMsg.value = null
  }
})

const close = () => {
  emit('close')
}

const saveProfile = async () => {
  saving.value = true
  error.value = null
  successMsg.value = null

  if (form.newPassword && !form.currentPassword) {
    error.value = 'Debes ingresar tu contraseña actual para establecer una nueva.'
    saving.value = false
    return
  }

  try {
    const res = await fetch('/api/users/profile', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: form.name,
        email: form.email,
        currentPassword: form.currentPassword,
        newPassword: form.newPassword
      }),
      credentials: 'include'
    })

    const data = await res.json()

    if (res.ok) {
      authState.user = data
      successMsg.value = '¡Perfil actualizado exitosamente!'
      form.currentPassword = ''
      form.newPassword = ''
      setTimeout(() => {
        close()
      }, 1200)
    } else {
      error.value = data.error || 'Error al actualizar el perfil'
    }
  } catch (err) {
    error.value = 'Error de conexión con el servidor'
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(8px);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.modal-content {
  width: 100%;
  max-width: 520px;
  background: var(--popover-bg);
  border: 1px solid var(--border-color);
  padding: 28px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 12px;
}

.modal-header h2 {
  font-size: 1.4rem;
  font-weight: 700;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.8rem;
  color: var(--text-muted);
  cursor: pointer;
  line-height: 1;
}

.btn-close:hover {
  color: var(--text-primary);
}

.disabled-input {
  opacity: 0.6;
  cursor: not-allowed;
}

.divider {
  border: none;
  border-top: 1px solid var(--border-color);
  margin: 20px 0;
}

.section-subtitle {
  font-size: 1rem;
  font-weight: 700;
  margin-bottom: 14px;
  color: var(--accent-secondary);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}
</style>
