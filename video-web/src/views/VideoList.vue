<template>
  <div class="video-list">
    <!-- 添加搜索区域 -->
    <div class="search-section">
      <div class="search-container">
        <input 
          v-model="searchKeyword"
          type="text" 
          placeholder="输入视频名称搜索..."
          class="search-input"
          @keyup.enter="handleSearch"
          @input="handleSearchInput"
        />
        <button class="search-btn" @click="handleSearch">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <path d="M14 14L10.3333 10.3333M11.7778 6.88889C11.7778 9.65411 9.65411 11.7778 6.88889 11.7778C4.12367 11.7778 2 9.65411 2 6.88889C2 4.12367 4.12367 2 6.88889 2C9.65411 2 11.7778 4.12367 11.7778 6.88889Z" 
                  stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
          搜索
        </button>
        <button 
          v-if="searchKeyword" 
          class="clear-btn" 
          @click="clearSearch"
        >
          清除
        </button>
      </div>
      
      <!-- 搜索提示 -->
      <div v-if="searchKeyword && videos.length > 0" class="search-info">
        找到 {{ videos.length }} 个相关视频
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading"></div>
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-container">
      <svg width="80" height="80" viewBox="0 0 80 80" fill="none">
        <circle cx="40" cy="40" r="35" stroke="#ff6b6b" stroke-width="3" />
        <path d="M40 25V45" stroke="#ff6b6b" stroke-width="3" stroke-linecap="round" />
        <circle cx="40" cy="55" r="2" fill="#ff6b6b" />
      </svg>
      <p class="error-message">{{ error }}</p>
      <button class="retry-btn" @click="fetchVideoList">重新加载</button>
    </div>

    <!-- 视频列表 -->
    <div v-else-if="videos.length > 0">
      <div class="videos-grid">
        <div 
          v-for="video in videos" 
          :key="video.id"
          class="video-card"
          @click="goToDetail(video.id, video.videoType)"
        >
          <div class="video-thumbnail">
            <video 
              :src="getVideoUrl(video.videoUrl)"  
              preload="metadata"
              muted
              @mouseenter="handleVideoHover($event, true)"
              @mouseleave="handleVideoHover($event, false)"
            ></video>
            <div class="duration">{{ video.duration || '00:00' }}</div>
            <div class="play-overlay">
              <svg width="60" height="60" viewBox="0 0 60 60" fill="none">
                <circle cx="30" cy="30" r="28" fill="rgba(255, 255, 255, 0.95)" />
                <path d="M24 18L42 30L24 42V18Z" fill="#667eea" />
              </svg>
            </div>
          </div>
          <div class="video-info">
            <div class="video-details">
              <h3 class="video-title">{{ video.videoName }}</h3>
              <p class="video-author">{{ video.videoPublisher }}</p>
              <div class="video-meta">
                <span>{{ formatDate(video.createTime, 'yyyy-MM-dd HH:mm:ss') }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页组件 -->
      <div class="pagination" v-if="totalPages > 1">
        <button 
          class="page-btn" 
          :disabled="currentPage === 1"
          @click="changePage(currentPage - 1)"
        >
          上一页
        </button>
        
        <div class="page-numbers">
          <button
            v-for="page in visiblePages"
            :key="page"
            class="page-number"
            :class="{ active: page === currentPage }"
            :disabled="page === '...'"
            @click="changePage(page)"
          >
            {{ page }}
          </button>
        </div>
        
        <button 
          class="page-btn" 
          :disabled="currentPage === totalPages"
          @click="changePage(currentPage + 1)"
        >
          下一页
        </button>
      </div>
    </div>

    <!-- 空状态 - 区分搜索无结果和普通空状态 -->
    <div v-else class="empty-container">
      <svg width="120" height="120" viewBox="0 0 120 120" fill="none">
        <circle cx="60" cy="60" r="50" fill="#f0f0f0" />
        <path d="M45 50L75 60L45 70V50Z" fill="#ccc" />
      </svg>
      <p v-if="searchKeyword">没有找到"{{ searchKeyword }}"相关的视频</p>
      <p v-else>暂无视频</p>
      <button v-if="searchKeyword" class="clear-btn empty-btn" @click="clearSearch">
        查看所有视频
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { videoApi } from '../api/api'
import { formatDate } from '@/utils/date'
import { saveVideoType, getVideoType } from '@/utils/storage'
const currentVideoType = ref(getVideoType() || null)

const router = useRouter()
const route = useRoute()

const videos = ref([])
const loading = ref(false)
const error = ref('')
const total = ref(0)
const searchKeyword = ref('') // 搜索关键词
const searchDebounceTimer = ref(null) // 防抖计时器

// 分页相关
const pageSize = 12
const currentPage = ref(1)

// 获取视频列表
const fetchVideoList = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize
    }
    
    // 如果有搜索关键词，添加到参数中
    // 注意：后端参数是 videoName，对应数据库字段 video_name
    if (searchKeyword.value.trim()) {
      params.videoName = searchKeyword.value.trim()
    }
    
    const res = await videoApi.list(params)
    if(!res || !res.data || res.code !== 200) {
      error.value = res.message || '获取视频列表失败，请稍后重试'
      videos.value = []
      total.value = 0
      return
    }
    videos.value = res.data.list || []
    total.value = res.data.total
    
  } catch (err) {
    error.value = err.message || '获取视频列表失败，请稍后重试'
    videos.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 处理搜索输入（防抖）
const handleSearchInput = () => {
  clearTimeout(searchDebounceTimer.value)
  searchDebounceTimer.value = setTimeout(() => {
    handleSearch()
  }, 300) // 300毫秒防抖
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1 // 搜索时回到第一页
  fetchVideoList()
  
  // 更新URL查询参数
  const query = { ...route.query }
  if (searchKeyword.value.trim()) {
    query.search = searchKeyword.value.trim()
  } else {
    delete query.search
  }
  query.page = 1
  
  router.push({ query })
}

// 清除搜索
const clearSearch = () => {
  searchKeyword.value = ''
  currentPage.value = 1
  fetchVideoList()
  
  // 清除URL中的搜索参数
  const query = { ...route.query }
  delete query.search
  query.page = 1
  router.push({ query })
}

// 获取视频URL
const getVideoUrl = (videoUrl) => {
  if (!videoUrl) return ''
  if (videoUrl.startsWith('http://') || videoUrl.startsWith('https://')) {
    return videoUrl
  }
  // 添加后端访问路径前缀
  const baseUrl = 'http://localhost:8084'
  // 确保路径以 / 开头
  const path = videoUrl.startsWith('/') ? videoUrl : `/${videoUrl}`
  return `${baseUrl}${path}`
}

// 处理视频悬停效果
const handleVideoHover = (event, isHover) => {
  const videoElement = event.target
  if (isHover) {
    videoElement.play().catch(() => {
      // 如果自动播放失败，忽略错误
    })
  } else {
    videoElement.pause()
    videoElement.currentTime = 0
  }
}

// 切换页码
const changePage = (page) => {
  if (page === '...' || page < 1 || page > totalPages.value) return
  currentPage.value = page
  
  // 更新URL查询参数
  const query = { ...route.query }
  query.page = page
  if (searchKeyword.value.trim()) {
    query.search = searchKeyword.value.trim()
  }
  router.push({ query })
  
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const goToDetail = (id, videoType) => {
  if (videoType) {
    saveVideoType(videoType)
    currentVideoType.value = videoType
  }
  router.push(`/video/${id}`)
}

// 从路由查询参数获取页码和搜索词
onMounted(() => {
  const page = parseInt(route.query.page) || 1
  const search = route.query.search || ''
  
  currentPage.value = page
  searchKeyword.value = search
  
  fetchVideoList()
})

// 监听页码变化，重新获取数据
watch(currentPage, (newPage, oldPage) => {
  if (oldPage !== undefined && newPage !== oldPage) {
    fetchVideoList()
  }
})

// 监听路由变化，处理返回按钮等情况
watch(() => route.query, (newQuery) => {
  if (newQuery.search !== undefined && newQuery.search !== searchKeyword.value) {
    searchKeyword.value = newQuery.search || ''
  }
  
  const page = parseInt(newQuery.page) || 1
  if (page !== currentPage.value) {
    currentPage.value = page
  }
}, { immediate: true })
</script>

<style scoped>
.video-list {
  width: 100%;
}

/* 搜索区域样式 */
.search-section {
  margin-bottom: 30px;
  padding: 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.search-container {
  display: flex;
  gap: 12px;
  align-items: center;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  flex: 1;
  padding: 12px 20px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 15px;
  outline: none;
  transition: all 0.3s;
  font-family: inherit;
}

.search-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.search-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  white-space: nowrap;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.clear-btn {
  padding: 12px 20px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  background: white;
  color: #ff6b6b;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
}

.clear-btn:hover {
  background: #fff5f5;
  border-color: #ff6b6b;
  transform: translateY(-2px);
}

.search-info {
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
  color: #7f8c8d;
}

/* 空状态按钮 */
.empty-btn {
  margin-top: 20px;
  padding: 10px 24px;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.loading-container p {
  margin-top: 20px;
  font-size: 16px;
  color: #7f8c8d;
}

/* 错误状态 */
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.error-message {
  margin-top: 20px;
  font-size: 16px;
  color: #ff6b6b;
}

.retry-btn {
  margin-top: 20px;
  padding: 12px 32px;
  border: none;
  border-radius: 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.retry-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

/* 空状态 */
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.empty-container p {
  margin-top: 20px;
  font-size: 16px;
  color: #95a5a6;
  text-align: center;
}

.videos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 25px;
}

.video-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.video-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.video-thumbnail {
  position: relative;
  width: 100%;
  padding-top: 56.25%;
  overflow: hidden;
  background: #f0f0f0;
}

.video-thumbnail video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.video-card:hover .video-thumbnail video {
  transform: scale(1.1);
}

.duration {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 600;
}

.play-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.video-card:hover .play-overlay {
  opacity: 1;
}

.video-info {
  padding: 16px;
  display: flex;
  gap: 12px;
}

.video-details {
  flex: 1;
  min-width: 0;
}

.video-title {
  font-size: 15px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 6px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.video-author {
  font-size: 13px;
  color: #7f8c8d;
  margin: 0 0 4px 0;
}

.video-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #95a5a6;
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 40px;
  padding: 20px 0;
}

.page-btn {
  padding: 10px 20px;
  border: 1px solid #e0e0e0;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
  color: #5f6368;
  font-weight: 600;
}

.page-btn:hover:not(:disabled) {
  border-color: #667eea;
  color: #667eea;
  transform: translateY(-2px);
}

.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 8px;
}

.page-number {
  min-width: 40px;
  height: 40px;
  border: 1px solid #e0e0e0;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
  color: #5f6368;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.page-number:hover:not(.active):not(:disabled) {
  border-color: #667eea;
  color: #667eea;
  transform: translateY(-2px);
}

.page-number.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: #667eea;
  color: white;
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.page-number:disabled {
  cursor: default;
  pointer-events: none;
  opacity: 0.6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-container {
    flex-direction: column;
  }
  
  .search-input,
  .search-btn,
  .clear-btn {
    width: 100%;
  }
  
  .videos-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 20px;
  }
  
  .pagination {
    flex-wrap: wrap;
    gap: 8px;
    padding: 20px 10px;
  }
  
  .page-btn {
    padding: 8px 16px;
    font-size: 13px;
  }
  
  .page-number {
    min-width: 36px;
    height: 36px;
    font-size: 13px;
  }
}
</style>
