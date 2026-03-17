// pages/index/index.js
const app = getApp()

Page({
  data: {
    banners: [
      { id: 1, image: '/images/banner1.png', url: '' },
      { id: 2, image: '/images/banner2.png', url: '' },
      { id: 3, image: '/images/banner3.png', url: '' }
    ],
    recommendGoods: [
      { id: 1, name: '云南高原蓝莓', price: 39.9, image: '/images/goods1.png', unit: '盒', sales: 256 },
      { id: 2, name: '泰国金枕榴莲', price: 128.0, image: '/images/goods2.png', unit: '个', sales: 189 },
      { id: 3, name: '智利车厘子', price: 89.9, image: '/images/goods3.png', unit: '斤', sales: 432 },
      { id: 4, name: '海南金煌芒果', price: 29.9, image: '/images/goods4.png', unit: '斤', sales: 567 },
      { id: 5, name: '阳光玫瑰葡萄', price: 59.9, image: '/images/goods5.png', unit: '串', sales: 321 },
      { id: 6, name: '福建红心柚子', price: 19.9, image: '/images/goods6.png', unit: '个', sales: 654 }
    ]
  },

  onLoad() {
    // 加载数据
    this.loadData()
  },

  onShow() {
    // 更新购物车角标
    this.updateCartBadge()
  },

  // 加载数据
  loadData() {
    const { API, IMAGE_BASE_URL } = require('../../utils/api')
    
    // 获取轮播图
    API.getBanner().then(res => {
      if (res.code === 200 && res.data) {
        // 处理轮播图数据
        const banners = res.data.map(item => {
          // 处理图片URL
          let imageUrl = item.imageUrl
          // 如果是相对路径，拼接完整URL
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
      // 可以在这里显示错误提示
      wx.showToast({
        title: '加载轮播图失败',
        icon: 'none',
        duration: 2000
      })
    })
    
    // 获取推荐商品
    API.getRecommend().then(res => {
      if (res.code === 200 && res.data) {
        // 处理商品图片URL
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
      // 可以在这里显示错误提示
      wx.showToast({
        title: '加载推荐商品失败',
        icon: 'none',
        duration: 2000
      })
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
    
    // linkType: 1-商品详情 2-分类 3-外链
    switch (linkType) {
      case 1:
        // 跳转到商品详情
        if (linkParam) {
          wx.switchTab({
            url: '/pages/goods/goods'
          })
          app.globalData.currentGoods = parseInt(linkParam)
        }
        break
      case 2:
        // 跳转到分类页
        wx.switchTab({
          url: '/pages/goods/goods'
        })
        if (linkParam) {
          app.globalData.currentCategory = parseInt(linkParam)
        }
        break
      case 3:
        // 外链（小程序中通常用webview打开）
        // 这里暂不处理，可根据需求扩展
        break
      default:
        break
    }
  },

  // 去选购页
  goToGoods() {
    wx.switchTab({
      url: '/pages/goods/goods'
    })
  },

  // 点击商品
  onGoodsTap(e) {
    const { id } = e.currentTarget.dataset
    wx.switchTab({
      url: '/pages/goods/goods'
    })
    // 传递商品ID
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
