<template>
  <div class="news-list-container">
    <div class="page-title">
      <h1>新闻</h1>
    </div>
    
    <div class="content-wrapper">
      <!-- 左侧内容区域 -->
      <div class="main-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="loading-text">加载中...</div>
        </div>
        
        <!-- 错误状态 -->
        <div v-else-if="error" class="error-container">
          <div class="error-text">{{ error }}</div>
          <button class="retry-btn" @click="fetchNewsList">重试</button>
        </div>
        
        <!-- 新闻列表 -->
        <div v-else class="news-list">
          <div v-if="paginatedNews.length === 0" class="empty-container">
            <div class="empty-text">暂无新闻数据</div>
          </div>
          
          <div 
            v-for="item in paginatedNews" 
            :key="item.id" 
            class="news-item"
            @click="goToDetail(item.id, item.newsType)"
          >
            <div class="news-thumbnail">
              <img :src="getImageUrl(item.imgUrl)" :alt="item.title" />
            </div>
            
            <div class="news-content">
              <div class="news-category">{{ item.source }}</div>
              <h2 class="news-title">{{ item.title }}</h2>
              <p class="news-description">{{ item.describe }}</p>
              
              <div class="news-meta">
                <span class="meta-item">创建时间: {{ formatDate(item.createTime, 'yyyy-MM-dd HH:mm:ss') }}</span>
              </div>
              
              <div class="news-tags">
                <span 
                  v-for="tag in getTags(item.label)" 
                  :key="tag" 
                  class="tag"
                >
                  {{ tag }}
                </span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="pagination" v-if="!loading && !error && totalPages > 1">
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
      
      <!-- 右侧广告位 -->
      <AdSidebar :news-type="currentNewsType" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { newsApi } from '../api/api'
import { formatDate } from '@/utils/date'
import { saveNewsType, getNewsType } from '@/utils/storage'
import AdSidebar from '@/components/AdSidebar.vue'

const router = useRouter()
const route = useRoute()

// 新闻列表数据
const newsList = ref([])
const loading = ref(false)
const error = ref(null)
const total = ref(0)

// 当前新闻类型，用于传递给广告组件
const currentNewsType = ref(getNewsType() || null)

const pageSize = 5
const currentPage = ref(1)

// 获取新闻列表
const fetchNewsList = async () => {
  loading.value = true
  error.value = null
  
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize
    }
    
    const res = await newsApi.list(params)
    if(!res || !res.data || res.code !== 200) {
      error.value = res.message || '获取新闻列表失败，请稍后重试'
      newsList.value = []
      total.value = 0
      return
    }
    newsList.value = res.data.list || []
    total.value = res.total
  } catch (err) {
    error.value = err.message || '获取新闻列表失败，请稍后重试'
    newsList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 从路由查询参数获取页码
onMounted(() => {
  const page = parseInt(route.query.page) || 1
  currentPage.value = page
  fetchNewsList()
})

// 监听页码变化，重新获取数据
watch(currentPage, (newPage, oldPage) => {
  // 避免初始化时重复请求
  if (oldPage !== undefined && newPage !== oldPage) {
    fetchNewsList()
  }
})

// 分页后的新闻列表
// 如果接口支持分页，直接使用 newsList，否则进行前端分页
const paginatedNews = computed(() => {
  // 如果接口已经分页（返回的数据量小于等于 pageSize），直接返回
  // 否则进行前端分页处理
  if (newsList.value.length <= pageSize) {
    return newsList.value
  }
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return newsList.value.slice(start, end)
})

// 总页数
const totalPages = computed(() => {
  if (total.value > 0) {
    // 如果接口返回了总数，使用总数计算
    return Math.ceil(total.value / pageSize)
  }
  // 否则使用列表长度计算
  return Math.ceil(newsList.value.length / pageSize)
})

// 可见页码
const visiblePages = computed(() => {
  const pages = []
  const total = totalPages.value
  const current = currentPage.value
  
  if (total <= 7) {
    for (let i = 1; i <= total; i++) {
      pages.push(i)
    }
  } else {
    if (current <= 4) {
      for (let i = 1; i <= 5; i++) pages.push(i)
      pages.push('...')
      pages.push(total)
    } else if (current >= total - 3) {
      pages.push(1)
      pages.push('...')
      for (let i = total - 4; i <= total; i++) pages.push(i)
    } else {
      pages.push(1)
      pages.push('...')
      for (let i = current - 1; i <= current + 1; i++) pages.push(i)
      pages.push('...')
      pages.push(total)
    }
  }
  
  return pages
})

// 切换页码
const changePage = (page) => {
  if (page === '...' || page < 1 || page > totalPages.value) return
  currentPage.value = page
  router.push({ query: { ...route.query, page } })
  window.scrollTo({ top: 0, behavior: 'smooth' })
  // 页码变化会触发 watch，自动调用 fetchNewsList
}

// 获取图片完整URL
const getImageUrl = (imgUrl) => {
  if (!imgUrl) return ''
  // 如果已经是完整URL，直接返回
  if (imgUrl.startsWith('http://') || imgUrl.startsWith('https://')) {
    return imgUrl
  }
  // 添加后端访问路径前缀
  const baseUrl = ''
  // 确保路径以 / 开头
  const path = imgUrl.startsWith('/') ? imgUrl : `/${imgUrl}`
  return `${baseUrl}${path}`
}

// 处理标签，根据 | 分割
const getTags = (tags) => {
  if (!tags) return []
  // 如果是字符串，按 | 分割
  if (typeof tags === 'string') {
    return tags.split('|').filter(tag => tag.trim()).map(tag => tag.trim())
  }
  // 如果是数组，直接返回
  if (Array.isArray(tags)) {
    return tags
  }
  return []
}

// 跳转到详情页
const goToDetail = (id, newsType) => {
  // 存储新闻类型并更新广告组件
  if (newsType) {
    saveNewsType(newsType)
    currentNewsType.value = newsType
  }
  router.push(`/news/${id}`)
}

</script>

<style scoped>
.news-list-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.content-wrapper {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}

.main-content {
  flex: 1;
  min-width: 0;
}

.page-title {
  margin-bottom: 30px;
}

.page-title h1 {
  font-size: 32px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.news-item {
  display: flex;
  gap: 24px;
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
  cursor: pointer;
}

.news-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.news-thumbnail {
  position: relative;
  flex-shrink: 0;
  width: 300px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
}

.news-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.like-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  color: #666;
}

.like-btn:hover {
  background: #fff;
  transform: scale(1.1);
  color: #ff6b6b;
}

.like-btn svg {
  width: 20px;
  height: 20px;
}

.news-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.news-category {
  display: inline-block;
  padding: 4px 12px;
  background: #f0f0f0;
  border-radius: 12px;
  font-size: 12px;
  color: #666;
  width: fit-content;
}

.news-title {
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
  line-height: 1.4;
}

.news-description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: #999;
}

.meta-item {
  display: flex;
  align-items: center;
}

.news-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  padding: 4px 10px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
  transition: all 0.3s;
}

.tag:hover {
  background: #e0e0e0;
}

.news-actions {
  display: flex;
  gap: 12px;
  margin-top: auto;
}

.action-btn {
  padding: 10px 24px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 500;
}

.download-btn {
  background: #4a9eff;
  color: white;
}

.download-btn:hover {
  background: #3a8eef;
  transform: translateY(-1px);
}

.preview-btn {
  background: #f0f0f0;
  color: #666;
}

.preview-btn:hover {
  background: #e0e0e0;
  transform: translateY(-1px);
}

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
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
  color: #666;
}

.page-btn:hover:not(:disabled) {
  border-color: #4a9eff;
  color: #4a9eff;
}

.page-btn:disabled {
  opacity: 0.5;
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
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
  color: #666;
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-number:hover:not(.active) {
  border-color: #4a9eff;
  color: #4a9eff;
}

.page-number.active {
  background: #4a9eff;
  border-color: #4a9eff;
  color: white;
}

.page-number:disabled {
  cursor: default;
  pointer-events: none;
}

.loading-container,
.error-container,
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.loading-text,
.error-text,
.empty-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

.error-text {
  color: #ff6b6b;
}

.retry-btn {
  padding: 10px 24px;
  border: 1px solid #4a9eff;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: #4a9eff;
  transition: all 0.3s;
}

.retry-btn:hover {
  background: #4a9eff;
  color: white;
}

@media (max-width: 1024px) {
  .content-wrapper {
    flex-direction: column;
  }
}

@media (max-width: 768px) {
  .news-list-container {
    padding: 12px;
  }
}
</style>

