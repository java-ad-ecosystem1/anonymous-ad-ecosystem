import axios from 'axios'

export class HttpError extends Error {
  constructor(error) {
    super(error.response?.data?.message || error.message || '请求失败')
    this.name = 'HttpError'
    this.status = error.response?.status
    this.statusText = error.response?.statusText
    this.data = error.response?.data
  }
}

const createHttpClient = (customConfig = {}) => {
  const instance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || '',
    timeout: 10000,
    withCredentials: true,
    headers: {
      'Content-Type': 'application/json',
    },
    ...customConfig,
  })

  instance.interceptors.request.use(
    (config) => {
      // 如果是FormData，删除Content-Type，让浏览器自动设置
      if (config.data instanceof FormData) {
        delete config.headers['Content-Type']
      }
      return config
    },
    (error) => Promise.reject(error)
  )

  instance.interceptors.response.use(
    (response) => response,
    (error) => Promise.reject(new HttpError(error))
  )

  const request = async (url, config = {}) => {
    try {
      const response = await instance.request({
        url,
        ...config,
      })
      return {
        data: response.data,
        status: response.status,
        headers: response.headers,
      }
    } catch (error) {
      throw error
    }
  }

  return {
    request,
    get: (url, params, config) =>
      request(url, {
        method: 'GET',
        params,
        ...config,
      }),
    post: (url, body, config) =>
      request(url, {
        method: 'POST',
        data: body,
        ...config,
      }),
    put: (url, body, config) =>
      request(url, {
        method: 'PUT',
        data: body,
        ...config,
      }),
    patch: (url, body, config) =>
      request(url, {
        method: 'PATCH',
        data: body,
        ...config,
      }),
    delete: (url, body, config) =>
      request(url, {
        method: 'DELETE',
        data: body,
        ...config,
      }),
    setHeader: (key, value) => {
      instance.defaults.headers.common[key] = value
    },
    addRequestInterceptor: (onFulfilled, onRejected) => {
      instance.interceptors.request.use(onFulfilled, onRejected)
    },
    addResponseInterceptor: (onFulfilled, onRejected) => {
      instance.interceptors.response.use(onFulfilled, onRejected)
    },
  }
}

const http = createHttpClient()

export default http

