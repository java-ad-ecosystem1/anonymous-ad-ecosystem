<template>
  <header class="topbar">
    <div class="brand">广告管理系统</div>
    <div class="user-area">
      <div class="user">欢迎，{{ username }}</div>
      <button class="logout-button" @click="logout">登出</button>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const username = ref(localStorage.getItem('username') || '游客')
const router = useRouter()

const logout = () => {
  localStorage.removeItem('username')
  router.push({ name: 'Login' })
}
</script>

<style scoped>
.topbar {
  height: 64px;
  display:flex;
  align-items:center;
  justify-content:space-between;
  padding: 0 24px;
  background: linear-gradient(90deg,#2563eb 0%,#1e40af 100%);
  color: #fff;
  box-shadow: 0 1px 4px rgba(16,24,40,0.06);
}
.brand { font-weight:700; font-size:18px }
.user-area { display:flex; align-items:center; gap:12px }
.user { font-size:14px; opacity:0.95 }
.logout-button {
  background: rgba(255,255,255,0.06);
  border: 1px solid rgba(255,255,255,0.12);
  color: #fff;
  padding:6px 10px;
  border-radius:8px;
  cursor:pointer;
  transition: background .12s ease, transform .06s ease;
}
.logout-button:hover { background: rgba(255,255,255,0.12); transform: translateY(-1px) }
</style>
