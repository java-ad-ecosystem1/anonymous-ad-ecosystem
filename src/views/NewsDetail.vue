<template>
  <div class="news-detail-container">
    <button class="back-btn" @click="goBack">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M19 12H5M12 19l-7-7 7-7"/>
      </svg>
      返回列表
    </button>
    
    <div class="content-wrapper">
      <!-- 左侧内容区域 -->
      <div class="main-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading">
          <p>加载中...</p>
        </div>
        
        <!-- 错误状态 -->
        <div v-else-if="error" class="error-container">
          <div class="error-text">{{ error }}</div>
          <button class="retry-btn" @click="fetchNewsDetail">重试</button>
        </div>
        
        <!-- 新闻详情 -->
        <div v-else-if="newsItem" class="news-detail">
          <div class="detail-header">
            <div class="detail-category">{{ newsItem.source }}</div>
            <h1 class="detail-title">{{ newsItem.title }}</h1>
            <div class="detail-meta">
              <span>创建时间: {{ formatDate(newsItem.createTime, 'yyyy-MM-dd HH:mm:ss') }}</span>
            </div>
          </div>
          
          <div class="detail-tags" v-if="getTags(newsItem.label).length > 0">
            <span 
              v-for="tag in getTags(newsItem.label)" 
              :key="tag" 
              class="tag"
            >
              {{ tag }}
            </span>
          </div>
          
          <div class="detail-content">
            <div class="content-section" v-if="newsItem.content">
              <h2>详细内容</h2>
              <div class="content-html" v-html="newsItem.content"></div>
            </div>
          </div>
        </div>
        
        <div v-else class="loading">
          <p>暂无数据</p>
        </div>
      </div>
      
      <!-- 右侧广告位 -->
      <AdSidebar :news-type="currentNewsType" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { newsApi } from '../api/api'
import { formatDate } from '@/utils/date'
import { saveNewsType, getNewsType } from '@/utils/storage'
import AdSidebar from '@/components/AdSidebar.vue'

const router = useRouter()
const route = useRoute()

const newsItem = ref(null)
const loading = ref(false)
const error = ref(null)

// 当前新闻类型，用于传递给广告组件
const currentNewsType = ref(getNewsType() || null)

// 获取新闻详情
const fetchNewsDetail = async () => {
  loading.value = true
  error.value = null
  
  try {
    const id = parseInt(route.params.id)
    if (!id) {
      error.value = '无效的新闻ID'
      return
    }
    
    const params = { id }
    const res = await newsApi.detail(params)
    
    if (!res || !res.data || res.code !== 200) {
      error.value = res.message || '获取新闻详情失败，请稍后重试'
      newsItem.value = null
      return
    }
    
    newsItem.value = res.data
    
    // 存储新闻类型并更新广告组件
    if (newsItem.value && newsItem.value.newsType) {
      saveNewsType(newsItem.value.newsType)
      currentNewsType.value = newsItem.value.newsType
    }
  } catch (err) {
    error.value = err.message || '获取新闻详情失败，请稍后重试'
    newsItem.value = null
  } finally {
    loading.value = false
  }
}

// 获取图片完整URL
const getImageUrl = (imgUrl) => {
  if (!imgUrl) return ''
  // 如果已经是完整URL，直接返回
  if (imgUrl.startsWith('http://') || imgUrl.startsWith('https://')) {
    return imgUrl
  }
  // 添加后端访问路径前缀
  const baseUrl = 'http://127.0.0.1:8082'
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

onMounted(() => {
  fetchNewsDetail()
})

const goBack = () => {
  router.push('/')
}

</script>

<style scoped>
.news-detail-container {
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

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: #f5f5f5;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  margin-bottom: 24px;
  transition: all 0.3s;
}

.back-btn:hover {
  background: #e0e0e0;
  color: #4a9eff;
}

.news-detail {
  background: #ffffff;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.detail-header {
  margin-bottom: 30px;
}

.detail-category {
  display: inline-block;
  padding: 6px 16px;
  background: #f0f0f0;
  border-radius: 16px;
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
}

.detail-title {
  font-size: 32px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 16px 0;
  line-height: 1.4;
}

.detail-meta {
  display: flex;
  gap: 24px;
  font-size: 14px;
  color: #999;
}

.detail-thumbnail {
  width: 100%;
  height: 400px;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 24px;
}

.detail-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 40px;
}

.tag {
  padding: 6px 14px;
  background: #f5f5f5;
  border-radius: 6px;
  font-size: 14px;
  color: #666;
  transition: all 0.3s;
}

.tag:hover {
  background: #e0e0e0;
}

.detail-content {
  margin-bottom: 40px;
}

.content-section {
  margin-bottom: 40px;
}

.content-section h2 {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 16px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.content-section p {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
  margin: 0 0 16px 0;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.feature-list li {
  padding: 12px 0;
  padding-left: 28px;
  font-size: 16px;
  color: #666;
  line-height: 1.6;
  position: relative;
}

.feature-list li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: #4a9eff;
  font-weight: bold;
  font-size: 18px;
}

.specs-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.spec-item {
  display: flex;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
  gap: 12px;
}

.spec-label {
  font-weight: 600;
  color: #333;
}

.spec-value {
  color: #666;
}

.preview-images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.preview-image {
  border-radius: 8px;
  overflow: hidden;
  aspect-ratio: 4/3;
}

.preview-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.preview-image:hover img {
  transform: scale(1.05);
}

.detail-actions {
  display: flex;
  gap: 16px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
}

.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 14px 24px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.download-btn {
  background: #4a9eff;
  color: white;
}

.download-btn:hover {
  background: #3a8eef;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 158, 255, 0.3);
}

.preview-btn {
  background: #f0f0f0;
  color: #666;
}

.preview-btn:hover {
  background: #e0e0e0;
  transform: translateY(-2px);
}

.like-btn {
  background: #f0f0f0;
  color: #666;
}

.like-btn:hover {
  background: #ffe0e0;
  color: #ff6b6b;
}

.like-btn.liked {
  background: #ff6b6b;
  color: white;
}

.like-btn.liked:hover {
  background: #ff5252;
}

.loading {
  text-align: center;
  padding: 60px 20px;
  font-size: 18px;
  color: #999;
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.error-text {
  font-size: 16px;
  color: #ff6b6b;
  margin-bottom: 20px;
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

.content-html {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
}

.content-html :deep(p) {
  margin: 0 0 16px 0;
}

.content-html :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 16px 0;
}

@media (max-width: 1024px) {
  .content-wrapper {
    flex-direction: column;
  }
}

@media (max-width: 768px) {
  .news-detail-container {
    padding: 12px;
  }
  
  .news-detail {
    padding: 24px;
  }
  
  .detail-title {
    font-size: 24px;
  }
  
  .preview-images {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .specs-grid {
    grid-template-columns: 1fr;
  }
  
  .detail-actions {
    flex-direction: column;
  }
}
</style>

