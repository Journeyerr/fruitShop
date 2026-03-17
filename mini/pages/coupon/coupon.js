// pages/coupon/coupon.js
const { API } = require('../../utils/api')

Page({
  data: {
    currentTab: 0,
    coupons: [],
    usableCount: 0,
    usedCount: 0,
    expiredCount: 0,
    loading: false
  },

  onLoad() {
    this.loadCoupons()
  },

  onShow() {
    // 每次显示页面时刷新数据
    this.loadCoupons()
  },

  // 切换标签
  switchTab(e) {
    const index = parseInt(e.currentTarget.dataset.index)
    this.setData({ currentTab: index })
    this.loadCoupons()
  },

  // 加载优惠券列表
  loadCoupons() {
    this.setData({ loading: true })

    const status = this.data.currentTab

    API.getMemberCoupons(status).then(res => {
      if (res.code === 200) {
        const coupons = (res.data || []).map(item => ({
          ...item,
          startTime: this.formatDate(item.startTime),
          endTime: this.formatDate(item.endTime)
        }))

        this.setData({
          coupons,
          loading: false
        })

        // 更新各状态数量
        this.loadCouponCounts()
      } else {
        wx.showToast({
          title: res.message || '加载失败',
          icon: 'none'
        })
        this.setData({ loading: false })
      }
    }).catch(err => {
      console.error('加载优惠券失败:', err)
      this.setData({ loading: false })
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    })
  },

  // 加载优惠券数量统计
  loadCouponCounts() {
    // 获取各状态优惠券数量
    API.getMemberCouponCounts().then(res => {
      if (res.code === 200) {
        this.setData({
          usableCount: res.data.usable || 0,
          usedCount: res.data.used || 0,
          expiredCount: res.data.expired || 0
        })
      }
    }).catch(err => {
      console.error('加载优惠券数量失败:', err)
    })
  },

  // 格式化日期
  formatDate(dateStr) {
    if (!dateStr) return ''
    const date = new Date(dateStr)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  },

  // 使用优惠券（跳转到首页）
  useCoupon() {
    wx.switchTab({
      url: '/pages/index/index'
    })
  }
})
