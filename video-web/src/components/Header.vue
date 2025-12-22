<template>
  <header class="header">
    <div class="header-content">
      <div class="logo" @click="goHome">
        <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
          <circle cx="20" cy="20" r="18" fill="#FF6B6B" />
          <path d="M16 14L28 20L16 26V14Z" fill="white" />
        </svg>
        <h1>视频分享平台</h1>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

// 跳转到首页
const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 70px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.header-content {
  max-width: 1600px;
  margin: 0 auto;
  padding: 0 30px;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 15px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo h1 {
  font-size: 24px;
  font-weight: 700;
  color: white;
  margin: 0;
}
</style>
