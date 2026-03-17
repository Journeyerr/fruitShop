import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 15000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('admin_token')
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 判断是否成功
    if (res.code === 200) {
      return res
    }
    
    // 登录接口的401直接返回错误信息，不跳转
    const isLoginRequest = response.config.url.includes('/admin/login')
    if (res.code === 401) {
      // 如果是登录接口，直接显示错误信息
      if (isLoginRequest) {
        ElMessage.error(res.message || '登录失败')
        return Promise.reject(new Error(res.message || '登录失败'))
      }
      
      // 其他接口的401，表示token过期
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_userInfo')
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
      return Promise.reject(new Error('登录已过期'))
    }
    
    // 其他错误
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
