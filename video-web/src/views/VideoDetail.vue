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
          @pause="handleVideoPause"
          @ended="handleVideoEnded"
          @timeupdate="handleTimeUpdate"
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
        <h3>评论 ({{ comments.length }})</h3>
        
        <!-- 评论输入框 -->
        <div class="comment-input">
          <img :src="defaultAvatar" alt="用户头像" class="user-avatar">
          <div class="input-wrapper">
            <textarea 
              v-model="newComment" 
              placeholder="写下你的评论..."
              rows="3"
              @keydown.ctrl.enter="addComment"
              @keydown.enter.exact.prevent="addComment"
            ></textarea>
            <div class="input-actions">
              <button 
                class="submit-btn" 
                @click="addComment" 
                :disabled="!newComment.trim() || submittingComment"
              >
                {{ submittingComment ? '发布中...' : '发表评论' }}
              </button>
            </div>
          </div>
        </div>
        
        <!-- 评论列表 -->
        <div class="comments-list" v-if="comments.length > 0">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <img :src="comment.userAvatar || defaultAvatar" alt="用户头像" class="comment-avatar">
            <div class="comment-content">
              <div class="comment-header">
                <span class="comment-author">{{ comment.username }}</span>
                <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
              </div>
              <p class="comment-text">{{ comment.content }}</p>
              <div class="comment-actions">
                <button class="action-btn" @click="likeComment(comment.id)">
                  <span>👍 {{ comment.likes || 0 }}</span>
                </button>
                <button class="action-btn" @click="replyToComment(comment)">
                  <span>回复</span>
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 无评论提示 -->
        <div v-else class="no-comments">
          <p>还没有评论，快来发表第一条评论吧！</p>
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
import { videoApi, adsApi } from '@/api/api'
import { formatDate } from '@/utils/date'
import { saveVideoType, getVideoType } from '@/utils/storage'

// 导入默认头像图片
import defaultAvatarImg from '@/assets/image.png'

const route = useRoute()
const router = useRouter()

// 响应式数据
const video = ref({})
const loading = ref(false)
const error = ref('')
const newComment = ref('')
const showAdModal = ref(false)
const adCountdown = ref(10)
const videoPlayer = ref(null)
const adVideoPlayer = ref(null)
const isVideoPlaying = ref(false)
const hasShownAd = ref(false)
const hasShownHalfAd = ref(false)
const isProgrammaticPause = ref(false)
const currentVideoType = ref(getVideoType() || null)
const adData = ref(null)
const adLoading = ref(false)
// 用于管理倒计时的定时器，防止多个定时器叠加导致倒计时加速
const adInterval = ref(null)
const adTimeout = ref(null)
const submittingComment = ref(false)
const comments = ref([])
// 防止 timeupdate 重复触发与 pause 抖动
const lastTime = ref(0)
const lastPauseAt = ref(0)
const pauseCooldownMs = 500

// 默认头像 - 使用导入的图片
const defaultAvatar = defaultAvatarImg

// 生成随机访客用户名
const generateRandomUsername = () => {
  const randomNum = Math.floor(Math.random() * 9999) + 1
  return `访客用户${randomNum}`
}

// 从 localStorage 加载评论
const loadCommentsFromStorage = () => {
  const videoId = route.params.id
  const savedComments = localStorage.getItem(`video_comments_${videoId}`)
  if (savedComments) {
    try {
      comments.value = JSON.parse(savedComments)
    } catch (err) {
      console.error('解析评论数据失败:', err)
      comments.value = []
    }
  } else {
    comments.value = []
  }
}

// 保存评论到 localStorage
const saveCommentsToStorage = () => {
  const videoId = route.params.id
  localStorage.setItem(`video_comments_${videoId}`, JSON.stringify(comments.value))
}

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
      // 程序化暂停：先标记，避免 pause 处理器重复触发
      isProgrammaticPause.value = true
      // 暂停视频
      videoElement.pause()
      
      // 显示广告弹框
      showAdModal.value = true
      
      // 获取广告数据
      await fetchAdData()
      
      // 启动广告倒计时（会清理已有的定时器，避免叠加）
      startAdCountdown()
      
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
  adCountdown.value = 10

  // 清理倒计时定时器，防止遗留计时器继续修改 adCountdown
  if (adInterval.value) {
    clearInterval(adInterval.value)
    adInterval.value = null
  }
  if (adTimeout.value) {
    clearTimeout(adTimeout.value)
    adTimeout.value = null
  }

  // 关闭广告后继续播放视频
  const videoElement = videoPlayer.value
  if (videoElement) {
    // 标记为程序化操作，避免 pause 处理器误判
    isProgrammaticPause.value = true
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
      })
    }
  }
}

// 启动广告倒计时并安排自动关闭（会清理已有定时器）
const startAdCountdown = () => {
  // 重置倒计时显示
  adCountdown.value = 10

  // 清理已有定时器，避免多个定时器同时减小 adCountdown
  if (adInterval.value) {
    clearInterval(adInterval.value)
    adInterval.value = null
  }
  if (adTimeout.value) {
    clearTimeout(adTimeout.value)
    adTimeout.value = null
  }

  adInterval.value = setInterval(() => {
    adCountdown.value--
    if (adCountdown.value <= 0) {
      if (adInterval.value) {
        clearInterval(adInterval.value)
        adInterval.value = null
      }
    }
  }, 1000)

  adTimeout.value = setTimeout(() => {
    // 自动关闭弹窗
    closeAdModal()
    if (adTimeout.value) {
      clearTimeout(adTimeout.value)
      adTimeout.value = null
    }
  }, 10000)
}

// 当播放进度更新时，检查是否到达一半，显示中间广告（仅一次）
const handleTimeUpdate = async () => {
  const videoElement = videoPlayer.value
  if (!videoElement || hasShownHalfAd.value) return
  const duration = videoElement.duration || 0
  const current = videoElement.currentTime || 0

  // debug: 打印当前进度，帮助排查中插广告未触发问题
  console.log('[Ad] timeupdate', { current, duration, hasShownHalfAd: hasShownHalfAd.value, lastTime: lastTime.value })

  // 只在从未触发且跨越 50% 时触发一次，避免 timeupdate 重复进入竞态
  if (duration > 0 && lastTime.value < duration / 2 && current >= duration / 2 && !hasShownHalfAd.value) {
    // 立即标记已触发，防止并发触发
    hasShownHalfAd.value = true

    // 标记为程序化暂停，避免 pause 事件误触
    isProgrammaticPause.value = true
    videoElement.pause()

    // 与开头广告一致：先显示弹框，再获取广告数据
    showAdModal.value = true
    await fetchAdData()

    // 启动广告倒计时（会清理已有的定时器，避免叠加）
    startAdCountdown()
  }

  // 更新上一次进度
  lastTime.value = current
}

// 处理用户或其他原因的暂停事件，若为用户主动暂停则展示广告
const handleVideoPause = async () => {
  // debug: 打印状态，判断为什么暂停时未触发广告
  console.log('[Ad] pause event', {
    isProgrammaticPause: isProgrammaticPause.value,
    showAdModal: showAdModal.value,
    adLoading: adLoading.value,
    hasShownHalfAd: hasShownHalfAd.value,
    hasShownAd: hasShownAd.value,
    currentTime: videoPlayer.value ? videoPlayer.value.currentTime : null,
    duration: videoPlayer.value ? videoPlayer.value.duration : null
  })

  // 如果是我们程序化触发的暂停，不再处理
  if (isProgrammaticPause.value) {
    // 重置标记，允许后续用户暂停触发广告
    isProgrammaticPause.value = false
    return
  }

  // 防抖：短时间内连续 pause 忽略（避免用户或浏览器触发多次事件）
  const now = Date.now()
  if (now - lastPauseAt.value < pauseCooldownMs) {
    console.log('[Ad] pause ignored due to cooldown')
    return
  }
  lastPauseAt.value = now

  // 如果广告已显示或正在加载中，跳过
  if (showAdModal.value || adLoading.value) return

  const videoElement = videoPlayer.value
  if (!videoElement) return

  // 与开头广告一致：先显示弹框，再获取广告数据
  showAdModal.value = true
  await fetchAdData()

  // 启动广告倒计时（会清理已有的定时器，避免叠加）
  startAdCountdown()
}

// 处理视频自然结束（ended），在自然结束时也展示广告（与 pause 行为一致）
const handleVideoEnded = async () => {
  console.log('[Ad] ended event', {
    isProgrammaticPause: isProgrammaticPause.value,
    showAdModal: showAdModal.value,
    adLoading: adLoading.value,
    hasShownHalfAd: hasShownHalfAd.value,
    hasShownAd: hasShownAd.value
  })

  // 确保不是程序化暂停状态并且当前没有广告弹窗或加载中
  isProgrammaticPause.value = false
  if (showAdModal.value || adLoading.value) return

  // 允许结尾展示广告（即使之前展示过开头或中插），以覆盖自然结束场景

  // 与开头广告一致：先显示弹框，再请求广告
  showAdModal.value = true
  await fetchAdData()
  startAdCountdown()

  // 标记为已展示（防止会话内重复）
  hasShownAd.value = true
}

// 添加评论
const addComment = async () => {
  if (!newComment.value.trim() || submittingComment.value) return
  
  submittingComment.value = true
  
  try {
    // 创建新评论对象
    const newCommentObj = {
      id: Date.now(), // 使用时间戳作为ID
      username: generateRandomUsername(), // 随机生成访客用户名
      userAvatar: defaultAvatar, // 使用统一的默认头像
      content: newComment.value.trim(),
      createTime: new Date().toISOString(),
      likes: 0
    }
    
    // 添加到评论列表最前面
    comments.value.unshift(newCommentObj)
    
    // 保存到 localStorage
    saveCommentsToStorage()
    
    // 清空输入框
    newComment.value = ''
    
  } catch (err) {
    console.error('发表评论失败:', err)
    alert('评论发表失败，请稍后重试')
  } finally {
    submittingComment.value = false
  }
}

// 点赞评论
const likeComment = (commentId) => {
  const comment = comments.value.find(c => c.id === commentId)
  if (comment) {
    comment.likes = (comment.likes || 0) + 1
    // 保存更新后的评论
    saveCommentsToStorage()
  }
}

// 回复评论
const replyToComment = (comment) => {
  newComment.value = `回复 @${comment.username}: `
  // 滚动到评论输入框
  document.querySelector('.comment-input textarea')?.focus()
}

// 获取视频URL（带简单清洗，容错数据库中可能带有时长或空格）
const getVideoUrl = (videoUrl) => {
  if (!videoUrl) return ''
  if (videoUrl.startsWith('http://') || videoUrl.startsWith('https://')) {
    return videoUrl
  }

  // 简单清洗：去除末尾可能被拼接的时长文本（如 "_h4: 24"、".r15: 41" 等），并移除多余空白
  let cleaned = videoUrl.trim()
  cleaned = cleaned.replace(/[_.\s-]*[hr]?\s?\d+:\s*\d{2}$/, '')
  cleaned = cleaned.replace(/\s+/g, '')

  // 添加后端访问路径前缀
  const baseUrl = 'http://localhost:8084'
  const path = cleaned.startsWith('/') ? cleaned : `/${cleaned}`
  return `${baseUrl}${encodeURI(path)}`
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

// 初始化
onMounted(() => {
  window.scrollTo(0, 0)
  fetchVideoDetail()
  loadCommentsFromStorage() // 加载已保存的评论
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

/* 评论部分样式 */
.comments-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-top: 24px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

.comments-section h3 {
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 20px 0;
}

.comment-input {
  display: flex;
  gap: 16px;
  margin-bottom: 30px;
}

.user-avatar, .comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.input-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.comment-input textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  font-size: 14px;
  line-height: 1.5;
  resize: vertical;
  outline: none;
  transition: border-color 0.3s;
  font-family: inherit;
  min-height: 80px;
}

.comment-input textarea:focus {
  border-color: #667eea;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
}

.submit-btn {
  padding: 10px 30px;
  border: none;
  border-radius: 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-radius: 12px;
  background: #f8f9fa;
  transition: background 0.3s;
}

.comment-item:hover {
  background: #f0f2f5;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.comment-time {
  font-size: 12px;
  color: #95a5a6;
}

.comment-text {
  color: #5f6368;
  line-height: 1.6;
  margin: 0 0 12px 0;
  font-size: 14px;
  white-space: pre-wrap;
  word-break: break-word;
}

.comment-actions {
  display: flex;
  gap: 20px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  border: none;
  background: transparent;
  color: #7f8c8d;
  font-size: 13px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: all 0.3s;
}

.action-btn:hover {
  background: #e9ecef;
  color: #667eea;
}

.no-comments {
  text-align: center;
  padding: 40px 20px;
  color: #95a5a6;
  background: #f8f9fa;
  border-radius: 12px;
}

.no-comments p {
  margin: 0;
  font-size: 14px;
}

/* 广告弹框样式 - 保持不变 */
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

.ad-media {
  width: 100%;
  min-height: 300px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

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

.ad-video-container {
  background: #000;
  min-height: auto;
}

.ad-video {
  width: 100%;
  height: auto;
  max-height: 500px;
  display: block;
  outline: none;
}

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

.ad-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
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
  
  .ad-actions {
    flex-direction: column;
  }
  
  .ad-skip {
    width: 100%;
    padding: 12px 20px;
    font-size: 14px;
  }
}

</style>
