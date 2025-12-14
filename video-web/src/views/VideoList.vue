<template>
  <div class="video-list">
    <div class="filters">
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
          @click="goToDetail(video.id,video.videoType)"
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

    <!-- 空状态 -->
    <div v-else class="empty-container">
      <svg width="120" height="120" viewBox="0 0 120 120" fill="none">
        <circle cx="60" cy="60" r="50" fill="#f0f0f0" />
        <path d="M45 50L75 60L45 70V50Z" fill="#ccc" />
      </svg>
      <p>暂无视频</p>
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

// 分页相关
const pageSize = 12
const currentPage = ref(1)

// 总页数
const totalPages = computed(() => {
  if (total.value > 0) {
    // 如果接口返回了总数，使用总数计算
    return Math.ceil(total.value / pageSize)
  }
  // 否则使用列表长度计算
  return Math.ceil(videos.value.length / pageSize)
})

// 可见页码
const visiblePages = computed(() => {
  const pages = []
  const totalPageCount = totalPages.value
  const current = currentPage.value
  
  if (totalPageCount <= 7) {
    for (let i = 1; i <= totalPageCount; i++) {
      pages.push(i)
    }
  } else {
    if (current <= 4) {
      for (let i = 1; i <= 5; i++) pages.push(i)
      pages.push('...')
      pages.push(totalPageCount)
    } else if (current >= totalPageCount - 3) {
      pages.push(1)
      pages.push('...')
      for (let i = totalPageCount - 4; i <= totalPageCount; i++) pages.push(i)
    } else {
      pages.push(1)
      pages.push('...')
      for (let i = current - 1; i <= current + 1; i++) pages.push(i)
      pages.push('...')
      pages.push(totalPageCount)
    }
  }
  
  return pages
})

// 获取视频列表
const fetchVideoList = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize
    }
    const res = await videoApi.list(params)
    if(!res || !res.data || res.code !== 200) {
      error.value = res.message || '获取新闻列表失败，请稍后重试'
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
    // 鼠标悬停时播放视频
    videoElement.play().catch(() => {
      // 如果自动播放失败，忽略错误
    })
  } else {
    // 鼠标离开时暂停并重置视频
    videoElement.pause()
    videoElement.currentTime = 0
  }
}

// 切换页码
const changePage = (page) => {
  if (page === '...' || page < 1 || page > totalPages.value) return
  currentPage.value = page
  router.push({ query: { ...route.query, page } })
  window.scrollTo({ top: 0, behavior: 'smooth' })
  // 页码变化会触发 watch，自动调用 fetchVideoList
}

const goToDetail = (id,videoType) => {
if (videoType) {
    saveVideoType(videoType)
    currentVideoType.value = videoType
  }
  router.push(`/video/${id}`)
}

// 从路由查询参数获取页码
onMounted(() => {
  const page = parseInt(route.query.page) || 1
  currentPage.value = page
  fetchVideoList()
})

// 监听页码变化，重新获取数据
watch(currentPage, (newPage, oldPage) => {
  // 避免初始化时重复请求
  if (oldPage !== undefined && newPage !== oldPage) {
    fetchVideoList()
  }
})
</script>

<style scoped>
.video-list {
  width: 100%;
}

.filters {
  display: flex;
  gap: 12px;
  margin-bottom: 30px;
  overflow-x: auto;
  padding-bottom: 10px;
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
}

.filters::-webkit-scrollbar {
  height: 6px;
}

.filters::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 3px;
}

.filter-btn {
  padding: 10px 24px;
  border: none;
  border-radius: 25px;
  background: white;
  color: #5f6368;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.filter-btn:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.filter-btn.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
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

.video-avatar {
  flex-shrink: 0;
}

.video-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
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

/* 响应式分页 */
@media (max-width: 768px) {
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
  
  .page-numbers {
    gap: 6px;
  }
}
</style>

