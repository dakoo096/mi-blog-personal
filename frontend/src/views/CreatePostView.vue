<template>
  <div class="container form-container">
    <div class="card form-card">
      <h1 class="form-title">Nueva Publicación 📝</h1>
      <p class="form-subtitle">Comparte tus pensamientos, guías o noticias con la comunidad.</p>

      <div v-if="error" class="alert alert-danger">
        {{ error }}
      </div>

      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label class="form-label">Título</label>
          <input 
            v-model="title" 
            type="text" 
            class="form-input" 
            placeholder="Ej. Introducción a Spring Boot y Vue 3" 
            required 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Contenido</label>
          <textarea 
            v-model="content" 
            class="form-textarea" 
            rows="10" 
            placeholder="Escribe todo el contenido de tu publicación aquí..." 
            required
          ></textarea>
        </div>

        <div class="form-actions">
          <router-link to="/my-posts" class="btn btn-secondary">Cancelar</router-link>
          <button type="submit" class="btn btn-primary" :disabled="submitting">
            {{ submitting ? 'Guardando...' : 'Publicar Entrada' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const title = ref('')
const content = ref('')
const submitting = ref(false)
const error = ref(null)

const handleSubmit = async () => {
  if (!title.value.trim() || !content.value.trim()) return

  submitting.value = true
  error.value = null

  try {
    const res = await fetch('/api/posts', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        title: title.value,
        content: content.value
      }),
      credentials: 'include'
    })

    if (res.ok) {
      router.push('/my-posts')
    } else {
      const data = await res.json()
      error.value = data.error || 'Error al crear la publicación.'
    }
  } catch (err) {
    error.value = 'Error de conexión al servidor.'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.form-container {
  padding-top: 40px;
  max-width: 720px;
}

.form-title {
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 6px;
}

.form-subtitle {
  color: var(--text-secondary);
  margin-bottom: 24px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}
</style>
