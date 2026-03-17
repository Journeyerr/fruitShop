# 水果商城后台管理系统

基于 Vue 3 + Element Plus 的水果商城后台管理系统，支持PC端和iPad端自适应。

## 技术栈

- Vue 3.4
- Vue Router 4
- Pinia 2
- Element Plus 2.6
- Axios
- Vite 5
- Sass

## 功能模块

### 内容管理
- **轮播图管理**：轮播图的增删改查、状态管理
- **分类管理**：商品分类的增删改查
- **商品管理**：商品的增删改查、上下架管理

### 交易管理
- **订单管理**：订单列表、详情查看、发货、取消等操作
- **优惠券管理**：优惠券的增删改查、状态管理

### 用户中心
- **会员管理**：会员列表、详情查看
- **收货地址管理**：用户收货地址管理
- **配送地址管理**：配送范围设置

### 系统设置
- **基础设置**：商城名称、客服电话、配送范围等
- **配送设置**：自提、配送相关配置
- **支付设置**：微信支付配置
- **其他设置**：用户协议、隐私政策等

## 项目结构

```
admin/
├── public/                 # 静态资源
│   └── favicon.svg
├── src/
│   ├── api/               # API接口
│   │   └── index.js
│   ├── layout/            # 布局组件
│   │   └── index.vue
│   ├── router/            # 路由配置
│   │   └── index.js
│   ├── stores/            # Pinia状态管理
│   │   └── user.js
│   ├── styles/            # 全局样式
│   │   └── index.scss
│   ├── utils/             # 工具函数
│   │   └── request.js
│   ├── views/             # 页面组件
│   │   ├── banner/        # 轮播图管理
│   │   ├── category/      # 分类管理
│   │   ├── coupon/        # 优惠券管理
│   │   ├── dashboard/     # 首页概览
│   │   ├── delivery/      # 配送地址管理
│   │   ├── goods/         # 商品管理
│   │   ├── address/       # 收货地址管理
│   │   ├── login/         # 登录页面
│   │   ├── member/        # 会员管理
│   │   ├── order/         # 订单管理
│   │   └── setting/       # 系统设置
│   ├── App.vue
│   └── main.js
├── index.html
├── package.json
├── vite.config.js
└── README.md
```

## 快速开始

### 安装依赖

```bash
cd admin
npm install
```

### 开发模式

```bash
npm run dev
```

访问 http://localhost:3000

### 生产构建

```bash
npm run build
```

## 默认账号

- 用户名：admin
- 密码：admin123

## 响应式设计

系统支持以下设备：
- PC端（> 1024px）
- iPad端（768px - 1024px）
- 移动端（< 768px）

## API代理配置

开发环境下，API请求会代理到后端服务：

```javascript
// vite.config.js
proxy: {
  '/api': {
    target: 'http://localhost',
    changeOrigin: true
  }
}
```

## 后端接口

后台管理系统需要配合后端API使用，后端项目位于 `../backend` 目录。

## License

MIT
