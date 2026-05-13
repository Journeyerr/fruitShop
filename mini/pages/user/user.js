// pages/user/user.js
const app = getApp()

Page({
  data: {
    isLogin: false,
    userInfo: {
      nickname: '',
      avatar: '',
      phone: ''
    },
    walletInfo: {
      balance: '0.00',
      couponCount: 0,
      points: 0
    },
    orderCounts: {
      pending: 0,
      delivering: 0,
      completed: 0
    },
    menuItems: [
      { id: 'address', icon: '/images/location.png', name: '收货地址', badge: '', iconBg: 'bg-green' },
      { id: 'coupon', icon: '/images/menu-coupon.png', name: '我的优惠券', badge: '', iconBg: 'bg-red' },
      { id: 'favorite', icon: '/images/menu-favorite.png', name: '我的收藏', badge: '', iconBg: 'bg-orange' },
      { id: 'feedback', icon: '/images/menu-feedback.png', name: '意见反馈', badge: '', iconBg: 'bg-blue' },
      { id: 'about', icon: '/images/menu-about.png', name: '关于我们', badge: '', iconBg: 'bg-purple' },
      { id: 'service', icon: '/images/service.png', name: '客服热线：400-123-4567', badge: '', iconBg: 'bg-gray' }
    ]
  },

  onLoad() {
    this.checkLoginStatus()
    
    const token = wx.getStorageSync('token')
    if (app.globalData.needLogin && !token) {
      app.globalData.needLogin = false
      setTimeout(() => {
        this.getUserProfile()
      }, 600)
    }
  },

  onShow() {
    if (this.data.isLogin) {
      this.loadOrderCounts()
      this.loadWalletInfo()
    }
  },

  // 检查登录状态
  checkLoginStatus() {
    const token = wx.getStorageSync('token')
    const userInfo = wx.getStorageSync('userInfo')
    
    if (token && userInfo && userInfo.nickname) {
      this.setData({
        isLogin: true,
        userInfo: userInfo
      })
      app.globalData.userInfo = userInfo
      app.setCurrentUser(userInfo.id)
    }
  },

  // 加载钱包信息
  loadWalletInfo() {
    const { API } = require('../../utils/api')
    API.getMemberCouponCounts().then(res => {
      if (res.code === 200 && res.data) {
        this.setData({
          'walletInfo.couponCount': (res.data.usable || 0)
        })
        // 更新优惠券badge
        const menuItems = this.data.menuItems.map(item => {
          if (item.id === 'coupon') {
            return { ...item, badge: res.data.usable > 0 ? res.data.usable + '张' : '' }
          }
          return item
        })
        this.setData({ menuItems })
      }
    }).catch(err => {
      console.error('加载钱包信息失败:', err)
    })
  },

  // 微信登录
  getUserProfile(e) {
    const { API } = require('../../utils/api')
    
    wx.showLoading({ title: '登录中...', mask: true })
    
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: (userRes) => {
        wx.login({
          success: (loginRes) => {
            if (loginRes.code) {
              API.wechatLogin({
                code: loginRes.code,
                nickname: userRes.userInfo.nickName,
                avatar: userRes.userInfo.avatarUrl
              }).then(res => {
                wx.hideLoading()
                
                if (res.code === 200 && res.data) {
                  const { token, userInfo } = res.data
                  
                  wx.setStorageSync('token', token)
                  wx.setStorageSync('userInfo', userInfo)
                  app.globalData.userInfo = userInfo
                  app.setCurrentUser(userInfo.id)
                  
                  this.setData({
                    isLogin: true,
                    userInfo: userInfo
                  })
                  
                  wx.showToast({ title: '登录成功', icon: 'success' })
                  this.loadWalletInfo()
                } else {
                  wx.showToast({ title: res.message || '登录失败', icon: 'none' })
                }
              }).catch(err => {
                wx.hideLoading()
                wx.showToast({ title: '登录失败', icon: 'none' })
              })
            } else {
              wx.hideLoading()
              wx.showToast({ title: '获取登录凭证失败', icon: 'none' })
            }
          },
          fail: () => {
            wx.hideLoading()
            wx.showToast({ title: '微信登录失败', icon: 'none' })
          }
        })
      },
      fail: (err) => {
        wx.hideLoading()
        wx.showToast({ title: '登录取消', icon: 'none' })
      }
    })
  },

  // 退出登录
  logout() {
    const { API } = require('../../utils/api')
    
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          API.logout().then(() => {
            this.doLogout()
          }).catch(err => {
            this.doLogout()
          })
        }
      }
    })
  },

  doLogout() {
    wx.removeStorageSync('userInfo')
    wx.removeStorageSync('token')
    app.globalData.userInfo = null
    app.clearCurrentUser()
    
    this.setData({
      isLogin: false,
      userInfo: { nickname: '', avatar: '', phone: '' },
      walletInfo: { balance: '0.00', couponCount: 0, points: 0 }
    })
    
    wx.showToast({ title: '已退出登录', icon: 'success' })
  },

  // 加载订单统计
  loadOrderCounts() {
    // TODO: 调用API获取订单统计
  },

  // 去储值
  goToRecharge() {
    wx.showToast({ title: '储值功能开发中', icon: 'none' })
  },

  // 去优惠券
  goToCoupon() {
    wx.navigateTo({ url: '/pages/coupon/coupon' })
  },

  // 查看订单
  viewOrders(e) {
    const { status } = e.currentTarget.dataset
    wx.switchTab({ url: '/pages/order/order' })
    app.globalData.orderStatus = status
  },

  // 点击菜单项
  onMenuTap(e) {
    const { id } = e.currentTarget.dataset
    
    if (id === 'service') {
      this.callService()
      return
    }
    
    if (!this.data.isLogin) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    
    switch (id) {
      case 'address':
        wx.navigateTo({ url: '/pages/address/address' })
        break
      case 'coupon':
        wx.navigateTo({ url: '/pages/coupon/coupon' })
        break
      case 'favorite':
      case 'feedback':
      case 'about':
        wx.showToast({ title: '功能开发中', icon: 'none' })
        break
    }
  },

  // 客服电话
  callService() {
    wx.makePhoneCall({
      phoneNumber: '400-123-4567'
    })
  }
})
