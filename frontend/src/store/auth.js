import { reactive } from 'vue'

export const authState = reactive({
  user: null,
  loading: true,
  error: null,

  async fetchCurrentUser() {
    this.loading = true
    try {
      const response = await fetch('/api/users/me', {
        credentials: 'include'
      })
      if (response.ok) {
        this.user = await response.json()
      } else {
        this.user = null
      }
    } catch (err) {
      console.error('Error al obtener usuario actual:', err)
      this.user = null
    } finally {
      this.loading = false
    }
  },

  async login(username, password) {
    this.error = null
    try {
      const res = await fetch('/api/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
        credentials: 'include'
      })
      const data = await res.json()
      if (res.ok) {
        this.user = data
        return { success: true }
      } else {
        this.error = data.error || 'Credenciales inválidas'
        return { success: false, message: this.error }
      }
    } catch (err) {
      this.error = 'Error de conexión con el servidor'
      return { success: false, message: this.error }
    }
  },

  async register(userData) {
    this.error = null
    try {
      const res = await fetch('/api/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
      })
      const data = await res.json()
      if (res.ok) {
        return { success: true, message: data.message }
      } else {
        this.error = data.error || 'Error al registrar usuario'
        return { success: false, message: this.error }
      }
    } catch (err) {
      this.error = 'Error de conexión con el servidor'
      return { success: false, message: this.error }
    }
  },

  async logout() {
    try {
      await fetch('/api/users/logout', {
        method: 'POST',
        credentials: 'include'
      })
    } catch (err) {
      console.error('Error al cerrar sesión:', err)
    } finally {
      this.user = null
    }
  }
})
