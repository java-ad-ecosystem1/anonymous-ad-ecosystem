import http from '../utils/http'

const wrap = (promise) =>
  promise.then((res) => res.data).catch((error) => {
    throw error
  })


export const newsApi = {
  list(params) {
    return wrap(http.get('/api/news/list', params))
  },
  detail(params) {
    return wrap(http.get('/api/news/detail', params))
  },
}

export const adsApi = {
  queryAds(params) {
    return wrap(http.get('/api/advertisement/queryLastAds', params))
  }
}

export default {
  newsApi,
  adsApi,
}

