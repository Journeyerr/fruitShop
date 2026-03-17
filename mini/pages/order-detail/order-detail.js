// pages/order-detail/order-detail.js
const app = getApp()
const { formatTime } = require('../../utils/util')
const { API } = require('../../utils/api')

Page({
  data: {
    orderId: '',
    order: null
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ orderId: options.id })
      this.loadOrderDetail()
    } else {
      wx.showToast({
        title: '订单不存在',
        icon: 'none'
      })
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }
  },

  // 加载订单详情
  loadOrderDetail() {
    wx.showLoading({ title: '加载中...' })
    API.getOrderDetail(this.data.orderId).then(res => {
      wx.hideLoading()
      if (res.code === 200 && res.data) {
        const orderData = res.data
        
        // 转换订单状态
        const statusMap = {
          0: { status: 'pending', statusText: '待付款' },
          1: { status: 'to-deliver', statusText: '待配送' },
          2: { status: 'delivering', statusText: '待收货' },
          3: { status: 'completed', statusText: '已完成' },
          4: { status: 'cancelled', statusText: '已取消' }
        }
        
        const statusInfo = statusMap[orderData.status] || { status: 'pending', statusText: '待付款' }
        
        // 转换订单数据格式
        const order = {
          id: orderData.orderNo,
          status: statusInfo.status,
          statusText: statusInfo.statusText,
          createTime: orderData.createTime,
          payTime: orderData.payTime,
          deliveryTime: orderData.deliveryTime,
          completeTime: orderData.completeTime,
          goods: (orderData.items || []).map(item => ({
            id: item.goodsId,
            name: item.goodsName,
            price: item.price,
            quantity: item.quantity,
            image: item.goodsImage,
            unit: item.specification || '份'
          })),
          totalPrice: orderData.payAmount,
          goodsCount: orderData.goodsCount || 0,
          remark: orderData.remark,
          deliveryMethod: orderData.deliveryMethod || 'delivery',
          address: orderData.receiverName ? {
            name: orderData.receiverName,
            phone: orderData.receiverPhone,
            address: orderData.receiverAddress
          } : null
        }
        
        this.setData({ order })
      } else {
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

  // 复制订单号
  copyOrderId() {
    wx.setClipboardData({
      data: this.data.order.id,
      success: () => {
        wx.showToast({
          title: '已复制',
          icon: 'success'
        })
      }
    })
  },

  // 取消订单
  cancelOrder() {
    wx.showModal({
      title: '提示',
      content: '确定要取消这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '取消中...' })
          API.cancelOrder(this.data.orderId).then(res => {
            wx.hideLoading()
            if (res.code === 200) {
              wx.showToast({
                title: '订单已取消',
                icon: 'success'
              })
              setTimeout(() => {
                wx.navigateBack()
              }, 1500)
            } else {
              wx.showToast({
                title: res.message || '取消失败',
                icon: 'none'
              })
            }
          }).catch(() => {
            wx.hideLoading()
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
  payOrder() {
    wx.showLoading({ title: '支付中...' })
    API.payOrder(this.data.orderId).then(res => {
      wx.hideLoading()
      if (res.code === 200) {
        wx.showToast({
          title: '支付成功',
          icon: 'success',
          duration: 1500
        })
        // 重新加载订单详情
        setTimeout(() => {
          this.loadOrderDetail()
        }, 1500)
      } else {
        wx.showToast({
          title: res.message || '支付失败',
          icon: 'none'
        })
      }
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({
        title: '支付失败',
        icon: 'none'
      })
    })
  },

  // 确认收货
  confirmReceive() {
    wx.showModal({
      title: '提示',
      content: '确认已收到商品吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '处理中...' })
          API.completeOrder(this.data.orderId).then(res => {
            wx.hideLoading()
            if (res.code === 200) {
              wx.showToast({
                title: '已确认收货',
                icon: 'success',
                duration: 1500
              })
              // 重新加载订单详情
              setTimeout(() => {
                this.loadOrderDetail()
              }, 1500)
            } else {
              wx.showToast({
                title: res.message || '操作失败',
                icon: 'none'
              })
            }
          }).catch(() => {
            wx.hideLoading()
            wx.showToast({
              title: '操作失败',
              icon: 'none'
            })
          })
        }
      }
    })
  },

  // 联系商家
  contactMerchant() {
    wx.makePhoneCall({
      phoneNumber: '400-123-4567'
    })
  },

  // 再来一单
  reorder() {
    // TODO: 将订单商品加入购物车
    wx.switchTab({
      url: '/pages/goods/goods'
    })
  }
})
