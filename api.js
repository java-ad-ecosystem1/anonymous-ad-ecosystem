import http from '../utils/http'

const wrap = (promise) =>
  promise.then((res) => res.data).catch((error) => {
    throw error
  })

export const userApi = {
  login(payload) {
    return wrap(http.post('/api/user/login', payload))
  }
}


export const adsApi = {
  list(params) {
    return wrap(http.post('/api/advertisement/list', params))
  },
  save(params) {
    return wrap(http.post('/api/advertisement/save', params))
  },
  putAds(params) {
    return wrap(http.post('/api/advertisement/putAds/' + encodeURIComponent(params.id)))
  },
  
}

export const uploadApi = {
  upload(file) {
    const formData = new FormData()
    formData.append('file', file)
    return wrap(http.post('/api/upload/file', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }))
  },
}

export default {
  userApi,
  adsApi,
}

