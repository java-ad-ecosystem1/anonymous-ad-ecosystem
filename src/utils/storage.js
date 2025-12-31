// localStorage 工具函数

const NEWS_TYPE_KEY = 'newsType'

/**
 * 保存新闻类型到 localStorage
 * @param {string|number} newsType - 新闻类型
 */
export function saveNewsType(newsType) {
  try {
    if (newsType !== null && newsType !== undefined) {
      localStorage.setItem(NEWS_TYPE_KEY, String(newsType))
    }
  } catch (error) {
    console.error('保存 newsType 到 localStorage 失败:', error)
  }
}

/**
 * 从 localStorage 获取新闻类型
 * @returns {string|null} 新闻类型，如果不存在则返回 null
 */
export function getNewsType() {
  try {
    return localStorage.getItem(NEWS_TYPE_KEY)
  } catch (error) {
    console.error('从 localStorage 获取 newsType 失败:', error)
    return null
  }
}

/**
 * 清除新闻类型
 */
export function clearNewsType() {
  try {
    localStorage.removeItem(NEWS_TYPE_KEY)
  } catch (error) {
    console.error('清除 newsType 失败:', error)
  }
}

