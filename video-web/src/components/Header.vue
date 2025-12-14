<template>
  <header class="header">
    <div class="header-content">
      <div class="logo">
        <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
          <circle cx="20" cy="20" r="18" fill="#FF6B6B" />
          <path d="M16 14L28 20L16 26V14Z" fill="white" />
        </svg>
        <h1>视频分享平台</h1>
      </div>
      <nav class="nav">
        <div class="search-box">
          <input type="text">
          <button>
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
              <circle cx="8" cy="8" r="6" stroke="currentColor" stroke-width="2" />
              <path d="M12 12L16 16" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
            </svg>
          </button>
        </div>
        <button class="upload-btn">上传视频</button>
      </nav>
    </div>
  </header>
</template>

<script setup>

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

.nav {
  display: flex;
  align-items: center;
  gap: 20px;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-box input {
  width: 350px;
  height: 40px;
  padding: 0 45px 0 20px;
  border: none;
  border-radius: 25px;
  font-size: 14px;
  outline: none;
  background: rgba(255, 255, 255, 0.9);
  transition: all 0.3s ease;
}

.search-box input:focus {
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.search-box button {
  position: absolute;
  right: 5px;
  width: 35px;
  height: 35px;
  border: none;
  border-radius: 50%;
  background: #667eea;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.search-box button:hover {
  background: #5568d3;
  transform: scale(1.1);
}

.upload-btn {
  padding: 10px 25px;
  border: 2px solid white;
  border-radius: 25px;
  background: transparent;
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-btn:hover {
  background: white;
  color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.3);
}
</style>

