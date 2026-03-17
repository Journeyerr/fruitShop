const { API } = require('../../utils/api')

Page({
  data: {
    id: null,
    name: '',
    phone: '',
    region: '',
    province: '',
    city: '',
    district: '',
    detail: '',
    isDefault: false,
    regionIndex: [0, 0, 0],
    regionData: [[], [], []],
    provinces: [],
    cities: [],
    districts: []
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ id: options.id })
      this.loadAddress(options.id)
      wx.setNavigationBarTitle({ title: '编辑地址' })
    } else {
      wx.setNavigationBarTitle({ title: '新增地址' })
      this.loadProvinces()
    }
  },

  // 加载省份
  loadProvinces() {
    API.getProvinces().then(res => {
      if (res.code === 200 && res.data) {
        const provinces = res.data
        const provinceNames = provinces.map(item => item.name)
        this.setData({
          provinces,
          'regionData[0]': provinceNames
        })
        
        // 如果是编辑地址，找到对应省份并加载城市
        if (this.data.province) {
          const provinceIndex = provinces.findIndex(item => item.name === this.data.province)
          if (provinceIndex > -1) {
            this.setData({ 'regionIndex[0]': provinceIndex })
            const provinceCode = provinces[provinceIndex].code
            this.loadCitiesForEdit(provinceCode, this.data.city, this.data.district)
          }
        } else if (!this.data.id && provinces.length > 0) {
          // 如果是新增地址，加载第一个省份的城市
          this.loadCities(provinces[0].code)
        }
      }
    })
  },

  // 加载城市（编辑模式）
  loadCitiesForEdit(provinceCode, cityName, districtName) {
    API.getCities(provinceCode).then(res => {
      if (res.code === 200 && res.data) {
        const cities = res.data
        const cityNames = cities.map(item => item.name)
        this.setData({
          cities,
          'regionData[1]': cityNames
        })
        
        // 找到对应城市并加载区县
        const cityIndex = cities.findIndex(item => item.name === cityName)
        if (cityIndex > -1) {
          this.setData({ 'regionIndex[1]': cityIndex })
          const cityCode = cities[cityIndex].code
          this.loadDistrictsForEdit(cityCode, districtName)
        }
      }
    })
  },

  // 加载区县（编辑模式）
  loadDistrictsForEdit(cityCode, districtName) {
    API.getDistricts(cityCode).then(res => {
      if (res.code === 200 && res.data) {
        const districts = res.data
        const districtNames = districts.map(item => item.name)
        this.setData({
          districts,
          'regionData[2]': districtNames
        })
        
        // 找到对应区县
        const districtIndex = districts.findIndex(item => item.name === districtName)
        if (districtIndex > -1) {
          this.setData({ 'regionIndex[2]': districtIndex })
        }
      }
    })
  },

  // 加载城市
  loadCities(provinceCode) {
    API.getCities(provinceCode).then(res => {
      if (res.code === 200 && res.data) {
        const cities = res.data
        const cityNames = cities.map(item => item.name)
        this.setData({
          cities,
          'regionData[1]': cityNames
        })
        
        // 加载第一个城市的区县
        if (cities.length > 0) {
          this.loadDistricts(cities[0].code)
        } else {
          this.setData({
            districts: [],
            'regionData[2]': []
          })
        }
      }
    })
  },

  // 加载区县
  loadDistricts(cityCode) {
    API.getDistricts(cityCode).then(res => {
      if (res.code === 200 && res.data) {
        const districts = res.data
        const districtNames = districts.map(item => item.name)
        this.setData({
          districts,
          'regionData[2]': districtNames
        })
      }
    })
  },

  loadAddress(id) {
    wx.showLoading({ title: '加载中...' })
    API.getAddressById(id).then(res => {
      wx.hideLoading()
      if (res.code === 200 && res.data) {
        const address = res.data
        this.setData({
          name: address.receiverName,
          phone: address.receiverPhone,
          region: `${address.province} ${address.city} ${address.district}`,
          province: address.province,
          city: address.city,
          district: address.district,
          detail: address.detailAddress,
          isDefault: address.isDefault === 1
        })
        
        // 加载省份列表后，会自动匹配到对应的省市
        this.loadProvinces()
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

  onNameInput(e) {
    this.setData({ name: e.detail.value })
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value })
  },

  // 地区选择变化
  onRegionChange(e) {
    const values = e.detail.value
    const { provinces, cities, districts } = this.data
    
    if (provinces[values[0]] && cities[values[1]] && districts[values[2]]) {
      const province = provinces[values[0]].name
      const city = cities[values[1]].name
      const district = districts[values[2]].name
      const provinceCode = provinces[values[0]].code
      const cityCode = cities[values[1]].code
      const districtCode = districts[values[2]].code
      
      this.setData({
        regionIndex: values,
        region: `${province} ${city} ${district}`,
        province,
        city,
        district,
        provinceCode,
        cityCode,
        districtCode
      })
    }
  },

  // 地区列变化
  onRegionColumnChange(e) {
    const { column, value } = e.detail
    const { provinces, cities } = this.data
    
    if (column === 0) {
      // 省份变化，重新加载城市
      const province = provinces[value]
      if (province) {
        this.setData({
          regionIndex: [value, 0, 0],
          'regionData[1]': [],
          'regionData[2]': []
        })
        this.loadCities(province.code)
      }
    } else if (column === 1) {
      // 城市变化，重新加载区县
      const city = cities[value]
      if (city) {
        this.setData({
          regionIndex: [this.data.regionIndex[0], value, 0],
          'regionData[2]': []
        })
        this.loadDistricts(city.code)
      }
    }
  },

  onDetailInput(e) {
    this.setData({ detail: e.detail.value })
  },

  onDefaultChange(e) {
    this.setData({ isDefault: e.detail.value })
  },

  validateForm() {
    const { name, phone, region, detail } = this.data
    
    if (!name.trim()) {
      wx.showToast({ title: '请输入收货人姓名', icon: 'none' })
      return false
    }

    if (!phone.trim()) {
      wx.showToast({ title: '请输入手机号', icon: 'none' })
      return false
    }

    const phoneReg = /^1[3-9]\d{9}$/
    if (!phoneReg.test(phone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' })
      return false
    }

    if (!region) {
      wx.showToast({ title: '请选择所在地区', icon: 'none' })
      return false
    }

    if (!detail.trim()) {
      wx.showToast({ title: '请输入详细地址', icon: 'none' })
      return false
    }

    return true
  },

  onSave() {
    if (!this.validateForm()) {
      return
    }

    const { id, name, phone, province, city, district, detail, isDefault } = this.data
    
    const addressData = {
      receiverName: name,
      receiverPhone: phone,
      province,
      city,
      district,
      detailAddress: detail,
      isDefault: isDefault ? 1 : 0
    }

    wx.showLoading({ title: '保存中...' })

    if (id) {
      // 编辑
      addressData.id = id
      API.updateAddress(addressData).then(res => {
        wx.hideLoading()
        if (res.code === 200) {
          wx.showToast({
            title: '保存成功',
            icon: 'success',
            duration: 1500
          })
          setTimeout(() => {
            wx.navigateBack()
          }, 1500)
        } else {
          wx.showToast({
            title: res.message || '保存失败',
            icon: 'none'
          })
        }
      }).catch(() => {
        wx.hideLoading()
        wx.showToast({
          title: '保存失败',
          icon: 'none'
        })
      })
    } else {
      // 新增
      API.addAddress(addressData).then(res => {
        wx.hideLoading()
        if (res.code === 200) {
          wx.showToast({
            title: '保存成功',
            icon: 'success',
            duration: 1500
          })
          setTimeout(() => {
            wx.navigateBack()
          }, 1500)
        } else {
          wx.showToast({
            title: res.message || '保存失败',
            icon: 'none'
          })
        }
      }).catch(() => {
        wx.hideLoading()
        wx.showToast({
          title: '保存失败',
          icon: 'none'
        })
      })
    }
  }
})
