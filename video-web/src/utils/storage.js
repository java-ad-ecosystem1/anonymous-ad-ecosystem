// localStorage 工具函数

const VIDEO_TYPE_KEY = 'videoType'

/**
 * 保存视频类型到 localStorage
 * @param {string|number} videoType - 视频类型
 */
export function saveVideoType(videoType) {
  try {
    if (videoType !== null && videoType !== undefined) {
      localStorage.setItem(VIDEO_TYPE_KEY, String(videoType))
    }
  } catch (error) {
    console.error('保存 videoType 到 localStorage 失败:', error)
  }
}

/**
 * 从 localStorage 获取视频类型
 * @returns {string|null} 视频类型，如果不存在则返回 null
 */
export function getVideoType() {
  try {
    return localStorage.getItem(VIDEO_TYPE_KEY)
  } catch (error) {
    console.error('从 localStorage 获取 videoType 失败:', error)
    return null
  }
}

/**
 * 清除视频类型
 */
export function clearVideoType() {
  try {
    localStorage.removeItem(VIDEO_TYPE_KEY)
  } catch (error) {
    console.error('清除 videoType 失败:', error)
  }
}

