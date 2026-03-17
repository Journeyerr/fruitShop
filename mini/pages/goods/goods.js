// pages/goods/goods.js
const app = getApp()

Page({
  data: {
    categories: [
      { id: 1, name: '当季水果' },
      { id: 2, name: '果切拼盘' },
      { id: 3, name: '鲜榨果汁' },
      { id: 4, name: '精品礼盒' }
    ],
    currentCategory: 1,
    scrollIntoView: '',
    categoryGoods: [],
    goodsList: [
      { id: 1, categoryId: 1, name: '云南高原蓝莓', price: 39.9, image: '/images/goods1.png', unit: '盒', stock: 100, sales: 256, desc: '新鲜采摘，果粒饱满' },
      { id: 2, categoryId: 1, name: '泰国金枕榴莲', price: 128.0, image: '/images/goods2.png', unit: '个', stock: 20, sales: 189, desc: '香甜软糯，果肉厚实' },
      { id: 3, categoryId: 1, name: '智利车厘子', price: 89.9, image: '/images/goods3.png', unit: '斤', stock: 50, sales: 432, desc: '进口优选，个大味甜' },
      { id: 4, categoryId: 1, name: '海南金煌芒果', price: 29.9, image: '/images/goods4.png', unit: '斤', stock: 80, sales: 567, desc: '香甜多汁，营养丰富' },
      { id: 5, categoryId: 1, name: '阳光玫瑰葡萄', price: 59.9, image: '/images/goods5.png', unit: '串', stock: 60, sales: 321, desc: '皮薄肉脆，清甜爽口' },
      { id: 6, categoryId: 1, name: '福建红心柚子', price: 19.9, image: '/images/goods6.png', unit: '个', stock: 100, sales: 654, desc: '果肉饱满，酸甜可口' },
      { id: 7, categoryId: 2, name: '精选水果拼盘', price: 49.9, image: '/images/goods7.png', unit: '份', stock: 30, sales: 178, desc: '多种水果，新鲜切配' },
      { id: 8, categoryId: 2, name: '蓝莓草莓拼盘', price: 45.9, image: '/images/goods8.png', unit: '份', stock: 25, sales: 245, desc: '双莓组合，营养丰富' },
      { id: 9, categoryId: 3, name: '鲜榨橙汁', price: 18.0, image: '/images/goods9.png', unit: '杯', stock: 50, sales: 890, desc: '现榨现卖，新鲜健康' },
      { id: 10, categoryId: 3, name: '芒果冰沙', price: 22.0, image: '/images/goods10.png', unit: '杯', stock: 50, sales: 678, desc: '清凉解暑，香甜可口' },
      { id: 11, categoryId: 4, name: '精选水果礼盒', price: 168.0, image: '/images/goods11.png', unit: '盒', stock: 15, sales: 89, desc: '送礼佳品，精美包装' },
      { id: 12, categoryId: 4, name: '进口水果礼盒', price: 298.0, image: '/images/goods12.png', unit: '盒', stock: 10, sales: 56, desc: '高端大气，品质保证' }
    ],
    cartTotal: 0,
    cartTotalPrice: 0,
    sectionHeights: [], // 存储各分类区块的高度
    isScrollingToCategory: false // 标记是否正在滚动到指定分类
  },

  onLoad(options) {
    // 加载数据
    this.loadData()
    
    // 检查是否从首页跳转过来
    if (app.globalData.currentCategory) {
      this.setData({ currentCategory: app.globalData.currentCategory })
      app.globalData.currentCategory = null
    }
    
    // 更新购物车信息
    this.updateCartInfo()
  },
  
  // 加载数据
  loadData() {
    const { API, IMAGE_BASE_URL } = require('../../utils/api')
    
    // 获取分类列表
    API.getCategories().then(res => {
      if (res.code === 200 && res.data) {
        const categories = res.data.map(item => ({
          id: item.id,
          name: item.name
        }))
        this.setData({ categories })
        
        // 加载各分类的商品
        this.loadCategoryGoods()
      }
    }).catch(err => {
      console.error('获取分类失败:', err)
      wx.showToast({
        title: '加载分类失败',
        icon: 'none'
      })
    })
  },
  
  // 加载各分类的商品
  loadCategoryGoods() {
    const { API, IMAGE_BASE_URL } = require('../../utils/api')
    const { categories } = this.data
    
    const categoryGoods = []
    let completedRequests = 0
    
    categories.forEach((category, index) => {
      API.getGoodsByCategory(category.id).then(res => {
        if (res.code === 200 && res.data) {
          // 处理商品图片URL
          const goods = res.data.map(item => {
            let imageUrl = item.image
            if (imageUrl && !imageUrl.startsWith('http')) {
              imageUrl = IMAGE_BASE_URL + (imageUrl.startsWith('/') ? '' : '/') + imageUrl
            }
            return {
              ...item,
              image: imageUrl
            }
          })
          
          // 按顺序插入到正确的位置
          categoryGoods[index] = {
            categoryId: category.id,
            categoryName: category.name,
            goods: goods
          }
        }
        
        completedRequests++
        
        // 所有请求完成后更新数据
        if (completedRequests === categories.length) {
          this.setData({ categoryGoods }, () => {
            // 数据更新完成后重新计算高度
            setTimeout(() => {
              this.calculateSectionHeights()
            }, 300)
          })
        }
      }).catch(err => {
        console.error(`获取分类${category.name}商品失败:`, err)
        completedRequests++
      })
    })
  },

  onShow() {
    this.updateCartInfo()
    this.updateCartBadge()
  },

  onReady() {
    // 计算各分类区块的位置
    this.calculateSectionHeights()
  },

  // 初始化分类商品数据
  initCategoryGoods() {
    const { categories, goodsList } = this.data
    const categoryGoods = categories.map(cat => {
      return {
        categoryId: cat.id,
        categoryName: cat.name,
        goods: goodsList.filter(item => item.categoryId === cat.id)
      }
    })
    this.setData({ categoryGoods })
  },

  // 计算各分类区块的高度
  calculateSectionHeights() {
    const query = wx.createSelectorQuery().in(this)
    query.selectAll('.goods-section').boundingClientRect(rects => {
      if (rects && rects.length > 0) {
        // 计算每个区块的累计高度
        let cumulativeHeight = 0
        const sectionHeights = rects.map((rect, index) => {
          const height = rect.height
          const data = {
            categoryId: this.data.categories[index].id,
            top: cumulativeHeight,
            height: height
          }
          cumulativeHeight += height
          return data
        })
        this.setData({ sectionHeights })
      }
    }).exec()
  },

  // 更新购物车信息
  updateCartInfo() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      // 未登录时显示0
      this.setData({
        cartTotal: 0,
        cartTotalPrice: 0
      })
      return
    }
    
    const { cartTotal, cartTotalPrice } = app.globalData
    this.setData({
      cartTotal,
      cartTotalPrice
    })
  },

  // 更新购物车角标
  updateCartBadge() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      // 未登录时不显示角标
      wx.removeTabBarBadge({
        index: 1
      })
      return
    }
    
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

  // 点击分类
  onCategoryTap(e) {
    const { id } = e.currentTarget.dataset
    this.setData({
      currentCategory: id,
      scrollIntoView: `section-${id}`,
      isScrollingToCategory: true
    })
    
    // 滚动动画结束后，恢复滚动监听
    setTimeout(() => {
      this.setData({ isScrollingToCategory: false })
    }, 300)
  },

  // 商品列表滚动
  onGoodsScroll(e) {
    // 如果是点击分类触发的滚动，不处理
    if (this.data.isScrollingToCategory) return
    
    const scrollTop = e.detail.scrollTop
    const { sectionHeights, currentCategory } = this.data
    
    if (!sectionHeights || sectionHeights.length === 0) return
    
    // 查找当前滚动位置对应的分类
    for (let i = sectionHeights.length - 1; i >= 0; i--) {
      const section = sectionHeights[i]
      // 如果滚动位置超过该区块的起始位置，则高亮该分类
      if (scrollTop >= section.top - 20) {
        if (currentCategory !== section.categoryId) {
          this.setData({ currentCategory: section.categoryId })
        }
        break
      }
    }
  },

  // 加入购物车
  addToCart(e) {
    const { goods } = e.currentTarget.dataset
    app.addToCart(goods)
    
    wx.showToast({
      title: '已加入购物车',
      icon: 'success'
    })
    
    this.updateCartInfo()
    this.updateCartBadge()
  },

  // 查看购物车
  viewCart() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showModal({
        title: '提示',
        content: '请先登录后再结算',
        confirmText: '去登录',
        success: (res) => {
          if (res.confirm) {
            // 标记需要自动拉起登录
            app.globalData.needLogin = true
            wx.switchTab({
              url: '/pages/user/user'
            })
          }
        }
      })
      return
    }

    if (app.globalData.cartTotal === 0) {
      wx.showToast({
        title: '购物车是空的',
        icon: 'none'
      })
      return
    }
    wx.navigateTo({
      url: '/pages/cart/cart'
    })
  }
})
