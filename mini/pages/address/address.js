// pages/address/address.js
const { API } = require('../../utils/api')

Page({
  data: {
    addresses: [],
    selectMode: false // 是否为选择模式
  },

  onLoad(options) {
    // 如果是从购物车跳转来的，设置为选择模式
    if (options.select === 'true') {
      this.setData({ selectMode: true })
    }
  },

  onShow() {
    this.loadAddresses()
  },

  // 加载地址列表
  loadAddresses() {
    wx.showLoading({ title: '加载中...' })
    API.getAddresses().then(res => {
      wx.hideLoading()
      if (res.code === 200) {
        const addresses = res.data || []
        this.setData({ addresses })
      } else if (res.code !== 401) {
        wx.showToast({
          title: res.message || '加载失败',
          icon: 'none'
        })
      }
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    })
  },

  // 选择地址
  selectAddress(e) {
    if (!this.data.selectMode) return
    
    const { item } = e.currentTarget.dataset
    
    // 使用全局数据传递选中的地址
    const app = getApp()
    app.globalData.selectedAddress = item
    
    wx.navigateBack()
  },

  // 新增地址
  addAddress() {
    wx.navigateTo({
      url: '/pages/address-edit/address-edit'
    })
  },

  // 编辑地址
  editAddress(e) {
    const { item } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/address-edit/address-edit?id=${item.id}`
    })
  },

  // 删除地址
  deleteAddress(e) {
    const { id } = e.currentTarget.dataset
    wx.showModal({
      title: '提示',
      content: '确定要删除这个地址吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '删除中...' })
          API.deleteAddress(id).then(res => {
            wx.hideLoading()
            if (res.code === 200) {
              wx.showToast({
                title: '删除成功',
                icon: 'success'
              })
              this.loadAddresses()
            } else {
              wx.showToast({
                title: res.message || '删除失败',
                icon: 'none'
              })
            }
          }).catch(() => {
            wx.hideLoading()
            wx.showToast({
              title: '删除失败',
              icon: 'none'
            })
          })
        }
      }
    })
  },

  // 阻止事件冒泡
  stopPropagation() {}
})
