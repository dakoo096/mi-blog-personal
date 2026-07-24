import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import { authState } from './store/auth'

// Cargar estado de sesión actual antes de montar la app
authState.fetchCurrentUser()

const app = createApp(App)
app.use(router)
app.mount('#app')
