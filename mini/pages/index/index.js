// pages/index/index.js
const app = getApp()

Page({
  data: {
    banners: [],
    recommendGoods: []
  },

  onLoad() {
    this.loadData()
  },

  onShow() {
    this.updateCartBadge()
  },

  // 加载数据
  loadData() {
    const { API, IMAGE_BASE_URL } = require('../../utils/api')
    
    // 获取轮播图
    API.getBanner().then(res => {
      if (res.code === 200 && res.data) {
        const banners = res.data.map(item => {
          let imageUrl = item.imageUrl
          if (imageUrl && !imageUrl.startsWith('http')) {
            imageUrl = IMAGE_BASE_URL + (imageUrl.startsWith('/') ? '' : '/') + imageUrl
          }
          return {
            id: item.id,
            image: imageUrl,
            title: item.title,
            linkType: item.linkType,
            linkParam: item.linkParam
          }
        })
        this.setData({ banners })
      }
    }).catch(err => {
      console.error('获取轮播图失败:', err)
    })
    
    // 获取推荐商品
    API.getRecommend().then(res => {
      if (res.code === 200 && res.data) {
        const recommendGoods = res.data.map(item => {
          let imageUrl = item.image
          if (imageUrl && !imageUrl.startsWith('http')) {
            imageUrl = IMAGE_BASE_URL + (imageUrl.startsWith('/') ? '' : '/') + imageUrl
          }
          return {
            ...item,
            image: imageUrl
          }
        })
        this.setData({ recommendGoods })
      }
    }).catch(err => {
      console.error('获取推荐商品失败:', err)
    })
  },

  // 更新购物车角标
  updateCartBadge() {
    const total = app.globalData.cartTotal
    if (total > 0) {
      wx.setTabBarBadge({
        index: 1,
        text: total.toString()
      })
    } else {
      wx.removeTabBarBadge({
        index: 1
      })
    }
  },

  // 点击轮播图
  onBannerTap(e) {
    const { item } = e.currentTarget.dataset
    if (!item) return
    
    const { linkType, linkParam } = item
    
    switch (linkType) {
      case 1:
        if (linkParam) {
          wx.switchTab({ url: '/pages/goods/goods' })
          app.globalData.currentGoods = parseInt(linkParam)
        }
        break
      case 2:
        wx.switchTab({ url: '/pages/goods/goods' })
        if (linkParam) {
          app.globalData.currentCategory = parseInt(linkParam)
        }
        break
      default:
        break
    }
  },

  // 去选购页
  goToGoods() {
    wx.switchTab({ url: '/pages/goods/goods' })
  },

  // 去储值页
  goToRecharge() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      app.globalData.needLogin = true
      wx.switchTab({ url: '/pages/user/user' })
      return
    }
    wx.showToast({ title: '储值功能开发中', icon: 'none' })
  },

  // 去领券中心
  goToCoupon() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      app.globalData.needLogin = true
      wx.switchTab({ url: '/pages/user/user' })
      return
    }
    wx.navigateTo({ url: '/pages/coupon/coupon' })
  },

  // 查看订单
  viewOrders() {
    wx.switchTab({ url: '/pages/order/order' })
  },

  // 点击商品
  onGoodsTap(e) {
    const { id } = e.currentTarget.dataset
    wx.switchTab({ url: '/pages/goods/goods' })
    app.globalData.currentGoods = id
  },

  // 加入购物车
  addToCart(e) {
    const { goods } = e.currentTarget.dataset
    app.addToCart(goods)
    
    wx.showToast({
      title: '已加入购物车',
      icon: 'success'
    })
    
    this.updateCartBadge()
  }
})
