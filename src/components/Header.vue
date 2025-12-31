<template>
  <header class="header">
    <div class="header-container">
      <div class="header-left">
        <div class="logo">
          <span class="logo-icon">☁</span>
          <span class="logo-text">新闻网站</span>
        </div>
      </div>
      <div class="header-right">
        <button class="icon-btn">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="m21 21-4.35-4.35"></path>
          </svg>
        </button>
        <button class="icon-btn">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle cx="12" cy="7" r="4"></circle>
          </svg>
        </button>
      </div>
    </div>
    <div class="search-bar">
      <input 
        type="text" 
        placeholder="搜索" 
        class="search-input"
      />
      <button class="search-btn">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"></circle>
          <path d="m21 21-4.35-4.35"></path>
        </svg>
      </button>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: #1a1a1a;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 30px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #4a9eff;
  font-size: 18px;
  font-weight: 600;
}

.logo-icon {
  font-size: 24px;
}

.nav-menu {
  display: flex;
  gap: 20px;
}

.nav-item {
  color: #ffffff;
  text-decoration: none;
  font-size: 14px;
  padding: 8px 12px;
  border-radius: 4px;
  transition: all 0.3s;
}

.nav-item:hover,
.nav-item.active {
  background: rgba(74, 158, 255, 0.1);
  color: #4a9eff;
}

.header-right {
  display: flex;
  gap: 10px;
}

.icon-btn {
  background: transparent;
  border: none;
  color: #ffffff;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.icon-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.search-bar {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 15px;
  display: flex;
  gap: 10px;
}

.search-input {
  flex: 1;
  height: 44px;
  padding: 0 20px;
  border: 1px solid #e0e0e0;
  border-radius: 22px;
  font-size: 14px;
  outline: none;
  transition: all 0.3s;
}

.search-input:focus {
  border-color: #4a9eff;
  box-shadow: 0 0 0 3px rgba(74, 158, 255, 0.1);
}

.search-btn {
  width: 44px;
  height: 44px;
  background: #4a9eff;
  border: none;
  border-radius: 22px;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.search-btn:hover {
  background: #3a8eef;
  transform: scale(1.05);
}
</style>

