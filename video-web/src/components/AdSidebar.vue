<template>
  <div class="ad-sidebar">
    <div class="ad-container">
      <div class="ad-title">推荐广告</div>
      <div v-if="adsLoading" class="ad-loading">加载中...</div>
      <div v-else-if="adsError" class="ad-error">{{ adsError }}</div>
      <div v-else-if="!ads" class="ad-empty">暂无广告</div>
      <div v-else class="ad-item">
        <!-- 图片广告 -->
        <div v-if="ads.advertisementType === 'picture'" class="ad-media">
          <img :src="getMediaUrl(ads.content)" />
        </div>
        
        <!-- 视频广告 -->
        <div v-else-if="ads.advertisementType === 'video'" class="ad-media ad-video-container">
          <video
            :src="getMediaUrl(ads.content)"
            controls
            preload="metadata"
            class="ad-video"
            :muted="ads.muted !== false"
            :loop="ads.loop === true"
            :autoplay="ads.autoplay === true"
            @click.stop
            @play="onVideoPlay"
            @pause="onVideoPause"
          >
            您的浏览器不支持视频播放
          </video>
        </div>
        
        <!-- 文本广告 -->
        <div v-else-if="ads.advertisementType === 'text'" class="ad-text-content">
          <h3 class="ad-title-text" v-if="ads.title">{{ ads.title }}</h3>
          <p class="ad-text">{{ ads.content }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { adsApi } from '../api/api'
import { getNewsType } from '@/utils/storage'

// 定义 props
const props = defineProps({
  newsType: {
    type: [String, Number],
    default: null
  }
})

// 广告数据
const ads = ref(null)
const adsLoading = ref(false)
const adsError = ref(null)

// 获取媒体文件完整URL（图片或视频）
const getMediaUrl = (mediaUrl) => {
  if (!mediaUrl) return ''
  // 如果已经是完整URL，直接返回
  if (mediaUrl.startsWith('http://') || mediaUrl.startsWith('https://')) {
    return mediaUrl
  }
  // 添加广告提供商的后端访问路径前缀
  const baseUrl = 'http://10.100.164.8'
  // 确保路径以 / 开头
  const path = mediaUrl.startsWith('/') ? mediaUrl : `/${mediaUrl}`
  return `${baseUrl}${path}`
}

// 视频播放事件处理
const onVideoPlay = (event) => {
  // 可以在这里添加播放统计等逻辑
  console.log('视频开始播放')
}

// 视频暂停事件处理
const onVideoPause = (event) => {
  // 可以在这里添加暂停统计等逻辑
  console.log('视频暂停')
}

// 获取广告
const fetchAds = async (newsType) => {
  if (!newsType) {
    // 如果没有传入 newsType，尝试从 localStorage 获取
    newsType = getNewsType()
    if (!newsType) {
      return
    }
  }
  
  adsLoading.value = true
  adsError.value = null
  
  try {
    const params = { putType: newsType }
    const res = await adsApi.queryAds(params)
    
    if (res && res.code === 200 && res.data) {
      ads.value = res.data
    } else {
      ads.value = null
    }
  } catch (err) {
    adsError.value = '获取广告失败'
    ads.value = null
  } finally {
    adsLoading.value = false
  }
}

// 监听 newsType 变化
watch(() => props.newsType, (newNewsType) => {
  if (newNewsType) {
    fetchAds(newNewsType)
  }
}, { immediate: true })

// 组件挂载时加载广告
onMounted(() => {
  if (props.newsType) {
    fetchAds(props.newsType)
  } else {
    // 如果没有传入 newsType，尝试从 localStorage 获取
    const savedNewsType = getNewsType()
    if (savedNewsType) {
      fetchAds(savedNewsType)
    }
  }
})
</script>

<style scoped>
.ad-sidebar {
  width: 300px;
  flex-shrink: 0;
}

.ad-container {
  position: sticky;
  top: 20px;
  background: #ffffff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.ad-title {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.ad-loading,
.ad-error,
.ad-empty {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  font-size: 14px;
}

.ad-error {
  color: #ff6b6b;
}

.ad-item {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  border: 1px solid #f0f0f0;
}

.ad-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.ad-media {
  width: 100%;
  height: 150px;
  overflow: hidden;
  background: #f5f5f5;
  position: relative;
}

.ad-media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.ad-video-container {
  height: auto;
  min-height: 150px;
  max-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ad-video {
  width: 100%;
  height: auto;
  max-height: 300px;
  min-height: 150px;
  object-fit: contain;
  background: #000;
  cursor: pointer;
  border-radius: 4px;
}

.ad-video::-webkit-media-controls-panel {
  background-color: rgba(0, 0, 0, 0.5);
}

.ad-video::-webkit-media-controls-play-button {
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
}

.ad-text-content {
  padding: 12px;
}

.ad-content {
  padding: 12px;
}

.ad-title-text {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 8px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.ad-content {
  font-size: 12px;
  color: #666;
  margin: 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

@media (max-width: 1024px) {
  .ad-sidebar {
    width: 100%;
    order: -1;
  }
  
  .ad-container {
    position: static;
  }
}

@media (max-width: 768px) {
  .ad-sidebar {
    width: 100%;
  }
}
</style>

