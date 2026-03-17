# 微信登录功能说明

## 功能概述

小程序"我的"页面已集成微信登录功能，支持用户授权登录、信息展示和退出登录。

## 使用流程

### 1. 未登录状态
- 显示默认头像
- 显示"微信登录"按钮
- 显示提示文字"登录后享受更多权益"

### 2. 登录流程
1. 用户点击"微信登录"按钮
2. 弹出微信授权弹窗
3. 用户同意授权
4. 获取用户信息（头像、昵称）
5. 保存到本地存储
6. 显示已登录状态

### 3. 已登录状态
- 显示用户头像和昵称
- 显示手机号（如已绑定）
- 显示"退出"按钮

### 4. 退出登录
1. 点击"退出"按钮
2. 确认退出
3. 清除本地存储的用户信息
4. 返回未登录状态

## 技术实现

### 核心API

```javascript
// 获取用户信息
wx.getUserProfile({
  desc: '用于完善用户资料',
  success: (res) => {
    console.log(res.userInfo)
  }
})

// 本地存储
wx.setStorageSync('userInfo', userInfo)
wx.getStorageSync('userInfo')
wx.removeStorageSync('userInfo')
```

### 数据存储

用户信息存储在：
- 本地存储：`wx.setStorageSync('userInfo', userInfo)`
- 全局变量：`app.globalData.userInfo`

### 后端对接

预留了后端登录接口，需要时取消注释：

```javascript
// 登录到服务器
loginToServer(userInfo) {
  wx.login({
    success: (loginRes) => {
      API.login({
        code: loginRes.code,
        userInfo: userInfo
      }).then(res => {
        wx.setStorageSync('token', res.token)
        app.globalData.userInfo = res.userInfo
      })
    }
  })
}
```

## 权限说明

### 需要的权限

在 `app.json` 中配置：

```json
{
  "permission": {
    "scope.userInfo": {
      "desc": "用于展示用户头像和昵称"
    }
  }
}
```

## 注意事项

1. **隐私合规**
   - 使用 `wx.getUserProfile` 需要用户主动触发
   - 授权弹窗会显示 `desc` 描述文字
   - 符合微信小程序隐私规范

2. **登录状态**
   - 本地存储的用户信息不会自动过期
   - 可根据业务需求添加 token 过期机制
   - 建议在 `app.js` 中统一管理登录状态

3. **功能限制**
   - 未登录用户访问需要登录的功能时会提示
   - 已在菜单点击事件中添加登录检查

4. **数据安全**
   - 敏感信息应使用 `wx.login` 获取 code
   - 后端通过 code 换取 openid 和 session_key
   - 不要在前端直接处理敏感信息

## 扩展功能

### 手机号绑定

如需绑定手机号，可使用：

```javascript
<button open-type="getPhoneNumber" bindgetphonenumber="getPhoneNumber">
  绑定手机号
</button>
```

### 实名认证

如需实名认证，需要：
1. 后端配置相关接口
2. 使用微信实名认证能力
3. 前端调用认证流程

## 测试建议

1. 测试登录流程是否正常
2. 测试退出登录功能
3. 测试登录状态持久化
4. 测试未登录访问限制功能
5. 测试用户信息更新

## 常见问题

**Q: 登录后刷新页面还需要重新登录吗？**
A: 不需要。用户信息存储在本地，刷新后会自动读取。

**Q: 如何获取用户的 openid？**
A: 需要在后端通过 `wx.login` 获取的 code 换取 openid。

**Q: 用户拒绝授权怎么办？**
A: 用户拒绝授权后，可以引导用户重新点击登录按钮，或在小程序设置中重新授权。

**Q: 如何实现强制登录？**
A: 可以在 `app.js` 的 `onLaunch` 中检查登录状态，未登录时跳转到登录页面。
