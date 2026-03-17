# 鲜果时光 - 水果店微信点单小程序

一个现代化的水果店微信小程序，支持商品浏览、分类选购、购物车管理和订单管理功能。

## 项目特点

- 🎨 现代化UI设计，简洁美观
- 📱 左右分栏选购页面，支持分类联动
- 🛒 完整的购物流程体验
- 📦 订单管理系统
- 👤 个人中心功能
- 🔌 预留API接口，易于对接后端服务

## 功能模块

### 1. 首页
- 大尺寸轮播图展示
- 今日推荐商品网格布局
- 一键加入购物车

### 2. 选购页
- 左右分栏布局（左侧分类、右侧商品）
- 分类联动效果（滚动商品自动切换分类）
- 点击分类自动滚动到对应商品
- 一键加入购物车
- 购物车悬浮按钮（实时显示商品总数和总价）

### 3. 购物车
- 商品列表管理
- 数量增减
- 商品删除
- 全选功能
- 订单提交

### 4. 订单列表
- 订单状态筛选（全部、待付款、待配送、已完成）
- 订单摘要展示（商品数量、总金额、下单时间）
- 订单操作（取消、支付、再来一单）

### 5. 订单详情
- 订单状态展示
- 收货信息
- 商品明细
- 订单信息
- 金额明细

### 6. 个人中心
- 微信登录功能（支持授权登录）
- 用户信息展示（头像、昵称）
- 登录状态管理（本地存储）
- 退出登录功能
- 订单快捷入口
- 配送服务信息
- 功能菜单（收货地址、优惠券、收藏、反馈、关于）
- 客服热线

## 技术栈

- 微信小程序原生开发
- JavaScript ES6+
- WXSS 样式
- 组件化开发

## 项目结构

```
mini/
├── pages/                  # 页面目录
│   ├── index/             # 首页
│   ├── goods/             # 选购页
│   ├── cart/              # 购物车
│   ├── order/             # 订单列表
│   ├── order-detail/      # 订单详情
│   └── user/              # 个人中心
├── utils/                  # 工具类
│   ├── util.js            # 通用工具函数
│   └── api.js             # API接口配置
├── images/                 # 图片资源
├── app.js                  # 小程序入口
├── app.json               # 小程序配置
├── app.wxss               # 全局样式
├── sitemap.json           # 站点地图
├── START.txt              # 需求文档
├── IMAGES.md              # 图片资源说明
└── README.md              # 项目说明
```

## 🎯 核心特性

1. **左右分栏选购** - 左侧分类固定，右侧商品滚动，双向联动体验
2. **分类联动效果** - 滚动商品自动高亮对应分类，点击分类跳转到对应商品
3. **购物车悬浮窗** - 在选购页面以圆形悬浮按钮显示，实时展示商品总数和总价
4. **订单列表优化** - 只展示商品数量、总金额、支付时间，点击查看详情
5. **分类精准** - 当季水果、果切拼盘、鲜榨果汁、精品礼盒四大分类
6. **配送信息优化** - 个人中心展示配送范围、时间、费用说明
7. **API预留** - 所有接口已定义，便于后续对接真实后端

## 快速开始

### 1. 克隆项目
```bash
git clone [项目地址]
```

### 2. 导入微信开发者工具
1. 打开微信开发者工具
2. 选择"导入项目"
3. 选择项目根目录
4. 填写 AppID（已配置：wxbd3b6b5e923b771d）

### 3. 配置图片资源
参考 `IMAGES.md` 文件，准备相应的图片资源

### 4. 开始开发
- 所有API接口已预留，位于 `utils/api.js`
- 当前使用静态数据，可替换为真实API调用
- 购物车数据存储在全局和本地存储中

## API 对接指南

所有API接口定义在 `utils/api.js` 中，包括：

```javascript
// 首页相关
API.getBanner()        // 获取轮播图
API.getRecommend()     // 获取推荐商品

// 商品相关
API.getCategories()    // 获取分类
API.getGoodsList()     // 获取商品列表
API.getGoodsDetail()   // 获取商品详情

// 购物车相关
API.addToCart()        // 添加到购物车
API.getCart()          // 获取购物车
API.updateCart()       // 更新购物车
API.clearCart()        // 清空购物车

// 订单相关
API.createOrder()      // 创建订单
API.getOrders()        // 获取订单列表
API.getOrderDetail()   // 获取订单详情
API.cancelOrder()      // 取消订单

// 用户相关
API.login()            // 登录
API.getUserInfo()      // 获取用户信息
API.updateUserInfo()   // 更新用户信息
API.getAddresses()     // 获取地址列表
```

使用示例：
```javascript
const { API } = require('../../utils/api')

// 获取商品列表
API.getGoodsList(categoryId).then(res => {
  this.setData({ goodsList: res })
})
```

## 数据说明

### 商品数据结构
```javascript
{
  id: Number,           // 商品ID
  categoryId: Number,   // 分类ID
  name: String,         // 商品名称
  price: Number,        // 价格
  image: String,        // 图片URL
  unit: String,         // 单位
  stock: Number,        // 库存
  sales: Number,        // 销量
  desc: String          // 描述
}
```

### 订单数据结构
```javascript
{
  id: String,           // 订单ID
  status: String,       // 状态：pending/paid/completed
  statusText: String,   // 状态文本
  goodsCount: Number,   // 商品总数
  totalPrice: Number,   // 总金额
  createTime: String,   // 创建时间
  items: Array          // 商品列表
}
```

## 注意事项

1. **AppID配置**：项目已配置AppID，开发时请使用测试号或替换为自己的AppID
2. **图片资源**：首次运行需要准备图片资源，或使用占位图
3. **支付功能**：支付功能需要开通微信支付并配置商户信息
4. **数据持久化**：购物车数据使用本地存储，刷新页面不会丢失

## 优化建议

- [ ] 添加商品搜索功能
- [ ] 实现商品收藏功能
- [ ] 添加优惠券系统
- [ ] 实现地址管理
- [ ] 添加订单评价功能
- [ ] 实现消息推送
- [ ] 添加用户积分系统

## 版本历史

### v1.0.0 (2024-01-01)
- 初始版本发布
- 完成核心功能开发
- 预留API接口

## 技术支持

如有问题或建议，请联系开发团队。

## 许可证

MIT License
