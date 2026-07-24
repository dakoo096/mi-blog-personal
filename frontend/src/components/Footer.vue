<template>
  <footer class="footer">
    <div class="container footer-content">
      <p>&copy; {{ new Date().getFullYear() }} Mi Blog Personal. Desarrollado con Spring Boot y Vue.js</p>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  margin-top: 80px;
  border-top: 1px solid var(--border-color);
  padding: 30px 0;
  text-align: center;
  color: var(--text-muted);
  font-size: 0.9rem;
}
</style>
