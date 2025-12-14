<template>
  <div class="video-detail">
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
      <button class="retry-btn" @click="fetchVideoDetail">重新加载</button>
    </div>

    <!-- 视频详情 -->
    <div v-else>
      <button class="back-btn" @click="$router.back()">
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
          <path d="M12 4L6 10L12 16" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        返回列表
      </button>

      <div class="video-player">
        <video 
          ref="videoPlayer"
          :src="getVideoUrl(video.videoUrl)"
          preload="metadata"
          controls
          @play="handleVideoPlay"
          @mouseenter="handleVideoHover($event, true)"
          @mouseleave="handleVideoHover($event, false)"
        ></video>
        <div class="play-button" @click="playVideo" v-show="!isVideoPlaying">
          <svg width="80" height="80" viewBox="0 0 80 80" fill="none">
            <circle cx="40" cy="40" r="38" fill="rgba(255, 255, 255, 0.95)" />
            <path d="M32 24L56 40L32 56V24Z" fill="#667eea" />
          </svg>
        </div>
      </div>

      <div class="video-main-info">
        <h1 class="video-title">{{ video.videoName }}</h1>
        <div class="video-stats">
          <div class="stats-left">
            <span class="date">{{ formatDate(video.createTime, 'yyyy-MM-dd HH:mm:ss') }}</span>
          </div>
        </div>
      </div>

      <div class="video-description">
        <h3>视频简介</h3>
        <p>{{ video.videoDescribe || '暂无简介' }}</p>
      </div>

      <div class="comments-section">
        <h3>{{ comments }} 条评论</h3>
        
        <div class="comment-input">
          <img src="@/assets/image.png" class="user-avatar">
          <input type="text" placeholder="添加评论..." v-model="newComment">
          <button @click="addComment" :disabled="!newComment.trim()">发表</button>
        </div>
      </div>
    </div>

    <!-- 广告弹框 -->
    <div v-if="showAdModal" class="ad-modal-overlay" @click="closeAdModal">
      <div class="ad-modal" @click.stop>
        <button class="ad-close-btn" @click="closeAdModal">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
        
        <!-- 加载广告 -->
        <div v-if="adLoading" class="ad-loading">
          <div class="loading"></div>
          <p>加载广告中...</p>
        </div>
        
        <!-- 广告内容 -->
        <div v-else-if="adData" class="ad-content">
          <div v-if="adData.advertisementType === 'picture'" class="ad-media">
            <img :src="getMediaUrl(adData.content)" />
          </div>
          
          <!-- 视频广告 -->
          <div v-else-if="adData.advertisementType === 'video'" class="ad-media ad-video-container">
            <video
              ref="adVideoPlayer"
              :src="getMediaUrl(adData.content)"
              controls
              preload="auto"
              class="ad-video"
              muted
              autoplay
              playsinline
              :loop="adData.loop === true"
              @click.stop
              @loadedmetadata="handleAdVideoLoaded"
            >
              您的浏览器不支持视频播放
            </video>
          </div>
          
          <!-- 文本广告 -->
          <div v-else-if="adData.advertisementType === 'text'" class="ad-text-content">
            <h3 class="ad-title-text" v-if="adData.title">{{ adData.title }}</h3>
            <p class="ad-text">{{ adData.content }}</p>
          </div>
          <div class="ad-info">
            <div class="ad-actions">
              <button class="ad-skip" @click="closeAdModal">
                跳过广告 <span v-if="adCountdown > 0">({{ adCountdown }}s)</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 无广告 -->
        <div v-else class="ad-content">
          <div class="ad-info no-ad">
            <p>暂无广告</p>
            <button class="ad-skip" @click="closeAdModal">
              继续播放 <span v-if="adCountdown > 0">({{ adCountdown }}s)</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { videoApi, adsApi } from '../api/api'
import { formatDate } from '@/utils/date'
import { saveVideoType, getVideoType } from '@/utils/storage'

const route = useRoute()
const router = useRouter()

const video = ref({})
const loading = ref(false)
const error = ref('')
const comments = ref(0)
const newComment = ref('')
const showAdModal = ref(false)
const adCountdown = ref(10)
const videoPlayer = ref(null)
const adVideoPlayer = ref(null)
const isVideoPlaying = ref(false)
const hasShownAd = ref(false)
const currentVideoType = ref(getVideoType() || null)
const adData = ref(null)
const adLoading = ref(false)
let adTimer = null

// 获取视频详情
const fetchVideoDetail = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const videoId = route.params.id
    const params = { id: videoId }
    
    const res = await videoApi.detail(params)
    
    if (!res || !res.data || res.code !== 200) {
      error.value = res.message || '获取视频详情失败，请稍后重试'
      video.value = {}
      return
    }
    
    video.value = res.data || {}

    if (video.value && video.value.videoType) {
      saveVideoType(video.value.videoType)
      currentVideoType.value = video.value.videoType
    }
  } catch (err) {
    console.error('获取视频详情失败:', err)
    error.value = err.message || '获取视频详情失败，请稍后重试'
    video.value = {}
  } finally {
    loading.value = false
  }
}

// 获取广告数据
const fetchAdData = async () => {
  adLoading.value = true
  try {
    const params = {
      putType: currentVideoType.value
    }
    const res = await adsApi.queryAds(params)
    
    if (res && res.data && res.code === 200) {
      adData.value = res.data || null
    } else {
      adData.value = null
    }
  } catch (err) {
    console.error('获取广告失败:', err)
    adData.value = null
  } finally {
    adLoading.value = false
  }
}

// 处理视频播放事件
const handleVideoPlay = async () => {
  // 如果还没有显示过广告,则显示广告并暂停视频
  if (!hasShownAd.value) {
    const videoElement = videoPlayer.value
    if (videoElement) {
      // 暂停视频
      videoElement.pause()
      
      // 显示广告弹框
      showAdModal.value = true
      
      // 获取广告数据
      await fetchAdData()
      
      // 开始倒计时
      adCountdown.value = 10
      adTimer = setInterval(() => {
        adCountdown.value--
        if (adCountdown.value <= 0) {
          clearInterval(adTimer)
        }
      }, 1000)
      
      // 自动关闭广告并继续播放视频
      setTimeout(() => {
        closeAdModal()
      }, 10000)
      
      // 标记已显示过广告
      hasShownAd.value = true
    }
  } else {
    // 已经显示过广告,正常播放
    isVideoPlaying.value = true
  }
}

// 播放视频
const playVideo = () => {
  const videoElement = videoPlayer.value
  if (videoElement) {
    videoElement.play()
  }
}

// 关闭广告弹框
const closeAdModal = () => {
  showAdModal.value = false
  if (adTimer) {
    clearInterval(adTimer)
    adTimer = null
  }
  adCountdown.value = 10
  
  // 关闭广告后继续播放视频
  const videoElement = videoPlayer.value
  if (videoElement) {
    videoElement.play()
    isVideoPlaying.value = true
  }
}

// 处理视频悬停效果
const handleVideoHover = (event, isHover) => {
  // 详情页暂不自动播放，用户点击播放按钮或controls控制
}

// 处理广告视频加载完成
const handleAdVideoLoaded = () => {
  // 确保视频自动播放
  if (adVideoPlayer.value) {
    const playPromise = adVideoPlayer.value.play()
    if (playPromise !== undefined) {
      playPromise.then(() => {
        console.log('广告视频自动播放成功')
      }).catch((error) => {
        console.log('广告视频自动播放失败，可能需要用户交互:', error)
        // 如果自动播放失败，可以显示一个播放按钮提示
      })
    }
  }
}

// 添加评论
const addComment = () => {
  if (newComment.value.trim()) {
    // TODO: 调用评论API
    console.log('添加评论:', newComment.value)
    newComment.value = ''
    comments.value++
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

// 获取广告媒体URL (图片或视频)
const getMediaUrl = (mediaUrl) => {
  if (!mediaUrl) return ''
  if (mediaUrl.startsWith('http://') || mediaUrl.startsWith('https://')) {
    return mediaUrl
  }
   const baseUrl = 'http://10.100.164.8'
  const path = mediaUrl.startsWith('/') ? mediaUrl : `/${mediaUrl}`
  return `${baseUrl}${path}`
}

onMounted(() => {
  window.scrollTo(0, 0)
  fetchVideoDetail()
})
</script>

<style scoped>
.video-detail {
  width: 100%;
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

.loading {
  display: inline-block;
  width: 50px;
  height: 50px;
  border: 4px solid rgba(102, 126, 234, 0.2);
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
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

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  margin-bottom: 20px;
  border: none;
  border-radius: 8px;
  background: white;
  color: #5f6368;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.back-btn:hover {
  background: #f5f5f5;
  transform: translateX(-4px);
}

.video-player {
  position: relative;
  width: 100%;
  padding-top: 56.25%;
  background: #000;
  border-radius: 16px;
  overflow: hidden;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.video-player video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.play-button {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  cursor: pointer;
  transition: transform 0.3s ease;
}

.play-button:hover {
  transform: translate(-50%, -50%) scale(1.1);
}

.video-main-info {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.video-title {
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 16px 0;
  line-height: 1.4;
}

.video-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.stats-left {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #7f8c8d;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: 2px solid #e0e0e0;
  border-radius: 25px;
  background: white;
  color: #5f6368;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn:hover {
  background: #f5f5f5;
  border-color: #d0d0d0;
  transform: translateY(-2px);
}

.action-btn.active {
  border-color: #667eea;
  color: #667eea;
}

.channel-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 16px;
  padding: 20px 24px;
  margin-bottom: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.channel-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.channel-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}

.channel-name {
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 4px 0;
}

.channel-subscribers {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0;
}

.subscribe-btn {
  padding: 12px 32px;
  border: none;
  border-radius: 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
}

.subscribe-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.subscribe-btn.subscribed {
  background: #e0e0e0;
  color: #5f6368;
}

.video-description {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.video-description h3 {
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 12px 0;
}

.video-description p {
  font-size: 14px;
  color: #5f6368;
  line-height: 1.8;
  margin: 0;
}

.comments-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.comments-section h3 {
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 24px 0;
}

.comment-input {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.user-avatar,
.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.comment-input input {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 25px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s ease;
}

.comment-input input:focus {
  border-color: #667eea;
}

.comment-input button {
  padding: 10px 24px;
  border: none;
  border-radius: 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.comment-input button:disabled {
  background: #e0e0e0;
  color: #999;
  cursor: not-allowed;
}

.comment-input button:not(:disabled):hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment {
  display: flex;
  gap: 12px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.comment-author {
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
}

.comment-time {
  font-size: 13px;
  color: #95a5a6;
}

.comment-text {
  font-size: 14px;
  color: #5f6368;
  line-height: 1.6;
  margin: 0 0 8px 0;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.comment-action {
  display: flex;
  align-items: center;
  gap: 6px;
  border: none;
  background: transparent;
  color: #7f8c8d;
  font-size: 13px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.comment-action:hover {
  color: #667eea;
}

.related-videos {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.related-videos h3 {
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 20px 0;
}

.related-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.related-item {
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.related-item:hover {
  transform: translateY(-4px);
}

.related-thumbnail {
  position: relative;
  width: 100%;
  padding-top: 56.25%;
  border-radius: 12px;
  overflow: hidden;
  background: #f0f0f0;
  margin-bottom: 8px;
}

.related-thumbnail img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.related-item:hover .related-thumbnail img {
  transform: scale(1.1);
}

.related-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 3px 6px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
}

.related-info h4 {
  font-size: 14px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 6px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.related-author {
  font-size: 13px;
  color: #7f8c8d;
  margin: 0 0 4px 0;
}

.related-meta {
  font-size: 12px;
  color: #95a5a6;
  margin: 0;
}

/* 广告弹框样式 */
.ad-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.ad-modal {
  position: relative;
  max-width: 800px;
  width: 90%;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: scaleIn 0.3s ease-out;
}

@keyframes scaleIn {
  from {
    transform: scale(0.9);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

.ad-close-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  z-index: 10;
}

.ad-close-btn:hover {
  background: rgba(0, 0, 0, 0.8);
  transform: rotate(90deg) scale(1.1);
}

.ad-content {
  display: flex;
  flex-direction: column;
}

.ad-loading {
  padding: 60px 30px;
  text-align: center;
}

.ad-loading .loading {
  display: inline-block;
  width: 50px;
  height: 50px;
  border: 4px solid rgba(102, 126, 234, 0.2);
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

.ad-loading p {
  margin-top: 20px;
  font-size: 16px;
  color: #7f8c8d;
}

/* 广告媒体容器 */
.ad-media {
  width: 100%;
  min-height: 300px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

/* 图片广告样式 */
.ad-media img {
  width: 100%;
  height: auto;
  max-height: 500px;
  object-fit: contain;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.ad-media img:hover {
  transform: scale(1.02);
}

/* 视频广告容器 */
.ad-video-container {
  background: #000;
  min-height: auto;
}

/* 视频广告样式 */
.ad-video {
  width: 100%;
  height: auto;
  max-height: 500px;
  display: block;
  outline: none;
}

/* 文本广告样式 */
.ad-text-content {
  padding: 40px 30px;
  text-align: center;
  min-height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.ad-title-text {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 20px 0;
  line-height: 1.3;
}

.ad-text {
  font-size: 16px;
  color: #5f6368;
  line-height: 1.8;
  margin: 0;
  max-width: 600px;
  white-space: pre-wrap;
}

.ad-info {
  padding: 30px;
  text-align: center;
}

.ad-info.no-ad {
  padding: 60px 30px;
}

.ad-info.no-ad p {
  font-size: 18px;
  color: #7f8c8d;
  margin-bottom: 25px;
}

.ad-info h3 {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 15px 0;
}

.ad-info p {
  font-size: 16px;
  color: #5f6368;
  line-height: 1.6;
  margin: 0 0 25px 0;
}

.ad-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.ad-learn-more {
  padding: 14px 40px;
  border: none;
  border-radius: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.ad-learn-more:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.ad-skip {
  padding: 14px 40px;
  border: 2px solid #e0e0e0;
  border-radius: 30px;
  background: white;
  color: #5f6368;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.ad-skip:hover {
  border-color: #667eea;
  color: #667eea;
  transform: translateY(-3px);
}

.ad-skip span {
  color: #ff6b6b;
  font-weight: 700;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ad-modal {
    width: 95%;
    max-width: none;
  }
  
  .ad-media {
    min-height: 200px;
  }
  
  .ad-media img {
    max-height: 300px;
  }
  
  .ad-video {
    max-height: 300px;
  }
  
  .ad-text-content {
    padding: 30px 20px;
    min-height: 200px;
  }
  
  .ad-title-text {
    font-size: 22px;
  }
  
  .ad-text {
    font-size: 14px;
  }
  
  .ad-info {
    padding: 20px;
  }
  
  .ad-info h3 {
    font-size: 22px;
  }
  
  .ad-info p {
    font-size: 14px;
  }
  
  .ad-actions {
    flex-direction: column;
  }
  
  .ad-learn-more,
  .ad-skip {
    width: 100%;
    padding: 12px 20px;
    font-size: 14px;
  }
}
</style>

