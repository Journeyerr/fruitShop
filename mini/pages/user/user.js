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
    orderCounts: {
      pending: 0,
      delivering: 0,
      completed: 0
    },
    menuItems: [
      { id: 'address', icon: '/images/menu-address.png', name: '收货地址', badge: '' },
      { id: 'coupon', icon: '/images/menu-coupon.png', name: '我的优惠券', badge: '3张' },
      { id: 'favorite', icon: '/images/menu-favorite.png', name: '我的收藏', badge: '' },
      { id: 'feedback', icon: '/images/menu-feedback.png', name: '意见反馈', badge: '' },
      { id: 'about', icon: '/images/menu-about.png', name: '关于我们', badge: '' },
      { id: 'service', icon: '/images/service.png', name: '客服热线：400-123-4567', badge: '' }
    ]
  },

  onLoad() {
    // 先检查登录状态
    this.checkLoginStatus()
    
    // 检查是否需要自动拉起登录（且当前未登录）
    const token = wx.getStorageSync('token')
    if (app.globalData.needLogin && !token) {
      app.globalData.needLogin = false
      // 延迟一点时间，等页面渲染完成
      setTimeout(() => {
        this.getUserProfile()
      }, 600)
    }
  },

  onShow() {
    this.loadOrderCounts()
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
      
      // 设置当前用户ID
      app.setCurrentUser(userInfo.id)
    }
  },

  // 微信登录
  getUserProfile(e) {
    const { API } = require('../../utils/api')
    
    // 立即显示加载提示
    wx.showLoading({ title: '登录中...', mask: true })
    
    // 1. 先获取用户信息
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: (userRes) => {
        console.log('获取用户信息成功', userRes.userInfo)
        
        // 2. 获取微信登录code
        wx.login({
          success: (loginRes) => {
            if (loginRes.code) {
              // 3. 调用后端登录接口
              API.wechatLogin({
                code: loginRes.code,
                nickname: userRes.userInfo.nickName,
                avatar: userRes.userInfo.avatarUrl
              }).then(res => {
                wx.hideLoading()
                
                if (res.code === 200 && res.data) {
                  const { token, userInfo } = res.data
                  
                  // 保存token和用户信息
                  wx.setStorageSync('token', token)
                  wx.setStorageSync('userInfo', userInfo)
                  app.globalData.userInfo = userInfo
                  
                  // 设置当前用户ID，加载该用户的购物车
                  app.setCurrentUser(userInfo.id)
                  
                  this.setData({
                    isLogin: true,
                    userInfo: userInfo
                  })
                  
                  wx.showToast({
                    title: '登录成功',
                    icon: 'success'
                  })
                } else {
                  wx.showToast({
                    title: res.message || '登录失败',
                    icon: 'none'
                  })
                }
              }).catch(err => {
                wx.hideLoading()
                console.error('登录失败:', err)
                wx.showToast({
                  title: '登录失败',
                  icon: 'none'
                })
              })
            } else {
              wx.hideLoading()
              wx.showToast({
                title: '获取登录凭证失败',
                icon: 'none'
              })
            }
          },
          fail: () => {
            wx.hideLoading()
            wx.showToast({
              title: '微信登录失败',
              icon: 'none'
            })
          }
        })
      },
      fail: (err) => {
        wx.hideLoading()
        console.log('获取用户信息失败', err)
        wx.showToast({
          title: '登录取消',
          icon: 'none'
        })
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
          // 调用后端退出接口
          API.logout().then(() => {
            // 清除本地数据
            wx.removeStorageSync('userInfo')
            wx.removeStorageSync('token')
            app.globalData.userInfo = null
            
            // 清空当前用户，清空购物车显示
            app.clearCurrentUser()
            
            this.setData({
              isLogin: false,
              userInfo: {
                nickname: '',
                avatar: '',
                phone: ''
              }
            })
            
            wx.showToast({
              title: '已退出登录',
              icon: 'success'
            })
          }).catch(err => {
            console.error('退出登录失败:', err)
            // 即使接口失败，也清除本地数据
            wx.removeStorageSync('userInfo')
            wx.removeStorageSync('token')
            app.globalData.userInfo = null
            
            // 清空当前用户，清空购物车显示
            app.clearCurrentUser()
            
            this.setData({
              isLogin: false,
              userInfo: {
                nickname: '',
                avatar: '',
                phone: ''
              }
            })
          })
        }
      }
    })
  },

  // 加载订单统计
  loadOrderCounts() {
    // TODO: 调用API获取订单统计
    // const { API } = require('../../utils/api')
    // API.getOrderCounts().then(res => {
    //   this.setData({ orderCounts: res })
    // })
  },

  // 查看订单
  viewOrders(e) {
    const { status } = e.currentTarget.dataset
    wx.switchTab({
      url: '/pages/order/order'
    })
    // 传递订单状态
    app.globalData.orderStatus = status
  },

  // 点击菜单项
  onMenuTap(e) {
    const { id } = e.currentTarget.dataset
    
    // 如果是客服热线，直接拨打
    if (id === 'service') {
      this.callService()
      return
    }
    
    // 检查是否登录
    if (!this.data.isLogin) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      return
    }
    
    switch (id) {
      case 'address':
        wx.navigateTo({
          url: '/pages/address/address'
        })
        break
      case 'coupon':
        wx.navigateTo({
          url: '/pages/coupon/coupon'
        })
        break
      case 'favorite':
        wx.navigateTo({
          url: '/pages/favorite/favorite'
        })
        break
      case 'feedback':
        wx.navigateTo({
          url: '/pages/feedback/feedback'
        })
        break
      case 'about':
        wx.navigateTo({
          url: '/pages/about/about'
        })
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
