<template>
  <div class="card post-card">
    <div class="post-header">
      <span class="author-badge">
        ✍️ {{ post.user ? (post.user.name || post.user.username) : 'Anónimo' }}
      </span>
      <span class="post-date">{{ formatDate(post.createdAt) }}</span>
    </div>

    <h2 class="post-title">{{ post.title }}</h2>
    
    <p class="post-excerpt">{{ truncateContent(post.content, 140) }}</p>

    <div class="post-footer">
      <span class="comments-count">
        💬 {{ post.comments ? post.comments.length : 0 }} Comentarios
      </span>

      <router-link :to="`/post/${post.id}`" class="btn btn-secondary btn-sm">
        Leer publicación &rarr;
      </router-link>
    </div>
  </div>
</template>

<script setup>
defineProps({
  post: {
    type: Object,
    required: true
  }
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return new Intl.DateTimeFormat('es-ES', {
    day: 'numeric',
    month: 'short',
    year: 'numeric'
  }).format(date)
}

const truncateContent = (text, maxLength) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}
</script>

<style scoped>
.post-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: space-between;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.author-badge {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--accent-secondary);
}

.post-date {
  font-size: 0.8rem;
  color: var(--text-muted);
}

.post-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 10px;
  line-height: 1.35;
}

.post-excerpt {
  color: var(--text-secondary);
  font-size: 0.95rem;
  margin-bottom: 20px;
  flex-grow: 1;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid var(--border-color);
  padding-top: 14px;
}

.comments-count {
  font-size: 0.85rem;
  color: var(--text-muted);
  font-weight: 500;
}

.btn-sm {
  padding: 6px 14px;
  font-size: 0.85rem;
}
</style>
