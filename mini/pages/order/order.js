// pages/order/order.js
const { formatTime } = require('../../utils/util')

Page({
  data: {
    tabs: [
      { id: 'all', name: '全部' },
      { id: 0, name: '待付款' },
      { id: 2, name: '待收货' },
      { id: 3, name: '已完成' }
    ],
    currentTab: 'all',
    orders: [],
    page: 1,
    size: 10,
    hasMore: true
  },

  onLoad() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showModal({
        title: '提示',
        content: '请先登录后查看订单',
        confirmText: '去登录',
        success: (res) => {
          if (res.confirm) {
            // 跳转到我的页面
            wx.switchTab({
              url: '/pages/user/user'
            })
          } else {
            // 取消则返回首页
            wx.switchTab({
              url: '/pages/index/index'
            })
          }
        }
      })
      return
    }
    
    this.loadOrders()
  },

  onShow() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      // 未登录，不加载订单数据
      this.setData({ orders: [] })
      return
    }
    
    this.loadOrders()
  },

  // 加载订单数据
  loadOrders() {
    // 再次检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      return
    }
    
    const { API } = require('../../utils/api')
    const { currentTab, page, size } = this.data
    
    // 构造查询参数
    const status = currentTab === 'all' ? undefined : currentTab
    
    API.getOrders(page, size, status).then(res => {
      // 如果返回401，说明token过期或无效，已在API层面处理
      if (res.code === 401) {
        return
      }
      
      if (res.code === 200 && res.data) {
        const orders = res.data.records.map(order => ({
          id: order.id,
          orderNo: order.orderNo,
          status: order.status,
          statusText: order.statusText,
          goodsCount: order.goodsCount,
          totalPrice: order.payAmount,
          createTime: this.formatTime(order.createTime),
          items: order.items || []
        }))
        
        this.setData({
          orders: page === 1 ? orders : [...this.data.orders, ...orders],
          hasMore: res.data.records.length >= size
        })
      }
    }).catch(err => {
      console.error('获取订单失败:', err)
      // 网络错误等异常情况才显示提示
      wx.showToast({
        title: '网络错误，请重试',
        icon: 'none'
      })
    })
  },

  // 格式化时间
  formatTime(dateStr) {
    if (!dateStr) return ''
    const date = new Date(dateStr)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hour = String(date.getHours()).padStart(2, '0')
    const minute = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day} ${hour}:${minute}`
  },

  // 切换标签
  onTabChange(e) {
    const { id } = e.currentTarget.dataset
    this.setData({
      currentTab: id,
      page: 1,
      orders: []
    })
    this.loadOrders()
  },

  // 查看订单详情
  viewOrder(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/order-detail/order-detail?id=${id}`
    })
  },

  // 取消订单
  cancelOrder(e) {
    const { id } = e.currentTarget.dataset
    const { API } = require('../../utils/api')
    
    wx.showModal({
      title: '提示',
      content: '确定要取消这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          API.cancelOrder(id).then(res => {
            if (res.code === 200) {
              wx.showToast({
                title: '订单已取消',
                icon: 'success'
              })
              // 刷新订单列表
              this.setData({ page: 1, orders: [] })
              this.loadOrders()
            } else {
              wx.showToast({
                title: res.message || '取消失败',
                icon: 'none'
              })
            }
          }).catch(err => {
            console.error('取消订单失败:', err)
            wx.showToast({
              title: '取消失败',
              icon: 'none'
            })
          })
        }
      }
    })
  },

  // 去支付
  payOrder(e) {
    const { id } = e.currentTarget.dataset
    const { API } = require('../../utils/api')
    
    wx.showModal({
      title: '确认支付',
      content: '确定要支付该订单吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '支付中...' })
          API.payOrder(id).then(res => {
            wx.hideLoading()
            if (res.code === 200) {
              wx.showToast({
                title: '支付成功',
                icon: 'success'
              })
              // 刷新订单列表
              this.setData({ page: 1, orders: [] })
              this.loadOrders()
            } else {
              wx.showToast({
                title: res.message || '支付失败',
                icon: 'none'
              })
            }
          }).catch(err => {
            wx.hideLoading()
            console.error('支付失败:', err)
            wx.showToast({
              title: '支付失败',
              icon: 'none'
            })
          })
        }
      }
    })
  },

  // 再来一单
  reorder(e) {
    const { order } = e.currentTarget.dataset
    // TODO: 将订单商品加入购物车
    wx.switchTab({
      url: '/pages/goods/goods'
    })
  }
})
