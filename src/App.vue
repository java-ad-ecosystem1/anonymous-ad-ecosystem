<script setup>
import Header from './components/Header.vue'
</script>

<template>
  <div class="app">
    <Header />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  margin-top: 140px;
  padding-bottom: 40px;
  background: #f5f5f5;
  min-height: calc(100vh - 140px);
}
</style>
