<template>
  <header class="navbar">
    <div class="container navbar-content">
      <router-link to="/" class="brand">
        <span class="brand-icon">✨</span>
        <span class="brand-text">Blog<span class="gradient-text"> Personal</span></span>
      </router-link>

      <nav class="nav-links">
        <router-link to="/" class="nav-link">Inicio</router-link>

        <!-- Theme Toggle Button (Oscuro / Claro) -->
        <button @click="toggleTheme" class="btn-theme-toggle"
          :title="currentTheme === 'dark' ? 'Cambiar a Modo Claro' : 'Cambiar a Modo Oscuro'">
          <span v-if="currentTheme === 'dark'">🌙</span>
          <span v-else>☀️</span>
        </button>

        <template v-if="authState.user">
          <router-link to="/my-posts" class="nav-link">Mis Publicaciones</router-link>

          <router-link to="/create-post" class="btn btn-primary btn-sm">
            <span>+ Crear Post</span>
          </router-link>

          <!-- Notification Bell -->
          <div class="notif-wrapper" ref="notifRef">
            <button @click="toggleNotifDropdown" class="btn-icon notif-btn" title="Notificaciones">
              🔔
              <span v-if="unreadCount > 0" class="notif-badge">{{ unreadCount > 9 ? '9+' : unreadCount }}</span>
            </button>

            <!-- Notification Dropdown -->
            <div v-if="showNotifDropdown" class="dropdown-popover notif-popover card">
              <div class="popover-header">
                <h3>🔔 Notificaciones</h3>
                <button v-if="notifications.length > 0" @click="markAllAsRead" class="btn-link">
                  Marcar todas como leídas
                </button>
              </div>

              <div class="popover-body">
                <div v-if="loadingNotifs" class="notif-loading">Cargando...</div>
                <div v-else-if="notifications.length === 0" class="notif-empty">No tienes notificaciones por el momento
                </div>
                <div v-else class="notif-list">
                  <div v-for="n in notifications" :key="n.id" class="notif-item" :class="{ unread: !n.read }"
                    @click="handleNotifClick(n)">
                    <div class="notif-icon">{{ n.actionType === 'MENTION' ? '🏷️' : '💬' }}</div>
                    <div class="notif-content">
                      <p class="notif-text">
                        <strong>{{ n.actor ? (n.actor.name || n.actor.username) : 'Alguien' }}</strong>
                        <span v-if="n.actionType === 'MENTION'"> te respondió / mencionó en un comentario:</span>
                        <span v-else> comentó en tu publicación:</span>
                        <em> "{{ truncateText(n.commentContent, 60) }}"</em>
                      </p>
                      <span class="notif-time">{{ formatDate(n.createdAt) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- User Menu Dropdown -->
          <div class="user-dropdown-wrapper" ref="userMenuRef">
            <button @click="toggleUserDropdown" class="user-badge-btn">
              👤 {{ authState.user.name || authState.user.username }} ▾
            </button>

            <div v-if="showUserDropdown" class="dropdown-popover user-popover card">
              <button @click="openProfileModal" class="dropdown-item">
                ⚙️ Administrar mi perfil
              </button>
              <div class="dropdown-divider"></div>
              <button @click="handleLogout" class="dropdown-item text-danger">
                🚪 Cerrar sesión
              </button>
            </div>
          </div>
        </template>

        <template v-else-if="!authState.loading">
          <router-link to="/login" class="nav-link">Iniciar Sesión</router-link>
          <router-link to="/register" class="btn btn-primary btn-sm">Registrarse</router-link>
        </template>
      </nav>
    </div>

    <!-- Modal Editar Perfil -->
    <ProfileModal :isOpen="isProfileModalOpen" @close="isProfileModalOpen = false" />
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { authState } from '../store/auth'
import { useRouter } from 'vue-router'
import ProfileModal from './ProfileModal.vue'

const router = useRouter()

const showUserDropdown = ref(false)
const showNotifDropdown = ref(false)
const isProfileModalOpen = ref(false)

const currentTheme = ref('dark')

const unreadCount = ref(0)
const notifications = ref([])
const loadingNotifs = ref(false)

const notifRef = ref(null)
const userMenuRef = ref(null)
let pollTimer = null

const applyTheme = (theme) => {
  currentTheme.value = theme
  document.documentElement.setAttribute('data-theme', theme)
  localStorage.setItem('theme', theme)
}

const toggleTheme = () => {
  const nextTheme = currentTheme.value === 'dark' ? 'light' : 'dark'
  applyTheme(nextTheme)
}

const toggleUserDropdown = () => {
  showUserDropdown.value = !showUserDropdown.value
  showNotifDropdown.value = false
}

const toggleNotifDropdown = async () => {
  showNotifDropdown.value = !showNotifDropdown.value
  showUserDropdown.value = false
  if (showNotifDropdown.value) {
    await fetchNotifications()
  }
}

const openProfileModal = () => {
  showUserDropdown.value = false
  isProfileModalOpen.value = true
}

const handleLogout = async () => {
  showUserDropdown.value = false
  await authState.logout()
  router.push('/')
}

const fetchUnreadCount = async () => {
  if (!authState.user) return
  try {
    const res = await fetch('/api/notifications/unread-count', { credentials: 'include' })
    if (res.ok) {
      const data = await res.json()
      unreadCount.value = data.unreadCount || 0
    }
  } catch (err) {
    console.error('Error fetching unread count:', err)
  }
}

const fetchNotifications = async () => {
  loadingNotifs.value = true
  try {
    const res = await fetch('/api/notifications', { credentials: 'include' })
    if (res.ok) {
      notifications.value = await res.json()
    }
  } catch (err) {
    console.error('Error fetching notifications:', err)
  } finally {
    loadingNotifs.value = false
  }
}

const markAllAsRead = async () => {
  try {
    await fetch('/api/notifications/read-all', { method: 'PUT', credentials: 'include' })
    unreadCount.value = 0
    notifications.value.forEach(n => n.read = true)
  } catch (err) {
    console.error('Error marking all read:', err)
  }
}

const handleNotifClick = async (notif) => {
  if (!notif.read) {
    try {
      await fetch(`/api/notifications/${notif.id}/read`, { method: 'PUT', credentials: 'include' })
      notif.read = true
      if (unreadCount.value > 0) unreadCount.value--
    } catch (err) {
      console.error('Error marking read:', err)
    }
  }
  showNotifDropdown.value = false
  if (notif.post && notif.post.id) {
    router.push(`/post/${notif.post.id}`)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Intl.DateTimeFormat('es-ES', {
    day: 'numeric',
    month: 'short',
    hour: '2-digit',
    minute: '2-digit'
  }).format(new Date(dateStr))
}

const truncateText = (text, maxLength) => {
  if (!text) return ''
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}

const handleClickOutside = (e) => {
  if (userMenuRef.value && !userMenuRef.value.contains(e.target)) {
    showUserDropdown.value = false
  }
  if (notifRef.value && !notifRef.value.contains(e.target)) {
    showNotifDropdown.value = false
  }
}

watch(() => authState.user, (user) => {
  if (user) {
    fetchUnreadCount()
  } else {
    unreadCount.value = 0
    notifications.value = []
  }
})

onMounted(() => {
  const savedTheme = localStorage.getItem('theme') || 'dark'
  applyTheme(savedTheme)

  document.addEventListener('click', handleClickOutside)
  if (authState.user) {
    fetchUnreadCount()
  }
  pollTimer = setInterval(() => {
    if (authState.user) fetchUnreadCount()
  }, 15000)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  if (pollTimer) clearInterval(pollTimer)
})
</script>

<style scoped>
.navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  background: var(--bg-primary);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid var(--border-color);
  padding: 14px 0;
}

.navbar-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1.4rem;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.gradient-text {
  background: var(--accent-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 16px;
}

.nav-link {
  font-weight: 600;
  font-size: 0.95rem;
  color: var(--text-secondary);
  transition: var(--transition);
  padding: 6px 10px;
  border-radius: var(--radius-sm);
}

.nav-link:hover,
.nav-link.router-link-active {
  color: var(--text-primary);
  background: rgba(255, 255, 255, 0.05);
}

.btn-theme-toggle {
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  font-weight: 600;
  font-size: 0.85rem;
  padding: 6px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: var(--transition);
}

.btn-theme-toggle:hover {
  background: rgba(255, 255, 255, 0.12);
  border-color: var(--border-glow);
}

.btn-sm {
  padding: 8px 16px;
  font-size: 0.85rem;
}

/* Notif Bell */
.notif-wrapper {
  position: relative;
}

.btn-icon {
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid var(--border-color);
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.1rem;
  position: relative;
  transition: var(--transition);
}

.btn-icon:hover {
  background: rgba(255, 255, 255, 0.12);
  border-color: rgba(255, 255, 255, 0.2);
}

.notif-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background: #ef4444;
  color: white;
  font-size: 0.7rem;
  font-weight: 800;
  padding: 2px 6px;
  border-radius: 10px;
  border: 2px solid var(--bg-primary);
}

/* Dropdown Popovers */
.user-dropdown-wrapper {
  position: relative;
}

.user-badge-btn {
  font-weight: 600;
  font-size: 0.9rem;
  color: var(--text-primary);
  background: rgba(255, 255, 255, 0.06);
  padding: 8px 14px;
  border-radius: 20px;
  border: 1px solid var(--border-color);
  transition: var(--transition);
}

.user-badge-btn:hover {
  background: rgba(255, 255, 255, 0.12);
}

.dropdown-popover {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  background: var(--popover-bg);
  border: 1px solid var(--border-color);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4);
  border-radius: var(--radius-md);
  z-index: 200;
  animation: fadeIn 0.15s ease-out;
}

.user-popover {
  width: 220px;
  padding: 8px;
}

.dropdown-item {
  width: 100%;
  padding: 10px 14px;
  background: none;
  border: none;
  color: var(--text-primary);
  text-align: left;
  font-size: 0.9rem;
  font-weight: 600;
  border-radius: var(--radius-sm);
  transition: var(--transition);
}

.dropdown-item:hover {
  background: rgba(255, 255, 255, 0.08);
}

.text-danger {
  color: #f87171;
}

.text-danger:hover {
  background: rgba(239, 68, 68, 0.15);
}

.dropdown-divider {
  height: 1px;
  background: var(--border-color);
  margin: 6px 0;
}

/* Notif Popover */
.notif-popover {
  width: 360px;
  padding: 16px;
}

.popover-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 8px;
}

.popover-header h3 {
  font-size: 1rem;
  font-weight: 700;
}

.btn-link {
  background: none;
  border: none;
  color: var(--accent-primary);
  font-size: 0.78rem;
  font-weight: 600;
  cursor: pointer;
}

.btn-link:hover {
  text-decoration: underline;
}

.notif-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 340px;
  overflow-y: auto;
}

.notif-item {
  display: flex;
  gap: 12px;
  padding: 10px;
  border-radius: var(--radius-sm);
  background: rgba(255, 255, 255, 0.03);
  cursor: pointer;
  transition: var(--transition);
}

.notif-item:hover {
  background: rgba(255, 255, 255, 0.08);
}

.notif-item.unread {
  background: rgba(59, 130, 246, 0.12);
  border-left: 3px solid var(--accent-primary);
}

.notif-text {
  font-size: 0.85rem;
  line-height: 1.35;
  color: var(--text-primary);
}

.notif-time {
  font-size: 0.75rem;
  color: var(--text-muted);
  display: block;
  margin-top: 4px;
}

.notif-loading,
.notif-empty {
  text-align: center;
  padding: 20px;
  font-size: 0.85rem;
  color: var(--text-muted);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-6px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
