import request from '@/utils/request'

// ============ 登录相关 ============
export const loginApi = {
  login: (data) => request.post('/admin/login', data),
  logout: () => request.post('/admin/logout')
}

// ============ 文件上传 ============
export const uploadApi = {
  image: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/admin/upload/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  file: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/admin/upload/file', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}

// ============ 轮播图管理 ============
export const bannerApi = {
  page: (params) => request.get('/admin/banner/page', { params }),
  add: (data) => request.post('/admin/banner', data),
  update: (data) => request.put('/admin/banner', data),
  delete: (id) => request.delete(`/admin/banner/${id}`),
  updateStatus: (id, status) => request.put(`/admin/banner/status/${id}`, { status })
}

// ============ 分类管理 ============
export const categoryApi = {
  list: (params) => request.get('/admin/category/list', { params }),
  add: (data) => request.post('/admin/category', data),
  update: (data) => request.put('/admin/category', data),
  delete: (id) => request.delete(`/admin/category/${id}`),
  updateStatus: (id, status) => request.put(`/admin/category/status/${id}`, { status })
}

// ============ 商品管理 ============
export const goodsApi = {
  page: (params) => request.get('/admin/goods/page', { params }),
  detail: (id) => request.get(`/admin/goods/${id}`),
  add: (data) => request.post('/admin/goods', data),
  update: (data) => request.put('/admin/goods', data),
  delete: (id) => request.delete(`/admin/goods/${id}`),
  updateStatus: (id, status) => request.put(`/admin/goods/status/${id}`, { status })
}

// ============ 订单管理 ============
export const orderApi = {
  page: (params) => request.get('/admin/order/page', { params }),
  detail: (id) => request.get(`/admin/order/${id}`),
  deliver: (id) => request.put(`/admin/order/deliver/${id}`),
  cancel: (id) => request.put(`/admin/order/cancel/${id}`),
  statistics: () => request.get('/admin/order/statistics')
}

// ============ 会员管理 ============
export const memberApi = {
  page: (params) => request.get('/admin/member/page', { params }),
  detail: (id) => request.get(`/admin/member/${id}`),
  update: (data) => request.put('/admin/member', data)
}

// ============ 优惠券管理 ============
export const couponApi = {
  page: (params) => request.get('/admin/coupon/page', { params }),
  add: (data) => request.post('/admin/coupon', data),
  update: (data) => request.put('/admin/coupon', data),
  delete: (id) => request.delete(`/admin/coupon/${id}`),
  updateStatus: (id, status) => request.put(`/admin/coupon/status/${id}`, { status })
}

// ============ 地址管理 ============
export const addressApi = {
  page: (params) => request.get('/admin/address/page', { params }),
  delete: (id) => request.delete(`/admin/address/${id}`)
}

// ============ 配送地址管理 ============
export const deliveryApi = {
  page: (params) => request.get('/admin/delivery/page', { params }),
  add: (data) => request.post('/admin/delivery', data),
  update: (data) => request.put('/admin/delivery', data),
  delete: (id) => request.delete(`/admin/delivery/${id}`)
}

// ============ 系统设置 ============
export const settingApi = {
  get: () => request.get('/admin/setting'),
  update: (data) => request.put('/admin/setting', data)
}

// ============ 数据统计 ============
export const statisticsApi = {
  overview: () => request.get('/admin/statistics/overview'),
  orderTrend: (params) => request.get('/admin/statistics/order-trend', { params }),
  salesRank: (params) => request.get('/admin/statistics/sales-rank', { params })
}
