import http from '../utils/http'

const wrap = (promise) =>
  promise.then((res) => res.data).catch((error) => {
    throw error
  })


export const videoApi = {
  list(params) {
    return wrap(http.get('/api/video/list', params))
  },
  detail(params) {
    return wrap(http.get('/api/video/detail', params))
  },
}

export const adsApi = {
  queryAds(params) {
    return wrap(http.get('/api/advertisement/queryLastAds', params))
  }
}

export default {
  videoApi,
  adsApi,
}

