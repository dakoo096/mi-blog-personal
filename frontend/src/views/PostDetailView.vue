<template>
  <div class="container post-detail-container">
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Cargando publicación...</p>
    </div>

    <div v-else-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <div v-else-if="post" class="post-detail-wrapper">
      <router-link to="/" class="back-link">&larr; Volver al inicio</router-link>

      <div class="layout-grid">
        <!-- Columna Izquierda: Publicación (70% aprox, scroll interno dentro de 100vh) -->
        <main class="main-column">
          <article class="card article-card scrollable-post">
            <header class="article-header">
              <div class="meta-row">
                <span class="badge">✍️ {{ post.user ? (post.user.name || post.user.username) : 'Anónimo' }}</span>
                <span class="meta-date">Publicado el {{ formatDate(post.createdAt) }}</span>
              </div>
              <h1 class="article-title">{{ post.title }}</h1>
            </header>

            <div class="article-body">
              <p v-for="(paragraph, index) in paragraphs" :key="index">
                {{ paragraph }}
              </p>
            </div>

            <div v-if="isOwner" class="owner-actions">
              <router-link :to="`/edit-post/${post.id}`" class="btn btn-secondary btn-sm">✏️ Editar</router-link>
              <button @click="deletePost" class="btn btn-danger btn-sm">🗑️ Eliminar</button>
            </div>
          </article>
        </main>

        <!-- Columna Derecha: Comentarios Sidebar (30% aprox) -->
        <aside class="sidebar-column">
          <section class="card comments-section">
            <div class="comments-header">
              <h3>💬 Comentarios</h3>
              <span class="comments-badge">{{ post.comments ? post.comments.length : 0 }}</span>
            </div>

            <!-- Formulario de comentario -->
            <form v-if="authState.user" @submit.prevent="addComment" class="comment-form">
              <div class="form-group">
                <textarea 
                  ref="commentTextareaRef"
                  v-model="newComment" 
                  placeholder="Escribe tu comentario..." 
                  class="form-textarea comment-input"
                  rows="3"
                  required
                ></textarea>
              </div>
              <button type="submit" class="btn btn-primary btn-sm btn-block" :disabled="submittingComment">
                {{ submittingComment ? 'Enviando...' : 'Comentar' }}
              </button>
            </form>

            <div v-else class="alert alert-secondary login-prompt">
              <router-link to="/login" class="gradient-link">Inicia sesión</router-link> para dejar un comentario.
            </div>

            <!-- Lista de comentarios -->
            <div class="comments-list">
              <div v-if="!post.comments || post.comments.length === 0" class="no-comments">
                Aún no hay comentarios. ¡Sé el primero en opinar!
              </div>

              <div v-for="comment in post.comments" :key="comment.id" class="comment-item">
                <div class="comment-header">
                  <span class="comment-author">
                    👤 {{ comment.user ? (comment.user.name || comment.user.username) : 'Usuario' }}
                  </span>
                  <span class="comment-date">{{ formatDateShort(comment.createdAt) }}</span>
                </div>

                <p class="comment-text">{{ comment.content }}</p>

                <div class="comment-actions">
                  <button 
                    v-if="authState.user" 
                    @click="replyToUser(comment.user ? (comment.user.username || comment.user.name) : 'Usuario')"
                    class="btn-reply-comment"
                  >
                    ↩️ Responder
                  </button>

                  <button 
                    v-if="authState.user && comment.user && comment.user.id === authState.user.id" 
                    @click="deleteComment(comment.id)"
                    class="btn-delete-comment"
                    title="Eliminar comentario"
                  >
                    🗑️ Eliminar
                  </button>
                </div>
              </div>
            </div>
          </section>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { authState } from '../store/auth'

const route = useRoute()
const router = useRouter()

const post = ref(null)
const loading = ref(true)
const error = ref(null)
const newComment = ref('')
const submittingComment = ref(false)
const commentTextareaRef = ref(null)

const isOwner = computed(() => {
  if (!post.value || !authState.user) return false
  return post.value.user && post.value.user.id === authState.user.id
})

const paragraphs = computed(() => {
  if (!post.value || !post.value.content) return []
  return post.value.content.split('\n\n')
})

const fetchPost = async () => {
  loading.value = true
  error.value = null
  try {
    const res = await fetch(`/api/posts/${route.params.id}`)
    if (res.ok) {
      post.value = await res.json()
    } else {
      error.value = 'Publicación no encontrada.'
    }
  } catch (err) {
    error.value = 'Error al cargar la publicación.'
  } finally {
    loading.value = false
  }
}

const addComment = async () => {
  if (!newComment.value.trim()) return
  submittingComment.value = true
  try {
    const res = await fetch('/api/comments', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        postId: post.value.id,
        content: newComment.value
      }),
      credentials: 'include'
    })
    if (res.ok) {
      newComment.value = ''
      await fetchPost()
    }
  } catch (err) {
    console.error('Error al agregar comentario:', err)
  } finally {
    submittingComment.value = false
  }
}

const replyToUser = (username) => {
  const mention = `@${username} `
  if (!newComment.value.startsWith(mention)) {
    newComment.value = mention + newComment.value
  }
  nextTick(() => {
    if (commentTextareaRef.value) {
      commentTextareaRef.value.focus()
    }
  })
}

const deleteComment = async (commentId) => {
  if (!confirm('¿Estás seguro de eliminar este comentario?')) return
  try {
    const res = await fetch(`/api/comments/${commentId}`, {
      method: 'DELETE',
      credentials: 'include'
    })
    if (res.ok) {
      await fetchPost()
    }
  } catch (err) {
    console.error('Error al eliminar comentario:', err)
  }
}

const deletePost = async () => {
  if (!confirm('¿Estás seguro de eliminar esta publicación de forma permanente?')) return
  try {
    const res = await fetch(`/api/posts/${post.value.id}`, {
      method: 'DELETE',
      credentials: 'include'
    })
    if (res.ok) {
      router.push('/')
    }
  } catch (err) {
    console.error('Error al eliminar publicación:', err)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Intl.DateTimeFormat('es-ES', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  }).format(new Date(dateStr))
}

const formatDateShort = (dateStr) => {
  if (!dateStr) return ''
  return new Intl.DateTimeFormat('es-ES', {
    day: 'numeric',
    month: 'short',
    hour: '2-digit',
    minute: '2-digit'
  }).format(new Date(dateStr))
}

onMounted(() => {
  fetchPost()
})
</script>

<style scoped>
.post-detail-container {
  padding-top: 24px;
  max-width: 1560px;
}

.back-link {
  display: inline-block;
  margin-bottom: 16px;
  color: var(--text-secondary);
  font-weight: 600;
  font-size: 0.9rem;
  transition: var(--transition);
}

.back-link:hover {
  color: var(--accent-primary);
  transform: translateX(-4px);
}

/* Grid Layout: 70% Post | 30% Sidebar */
.layout-grid {
  display: grid;
  grid-template-columns: 1fr 400px;
  gap: 32px;
  align-items: start;
}

/* Post limitado al 100vh de la pantalla con scroll interno */
.scrollable-post {
  padding: 32px;
  max-height: calc(100vh - 160px);
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: rgba(139, 92, 246, 0.4) transparent;
}

.scrollable-post::-webkit-scrollbar {
  width: 6px;
}

.scrollable-post::-webkit-scrollbar-thumb {
  background: rgba(139, 92, 246, 0.4);
  border-radius: 4px;
}

.article-header {
  margin-bottom: 24px;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 18px;
}

.meta-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.meta-date {
  color: var(--text-muted);
  font-size: 0.85rem;
}

.article-title {
  font-size: 2.2rem;
  font-weight: 800;
  line-height: 1.25;
}

.article-body p {
  font-size: 1.05rem;
  line-height: 1.85;
  color: var(--text-secondary);
  margin-bottom: 20px;
  white-space: pre-wrap;
}

.owner-actions {
  display: flex;
  gap: 12px;
  margin-top: 28px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
}

/* Sidebar Comentarios */
.sidebar-column {
  position: sticky;
  top: 90px;
}

.comments-section {
  padding: 20px;
  max-height: calc(100vh - 160px);
  display: flex;
  flex-direction: column;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.comments-header h3 {
  font-size: 1.1rem;
  font-weight: 700;
}

.comments-badge {
  background: rgba(139, 92, 246, 0.2);
  color: var(--accent-secondary);
  font-size: 0.85rem;
  font-weight: 700;
  padding: 2px 10px;
  border-radius: 12px;
  border: 1px solid rgba(139, 92, 246, 0.3);
}

.comment-input {
  min-height: 80px;
  font-size: 0.9rem;
}

.btn-block {
  width: 100%;
}

.login-prompt {
  font-size: 0.85rem;
  margin-bottom: 16px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 14px;
  overflow-y: auto;
  flex: 1;
  padding-right: 4px;
}

.no-comments {
  color: var(--text-muted);
  font-style: italic;
  font-size: 0.88rem;
  text-align: center;
  padding: 20px 0;
}

.comment-item {
  background: var(--comment-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 12px 14px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.comment-author {
  font-weight: 600;
  font-size: 0.85rem;
  color: var(--accent-secondary);
}

.comment-date {
  font-size: 0.75rem;
  color: var(--text-muted);
}

.comment-text {
  color: var(--text-primary);
  font-size: 0.88rem;
  line-height: 1.45;
  white-space: pre-wrap;
}

.comment-actions {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-top: 8px;
}

.btn-reply-comment {
  background: none;
  border: none;
  color: var(--accent-primary);
  font-size: 0.75rem;
  font-weight: 600;
  cursor: pointer;
  opacity: 0.85;
  transition: var(--transition);
}

.btn-reply-comment:hover {
  opacity: 1;
  text-decoration: underline;
}

.btn-delete-comment {
  background: none;
  border: none;
  color: #f87171;
  font-size: 0.75rem;
  cursor: pointer;
  opacity: 0.85;
  transition: var(--transition);
}

.btn-delete-comment:hover {
  opacity: 1;
  text-decoration: underline;
}

.gradient-link {
  color: var(--accent-primary);
  font-weight: 600;
}

/* Responsivo */
@media (max-width: 992px) {
  .layout-grid {
    grid-template-columns: 1fr;
  }

  .scrollable-post {
    max-height: none;
  }

  .sidebar-column {
    position: static;
  }

  .comments-section {
    max-height: none;
  }
}
</style>
