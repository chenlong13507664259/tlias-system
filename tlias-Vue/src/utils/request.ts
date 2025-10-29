import axios from 'axios'

// 创建 axios 实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

// 请求拦截器：自动带 token
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['token'] = token // 根据后端要求的 header 名称
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => response.data, // 成功回调，直接返回 data
  (error) => Promise.reject(error) // 失败回调
)

export default request
