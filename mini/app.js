// app.js
App({
  globalData: {
    userInfo: null,
    currentUserId: null, // 当前登录用户ID
    cart: [], // 购物车数据
    cartTotal: 0, // 购物车商品总数
    cartTotalPrice: 0, // 购物车总价
    selectedAddress: null // 选中的收货地址
  },

  onLaunch() {
    // 获取当前登录用户
    const token = wx.getStorageSync('token')
    const userInfo = wx.getStorageSync('userInfo')
    
    if (token && userInfo) {
      this.globalData.currentUserId = userInfo.id
      this.globalData.userInfo = userInfo
    }
    
    // 初始化时从本地存储读取购物车数据
    this.loadUserCart()
  },

  // 加载当前用户的购物车
  loadUserCart() {
    const allCart = wx.getStorageSync('cart') || []
    const userId = this.globalData.currentUserId
    
    if (userId) {
      // 已登录，只加载该用户的购物车
      this.globalData.cart = allCart.filter(item => item.userId === userId)
    } else {
      // 未登录，清空购物车
      this.globalData.cart = []
    }
    
    this.updateCartInfo()
  },

  // 更新购物车信息
  updateCartInfo() {
    const cart = this.globalData.cart
    let total = 0
    let totalPrice = 0
    
    cart.forEach(item => {
      total += item.quantity
      totalPrice += item.price * item.quantity
    })
    
    this.globalData.cartTotal = total
    this.globalData.cartTotalPrice = totalPrice.toFixed(2)
    
    // 保存到本地存储（保存所有用户的购物车）
    const allCart = wx.getStorageSync('cart') || []
    const userId = this.globalData.currentUserId
    
    if (userId) {
      // 移除该用户的旧数据
      const otherUserCart = allCart.filter(item => item.userId !== userId)
      // 添加该用户的新数据
      const userCart = cart.map(item => ({ ...item, userId }))
      // 合并保存
      wx.setStorageSync('cart', [...otherUserCart, ...userCart])
    } else {
      // 未登录时只保存其他用户的数据
      wx.setStorageSync('cart', allCart.filter(item => item.userId))
    }
  },

  // 添加商品到购物车
  addToCart(goods) {
    const userId = this.globalData.currentUserId
    
    // 未登录时允许加入购物车，但标记为临时数据
    const cart = this.globalData.cart
    const existItem = cart.find(item => item.id === goods.id)
    
    if (existItem) {
      existItem.quantity += goods.quantity || 1
    } else {
      cart.push({
        ...goods,
        quantity: goods.quantity || 1,
        userId: userId || 'temp' // 临时用户标记
      })
    }
    
    this.updateCartInfo()
  },

  // 清空购物车
  clearCart() {
    this.globalData.cart = []
    this.updateCartInfo()
  },

  // 用户登录后设置用户ID
  setCurrentUser(userId) {
    this.globalData.currentUserId = userId
    this.loadUserCart()
  },

  // 用户退出登录
  clearCurrentUser() {
    this.globalData.currentUserId = null
    this.globalData.cart = []
    this.globalData.cartTotal = 0
    this.globalData.cartTotalPrice = 0
  }
})
