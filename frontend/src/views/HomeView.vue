<template>
  <div class="home-view">
    <!-- LANDING VIEW (Para usuarios NO autenticados) -->
    <template v-if="!authState.user && !authState.loading">
      <!-- Hero Landing -->
      <section class="hero-landing">
        <div class="container hero-content">
          <span class="hero-badge">✨ Plataforma de Blogs & Comunicación</span>
          <h1 class="hero-title">
            Escribe, Comparte y Conecta con la <br />
            <span class="gradient-text">Comunidad de Desarrolladores</span>
          </h1>
          <p class="hero-subtitle">
            Crea publicaciones de tecnología, responde comentarios con menciones <strong>@usuario</strong> en tiempo
            real y recibe notificaciones instantáneas en tu campanita.
          </p>

          <div class="hero-actions">
            <router-link to="/register" class="btn btn-primary btn-lg">
              🚀 Unirse a la Comunidad
            </router-link>
            <router-link to="/login" class="btn btn-secondary btn-lg">
              🔑 Iniciar Sesión
            </router-link>
          </div>
        </div>
      </section>

      <!-- Features Showcase Section -->
      <section class="features-section">
        <div class="container">
          <div class="section-header">
            <h2 class="section-title">¿Qué puedes hacer en <span class="gradient-text">Blog Personal</span>?</h2>
            <p class="section-subtitle">Todo lo que necesitas para compartir conocimiento y debatir con otros
              desarrolladores.</p>
          </div>

          <div class="features-grid">
            <div class="card feature-card">
              <div class="feature-icon">📝</div>
              <h3>Publica tus Artículos</h3>
              <p>Redacta publicaciones sobre programación, arquitectura y tecnología con una interfaz limpia e
                intuitiva.</p>
            </div>

            <div class="card feature-card">
              <div class="feature-icon">💬</div>
              <h3>Debates & Comentarios</h3>
              <p>Comenta en los posts de la comunidad con un diseño lateral cómodo que no interrumpe la lectura.</p>
            </div>

            <div class="card feature-card">
              <div class="feature-icon">🏷️</div>
              <h3>Menciones `@usuario`</h3>
              <p>Responde a cualquier usuario con el botón "Responder" para etiquetarlo directamente en la conversación.
              </p>
            </div>

            <div class="card feature-card">
              <div class="feature-icon">🔔</div>
              <h3>Campanita de Notificaciones</h3>
              <p>Recibe alertas instantáneas en tu campanita cada vez que te respondan o comenten en tus artículos.</p>
            </div>

            <div class="card feature-card">
              <div class="feature-icon">⚙️</div>
              <h3>Administración de Perfil</h3>
              <p>Gestiona tus datos personales y cambia tu contraseña de forma segura desde tu menú desplegable.</p>
            </div>

            <div class="card feature-card">
              <div class="feature-icon">🔍</div>
              <h3>Buscador en Tiempo Real</h3>
              <p>Encuentra contenido relevante al instante buscando por palabras clave en los títulos de publicaciones.
              </p>
            </div>
          </div>
        </div>
      </section>

      <!-- Call to Action Banner -->
      <section class="cta-section container">
        <div class="card cta-card">
          <h2>¿Listo para compartir tus ideas?</h2>
          <p>Crea tu cuenta en segundos y empieza a publicar tus conocimientos con la comunidad.</p>
          <router-link to="/register" class="btn btn-primary btn-lg" style="margin-top: 16px;">
            Crear mi cuenta gratis &rarr;
          </router-link>
        </div>
      </section>

      <!-- Preview Feed Section for Guests -->
      <section class="container main-content">
        <div class="feed-header">
          <h2>🔥 Explorar publicaciones recientes</h2>

          <div class="search-box">
            <input v-model="searchQuery" @input="handleSearch" type="text" placeholder="Buscar publicaciones..."
              class="form-input search-input" />
            <span class="search-icon">🔍</span>
          </div>
        </div>

        <div v-if="loading" class="loading-state">
          <div class="spinner"></div>
          <p>Cargando publicaciones...</p>
        </div>

        <div v-else-if="posts.length === 0" class="empty-state card">
          <span class="empty-icon">📝</span>
          <h3>No se encontraron publicaciones</h3>
          <p>Intenta con otra búsqueda o sé el primero en registrarte y publicar.</p>
        </div>

        <div v-else class="posts-grid">
          <PostCard v-for="post in posts" :key="post.id" :post="post" />
        </div>
      </section>
    </template>

    <!-- FEED VIEW (Para usuarios AUTENTICADOS) -->
    <template v-else>
      <section class="hero-feed">
        <div class="container hero-content">
          <h1 class="hero-title">
            ¡Hola de nuevo, <span class="gradient-text">{{ authState.user ? (authState.user.name ||
              authState.user.username) : '' }}</span>! 👋
          </h1>
          <p class="hero-subtitle">Explora los últimos artículos de la comunidad o redacta una nueva publicación.</p>

          <div class="feed-controls">
            <div class="search-box feed-search">
              <input v-model="searchQuery" @input="handleSearch" type="text" placeholder="Buscar por título..."
                class="form-input search-input" />
              <span class="search-icon">🔍</span>
            </div>

            <router-link to="/create-post" class="btn btn-primary btn-lg">
              + Crear Publicación
            </router-link>
          </div>
        </div>
      </section>

      <!-- Posts Grid -->
      <section class="container main-content">
        <div v-if="loading" class="loading-state">
          <div class="spinner"></div>
          <p>Cargando publicaciones...</p>
        </div>

        <div v-else-if="posts.length === 0" class="empty-state card">
          <span class="empty-icon">📝</span>
          <h3>No se encontraron publicaciones</h3>
          <p>Sé el primero en compartir una idea o intenta con otra búsqueda.</p>
          <router-link to="/create-post" class="btn btn-primary" style="margin-top: 15px;">
            Crear mi primera publicación
          </router-link>
        </div>

        <div v-else class="posts-grid">
          <PostCard v-for="post in posts" :key="post.id" :post="post" />
        </div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PostCard from '../components/PostCard.vue'
import { authState } from '../store/auth'

const posts = ref([])
const loading = ref(true)
const searchQuery = ref('')
let searchTimeout = null

const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await fetch('/api/posts')
    if (res.ok) {
      posts.value = await res.json()
    }
  } catch (err) {
    console.error('Error al cargar posts:', err)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(async () => {
    if (!searchQuery.value.trim()) {
      fetchPosts()
      return
    }
    loading.value = true
    try {
      const res = await fetch(`/api/posts/search?title=${encodeURIComponent(searchQuery.value)}`)
      if (res.ok) {
        posts.value = await res.json()
      }
    } catch (err) {
      console.error('Error en búsqueda:', err)
    } finally {
      loading.value = false
    }
  }, 300)
}

onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
/* Hero Landing */
.hero-landing {
  padding: 70px 0 50px;
  text-align: center;
  position: relative;
}

.hero-badge {
  display: inline-block;
  font-size: 0.85rem;
  font-weight: 700;
  color: var(--accent-primary);
  background: rgba(59, 130, 246, 0.12);
  border: 1px solid rgba(59, 130, 246, 0.3);
  padding: 6px 16px;
  border-radius: 20px;
  margin-bottom: 20px;
}

.hero-title {
  font-size: 3rem;
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 16px;
  letter-spacing: -1px;
}

.hero-subtitle {
  font-size: 1.15rem;
  color: var(--text-secondary);
  max-width: 680px;
  margin: 0 auto 36px;
  line-height: 1.6;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
}

.btn-lg {
  padding: 12px 28px;
  font-size: 1.05rem;
  border-radius: var(--radius-md);
}

/* Features Section */
.features-section {
  padding: 60px 0;
  background: var(--section-bg);
  border-y: 1px solid var(--border-color);
  margin: 40px 0;
}

.section-header {
  text-align: center;
  margin-bottom: 44px;
}

.section-title {
  font-size: 2.2rem;
  font-weight: 800;
  margin-bottom: 10px;
}

.section-subtitle {
  color: var(--text-secondary);
  font-size: 1.05rem;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}

.feature-card {
  padding: 28px;
  transition: var(--transition);
}

.feature-card:hover {
  transform: translateY(-6px);
}

.feature-icon {
  font-size: 2.4rem;
  margin-bottom: 16px;
}

.feature-card h3 {
  font-size: 1.2rem;
  font-weight: 700;
  margin-bottom: 10px;
  color: var(--text-primary);
}

.feature-card p {
  color: var(--text-secondary);
  font-size: 0.95rem;
  line-height: 1.55;
}

/* CTA Card */
.cta-section {
  margin: 40px auto 60px;
}

.cta-card {
  text-align: center;
  padding: 48px 30px;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.12) 0%, rgba(139, 92, 246, 0.12) 100%);
  border: 1px solid var(--border-glow);
}

.cta-card h2 {
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 10px;
}

.cta-card p {
  color: var(--text-secondary);
  font-size: 1.05rem;
  max-width: 540px;
  margin: 0 auto;
}

/* Hero Feed (Autenticado) */
.hero-feed {
  padding: 50px 0 30px;
  text-align: center;
}

.feed-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 24px;
  flex-wrap: wrap;
}

.feed-search {
  flex: 1;
  max-width: 440px;
  margin: 0;
}

.search-box {
  position: relative;
}

.search-input {
  padding-left: 45px;
  padding-right: 20px;
  height: 48px;
  font-size: 0.95rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.search-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.1rem;
}

.feed-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}

.feed-header h2 {
  font-size: 1.6rem;
  font-weight: 800;
}

.main-content {
  margin-top: 20px;
}

.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary);
}

.empty-icon {
  font-size: 3rem;
  display: block;
  margin-bottom: 12px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  border-top-color: var(--accent-primary);
  animation: spin 0.8s linear infinite;
  margin: 0 auto 16px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 2.2rem;
  }
}
</style>
