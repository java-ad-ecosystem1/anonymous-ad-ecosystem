<script setup>
import Header from './components/Header.vue'
</script>

<template>
  <div class="app">
    <Header />
    <div class="main-container">
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
.app {
  min-height: 100vh;
  background: #f5f7fa;
}

.main-container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 90px 30px 30px;
}

.content {
  width: 100%;
}
</style>
