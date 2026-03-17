// utils/api.js - API接口配置

// API基础地址
const BASE_URL = 'http://localhost:8080'

// 图片服务器基础地址（如果图片和API不在同一服务器，可以单独配置）
const IMAGE_BASE_URL = 'http://localhost:8080'

/**
 * 封装请求方法
 */
const request = (url, method = 'GET', data = {}) => {
  return new Promise((resolve, reject) => {
    // 检查是否需要登录（排除登录接口本身）
    const noAuthRequired = url.includes('/wechat/login') || 
                          url.includes('/banner') || 
                          url.includes('/goods/category') ||
                          url.includes('/goods/list') ||
                          url.includes('/goods/page') ||
                          url.includes('/goods/recommend')
    
    const token = wx.getStorageSync('token')
    
    // 如果需要登录但没有token，直接返回
    if (!noAuthRequired && !token) {
      resolve({
        code: 401,
        message: '请先登录'
      })
      return
    }
    
    wx.request({
      url: BASE_URL + url,
      method,
      data,
      header: {
        'content-type': 'application/json',
        'Authorization': token || ''
      },
      success: (res) => {
        // 处理401未授权错误
        if (res.statusCode === 401) {
          // 清除本地token
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          
          // 提示用户重新登录
          wx.showModal({
            title: '提示',
            content: '登录已过期，请重新登录',
            showCancel: false,
            confirmText: '去登录',
            success: () => {
              const app = getApp()
              app.globalData.needLogin = true
              wx.switchTab({
                url: '/pages/user/user'
              })
            }
          })
          
          // 返回特定格式
          resolve({
            code: 401,
            message: '登录已过期'
          })
          return
        }
        
        if (res.statusCode === 200) {
          resolve(res.data)
        } else {
          reject(res)
        }
      },
      fail: reject
    })
  })
}

/**
 * API接口定义
 */
const API = {
  // 首页相关
  getBanner: () => request('/api/web/banner/list'),
  getRecommend: () => request('/api/web/goods/recommend'),
  
  // 商品相关
  getCategories: () => request('/api/web/goods/category'),
  getGoodsByCategory: (categoryId) => request(`/api/web/goods/list/${categoryId}`),
  getGoodsList: (categoryId, keyword, page = 1, size = 20) => 
    request('/api/web/goods/page', 'GET', { categoryId, keyword, page, size }),
  getGoodsDetail: (id) => request(`/api/web/goods/${id}`),
  
  // 购物车相关
  addToCart: (data) => request('/api/cart', 'POST', data),
  getCart: () => request('/api/cart'),
  updateCart: (data) => request('/api/cart', 'PUT', data),
  clearCart: () => request('/api/cart', 'DELETE'),
  
  // 订单相关
  createOrder: (data) => request('/api/web/order', 'POST', data),
  getOrders: (page = 1, size = 10, status) => {
    const params = { page, size }
    if (status !== undefined && status !== null) {
      params.status = status
    }
    return request('/api/web/order/page', 'GET', params)
  },
  getOrderDetail: (id) => request(`/api/web/order/${id}`),
  cancelOrder: (id) => request(`/api/web/order/cancel/${id}`, 'PUT'),
  payOrder: (id) => request(`/api/web/order/pay/${id}`, 'PUT'),
  completeOrder: (id) => request(`/api/web/order/complete/${id}`, 'PUT'),
  
  // 用户相关
  wechatLogin: (data) => request('/api/web/wechat/login', 'POST', data),
  getUserInfo: () => request('/api/web/member/info'),
  updateUserInfo: (data) => request('/api/web/member/info', 'PUT', data),
  logout: () => request('/api/web/wechat/logout', 'POST'),
  
  // 地址相关
  getAddresses: () => request('/api/web/address/list'),
  getAddressById: (id) => request(`/api/web/address/${id}`),
  addAddress: (data) => request('/api/web/address', 'POST', data),
  updateAddress: (data) => request('/api/web/address', 'PUT', data),
  deleteAddress: (id) => request(`/api/web/address/${id}`, 'DELETE'),
  setDefaultAddress: (id) => request(`/api/web/address/default/${id}`, 'PUT'),
  
  // 优惠券相关
  getMemberCoupons: (status) => {
    const params = {}
    if (status !== undefined) {
      params.status = status
    }
    return request('/api/web/coupon/my', 'GET', params)
  },
  getMemberCouponCounts: () => request('/api/web/coupon/my/counts'),
  
  // 省市区相关
  getProvinces: () => request('/api/web/region/provinces'),
  getCities: (provinceCode) => request(`/api/web/region/cities/${provinceCode}`),
  getDistricts: (cityCode) => request(`/api/web/region/districts/${cityCode}`)
}

module.exports = {
  BASE_URL,
  IMAGE_BASE_URL,
  API
}
