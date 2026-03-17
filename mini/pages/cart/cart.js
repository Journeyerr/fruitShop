// pages/cart/cart.js
const app = getApp()
const { formatPrice } = require('../../utils/util')

Page({
  data: {
    isLogin: false,
    cart: [],
    totalPrice: 0,
    allSelected: true,
    deliveryMethod: 'pickup',
    selectedAddress: null,
    remark: ''
  },

  onLoad() {
    this.checkLogin()
    this.loadCart()
    this.loadDefaultAddress()
  },

  onShow() {
    this.checkLogin()
    this.loadCart()
    
    // 检查是否有从地址页面选中的地址（优先级高于默认地址）
    const app = getApp()
    if (app.globalData.selectedAddress) {
      const address = app.globalData.selectedAddress
      const selectedAddress = {
        ...address,
        name: address.receiverName,
        phone: address.receiverPhone,
        fullAddress: `${address.province}${address.city}${address.district}${address.detailAddress}`
      }
      this.setData({ selectedAddress })
      // 清除全局数据
      app.globalData.selectedAddress = null
    } else {
      // 没有选中地址时才加载默认地址
      this.loadDefaultAddress()
    }
  },

  // 检查登录状态
  checkLogin() {
    const token = wx.getStorageSync('token')
    this.setData({
      isLogin: !!token
    })
  },

  // 加载购物车数据
  loadCart() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      // 未登录时显示空购物车
      this.setData({
        cart: [],
        totalPrice: 0,
        allSelected: true
      })
      return
    }
    
    const cart = app.globalData.cart
    const totalPrice = app.globalData.cartTotalPrice
    const allSelected = cart.every(item => item.selected !== false)
    
    this.setData({
      cart,
      totalPrice,
      allSelected
    })
  },

  // 增加数量
  increase(e) {
    const { index } = e.currentTarget.dataset
    const cart = this.data.cart
    cart[index].quantity++
    app.updateCartInfo()
    this.loadCart()
  },

  // 减少数量
  decrease(e) {
    const { index } = e.currentTarget.dataset
    const cart = this.data.cart
    if (cart[index].quantity > 1) {
      cart[index].quantity--
      app.updateCartInfo()
      this.loadCart()
    }
  },

  // 删除商品
  deleteGoods(e) {
    const { index } = e.currentTarget.dataset
    wx.showModal({
      title: '提示',
      content: '确定要删除这个商品吗？',
      success: (res) => {
        if (res.confirm) {
          const cart = this.data.cart
          cart.splice(index, 1)
          app.updateCartInfo()
          this.loadCart()
          
          wx.showToast({
            title: '已删除',
            icon: 'success'
          })
        }
      }
    })
  },

  // 全选
  selectAll() {
    const cart = this.data.cart
    const allSelected = !this.data.allSelected
    cart.forEach(item => {
      item.selected = allSelected
    })
    
    this.setData({ cart, allSelected })
    app.updateCartInfo()
  },

  // 选择配送方式
  selectDeliveryMethod(e) {
    const { method } = e.currentTarget.dataset
    this.setData({ deliveryMethod: method })
  },

  // 加载默认地址
  loadDefaultAddress() {
    const { API } = require('../../utils/api')
    
    // 先检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      this.setData({ selectedAddress: null })
      return
    }

    API.getAddresses().then(res => {
      if (res.code === 200 && res.data && res.data.length > 0) {
        // 转换数据格式
        const addresses = res.data.map(item => ({
          ...item,
          name: item.receiverName,
          phone: item.receiverPhone,
          detail: item.detailAddress,
          fullAddress: `${item.province}${item.city}${item.district}${item.detailAddress}`
        }))
        
        // 优先选择默认地址
        const defaultAddress = addresses.find(item => item.isDefault === 1)
        const selectedAddress = defaultAddress || addresses[0]
        this.setData({ selectedAddress })
      } else {
        this.setData({ selectedAddress: null })
      }
    }).catch(() => {
      this.setData({ selectedAddress: null })
    })
  },

  // 选择地址
  selectAddress() {
    wx.navigateTo({
      url: '/pages/address/address?select=true'
    })
  },

  // 输入备注
  onRemarkInput(e) {
    this.setData({ remark: e.detail.value })
  },

  // 设置选中的地址（从地址选择页面返回时调用）
  setSelectedAddress(address) {
    // 转换数据格式以适配购物车页面显示
    const selectedAddress = {
      ...address,
      name: address.receiverName,
      phone: address.receiverPhone,
      fullAddress: `${address.province}${address.city}${address.district}${address.detailAddress}`
    }
    this.setData({ selectedAddress })
  },

  // 去登录
  goLogin() {
    const app = getApp()
    app.globalData.needLogin = true
    wx.switchTab({
      url: '/pages/user/user'
    })
  },

  // 去选购
  goShopping() {
    wx.switchTab({
      url: '/pages/goods/goods'
    })
  },

  // 提交订单
  submitOrder() {
    // 检查登录状态
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showModal({
        title: '提示',
        content: '请先登录后再下单',
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

    if (this.data.cart.length === 0) {
      wx.showToast({
        title: '购物车是空的',
        icon: 'none'
      })
      return
    }

    // 配送方式验证
    if (this.data.deliveryMethod === 'delivery' && !this.data.selectedAddress) {
      wx.showToast({
        title: '请选择收货地址',
        icon: 'none'
      })
      return
    }

    // 构造订单数据
    const { API } = require('../../utils/api')
    const { cart, totalPrice, deliveryMethod, selectedAddress, remark } = this.data
    
    // 构造订单项
    const items = cart.map(item => ({
      goodsId: item.id,
      goodsName: item.name,
      goodsImage: item.image,
      price: item.price,
      quantity: item.quantity,
      specification: item.unit
    }))

    // 构造订单数据
    const orderData = {
      items,
      goodsAmount: totalPrice,
      deliveryFee: 0,
      discountAmount: 0,
      payAmount: totalPrice,
      remark: remark || '',
      deliveryMethod: deliveryMethod
    }

    // 如果是配送，添加地址信息
    if (deliveryMethod === 'delivery' && selectedAddress) {
      orderData.addressId = selectedAddress.id
      orderData.receiverName = selectedAddress.receiverName || selectedAddress.name
      orderData.receiverPhone = selectedAddress.receiverPhone || selectedAddress.phone
      orderData.receiverAddress = selectedAddress.fullAddress || 
        `${selectedAddress.province}${selectedAddress.city}${selectedAddress.district}${selectedAddress.detailAddress || selectedAddress.detail}`
    }

    wx.showLoading({ title: '提交中...' })

    // 调用后端API创建订单
    API.createOrder(orderData).then(res => {
      wx.hideLoading()
      if (res.code === 200 && res.data) {
        // 清空购物车
        app.clearCart()
        
        wx.showToast({
          title: '下单成功',
          icon: 'success',
          duration: 1500
        })

        // 跳转到订单详情页
        setTimeout(() => {
          wx.redirectTo({
            url: `/pages/order-detail/order-detail?id=${res.data.id}`
          })
        }, 1500)
      } else {
        wx.showToast({
          title: res.message || '下单失败',
          icon: 'none'
        })
      }
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({
        title: '下单失败',
        icon: 'none'
      })
    })
  }
})
