<template>
  <div class="container my-posts-container">
    <div class="header-section">
      <div>
        <h1 class="page-title">Mis Publicaciones 📚</h1>
        <p class="page-subtitle">Gestiona todas las entradas que has compartido en la plataforma.</p>
      </div>

      <router-link to="/create-post" class="btn btn-primary">
        + Crear nueva publicación
      </router-link>
    </div>

    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Cargando tus publicaciones...</p>
    </div>

    <div v-else-if="posts.length === 0" class="empty-state card">
      <span class="empty-icon">📂</span>
      <h3>Aún no has creado publicaciones</h3>
      <p>Empieza a escribir tu primera entrada ahora mismo.</p>
      <router-link to="/create-post" class="btn btn-primary" style="margin-top: 15px;">
        Crear publicación
      </router-link>
    </div>

    <div v-else class="my-posts-list">
      <div v-for="post in posts" :key="post.id" class="card post-item">
        <div class="post-info">
          <h2 class="post-title">
            <router-link :to="`/post/${post.id}`">{{ post.title }}</router-link>
          </h2>
          <span class="post-date">Creado el {{ formatDate(post.createdAt) }}</span>
          <p class="post-preview">{{ truncateContent(post.content, 120) }}</p>
        </div>

        <div class="post-actions">
          <router-link :to="`/post/${post.id}`" class="btn btn-secondary btn-sm">Ver</router-link>
          <router-link :to="`/edit-post/${post.id}`" class="btn btn-secondary btn-sm">✏️ Editar</router-link>
          <button @click="deletePost(post.id)" class="btn btn-danger btn-sm">🗑️ Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const posts = ref([])
const loading = ref(true)

const fetchMyPosts = async () => {
  loading.value = true
  try {
    const res = await fetch('/api/posts/mine', {
      credentials: 'include'
    })
    if (res.ok) {
      posts.value = await res.json()
    }
  } catch (err) {
    console.error('Error al cargar mis publicaciones:', err)
  } finally {
    loading.value = false
  }
}

const deletePost = async (id) => {
  if (!confirm('¿Estás seguro de eliminar esta publicación?')) return
  try {
    const res = await fetch(`/api/posts/${id}`, {
      method: 'DELETE',
      credentials: 'include'
    })
    if (res.ok) {
      await fetchMyPosts()
    }
  } catch (err) {
    console.error('Error al eliminar publicación:', err)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Intl.DateTimeFormat('es-ES', {
    day: 'numeric',
    month: 'short',
    year: 'numeric'
  }).format(new Date(dateStr))
}

const truncateContent = (text, maxLength) => {
  if (!text) return ''
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}

onMounted(() => {
  fetchMyPosts()
})
</script>

<style scoped>
.my-posts-container {
  padding-top: 40px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 16px;
}

.page-title {
  font-size: 2.2rem;
  font-weight: 800;
}

.page-subtitle {
  color: var(--text-secondary);
}

.my-posts-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.post-info {
  flex: 1;
  min-width: 280px;
}

.post-title {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 4px;
}

.post-title a:hover {
  color: var(--accent-primary);
}

.post-date {
  font-size: 0.8rem;
  color: var(--text-muted);
  display: block;
  margin-bottom: 8px;
}

.post-preview {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.post-actions {
  display: flex;
  gap: 10px;
}

.loading-state, .empty-state {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary);
}
</style>
