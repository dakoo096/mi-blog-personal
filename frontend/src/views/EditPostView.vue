<template>
  <div class="container form-container">
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Cargando datos de la publicación...</p>
    </div>

    <div v-else-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <div v-else class="card form-card">
      <h1 class="form-title">Editar Publicación ✏️</h1>
      <p class="form-subtitle">Modifica el título o el contenido de tu publicación.</p>

      <form @submit.prevent="handleUpdate">
        <div class="form-group">
          <label class="form-label">Título</label>
          <input 
            v-model="title" 
            type="text" 
            class="form-input" 
            required 
          />
        </div>

        <div class="form-group">
          <label class="form-label">Contenido</label>
          <textarea 
            v-model="content" 
            class="form-textarea" 
            rows="10" 
            required
          ></textarea>
        </div>

        <div class="form-actions">
          <router-link to="/my-posts" class="btn btn-secondary">Cancelar</router-link>
          <button type="submit" class="btn btn-primary" :disabled="submitting">
            {{ submitting ? 'Actualizando...' : 'Guardar Cambios' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const title = ref('')
const content = ref('')
const loading = ref(true)
const submitting = ref(false)
const error = ref(null)

const fetchPost = async () => {
  loading.value = true
  try {
    const res = await fetch(`/api/posts/${route.params.id}`)
    if (res.ok) {
      const data = await res.json()
      title.value = data.title
      content.value = data.content
    } else {
      error.value = 'Publicación no encontrada.'
    }
  } catch (err) {
    error.value = 'Error al cargar publicación.'
  } finally {
    loading.value = false
  }
}

const handleUpdate = async () => {
  if (!title.value.trim() || !content.value.trim()) return

  submitting.value = true
  error.value = null

  try {
    const res = await fetch(`/api/posts/${route.params.id}`, {
      method: 'PUT',
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
      error.value = data.error || 'Error al actualizar la publicación.'
    }
  } catch (err) {
    error.value = 'Error de conexión con el servidor.'
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchPost()
})
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
