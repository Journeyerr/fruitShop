# 鲜果时光 - 水果店全栈电商系统

一个完整的水果电商全栈解决方案，包含微信小程序端、后台管理系统和后端API服务。

## 项目概述

本项目采用前后端分离架构，包含三个独立的子项目：

| 子项目 | 目录 | 技术栈 | 说明 |
|--------|------|--------|------|
| 小程序端 | mini | 微信小程序原生 | 用户端小程序，提供商品浏览、购物车、订单等功能 |
| 后台管理 | admin | Vue 3 + Element Plus | 后台管理系统，提供商品、订单、会员等管理功能 |
| 后端服务 | backend | Spring Boot 2.7 | 后端API服务，提供小程序和后台管理所需的所有接口 |

## 技术架构

```
┌─────────────────────────────────────────────────────────────┐
│                        用户端                                │
│  ┌─────────────────┐        ┌─────────────────┐            │
│  │  微信小程序(mini) │        │  后台管理(admin) │            │
│  │  原生开发         │        │  Vue3+ElementPlus│            │
│  └────────┬────────┘        └────────┬────────┘            │
└───────────┼──────────────────────────┼─────────────────────┘
            │      HTTP/HTTPS          │
            ▼                          ▼
┌─────────────────────────────────────────────────────────────┐
│                    后端服务(backend)                         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Spring Boot 2.7.18                      │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐          │   │
│  │  │Admin API │  │ Web API  │  │ 文件上传  │          │   │
│  │  └──────────┘  └──────────┘  └──────────┘          │   │
│  └─────────────────────────────────────────────────────┘   │
└───────────────────────────┬─────────────────────────────────┘
                            │
            ┌───────────────┼───────────────┐
            ▼               ▼               ▼
      ┌──────────┐   ┌──────────┐   ┌──────────┐
      │  MySQL   │   │  Redis   │   │ RabbitMQ │
      │  8.0+    │   │  6.0+    │   │  3.8+    │
      └──────────┘   └──────────┘   └──────────┘
```

---

## 一、小程序端 (mini)

一个现代化的水果店微信小程序，支持商品浏览、分类选购、购物车管理和订单管理功能。

### 项目特点

- 🎨 现代化UI设计，简洁美观
- 📱 左右分栏选购页面，支持分类联动
- 🛒 完整的购物流程体验
- 📦 订单管理系统
- 👤 个人中心功能
- 🔌 预留API接口，易于对接后端服务

### 技术栈

- 微信小程序原生开发
- JavaScript ES6+
- WXSS 样式
- 组件化开发

### 项目结构

```
mini/
├── pages/                  # 页面目录
│   ├── index/             # 首页
│   ├── goods/             # 选购页
│   ├── cart/              # 购物车
│   ├── order/             # 订单列表
│   ├── order-detail/      # 订单详情
│   ├── user/              # 个人中心
│   ├── address/           # 收货地址
│   ├── address-edit/      # 地址编辑
│   └── coupon/            # 优惠券
├── utils/                  # 工具类
│   ├── util.js            # 通用工具函数
│   └── api.js             # API接口配置
├── images/                 # 图片资源
├── app.js                  # 小程序入口
├── app.json               # 小程序配置
└── app.wxss               # 全局样式
```

### 功能模块

| 模块 | 功能描述 |
|------|----------|
| **首页** | 大尺寸轮播图展示、今日推荐商品网格布局、一键加入购物车 |
| **选购页** | 左右分栏布局、分类联动效果、购物车悬浮按钮 |
| **购物车** | 商品列表管理、数量增减、全选功能、订单提交 |
| **订单列表** | 状态筛选（全部、待付款、待配送、已完成）、订单操作 |
| **订单详情** | 订单状态展示、收货信息、商品明细、金额明细 |
| **个人中心** | 微信登录、用户信息、订单快捷入口、地址管理、优惠券、客服热线 |

### 核心特性

1. **左右分栏选购** - 左侧分类固定，右侧商品滚动，双向联动体验
2. **分类联动效果** - 滚动商品自动高亮对应分类，点击分类跳转到对应商品
3. **购物车悬浮窗** - 实时展示商品总数和总价
4. **分类精准** - 当季水果、果切拼盘、鲜榨果汁、精品礼盒四大分类

### 快速开始

1. 使用微信开发者工具导入 `mini` 目录
2. 填写 AppID（已配置：wxbd3b6b5e923b771d）或使用测试号
3. 配置图片资源（参考 `IMAGES.md` 文件）
4. 修改 `utils/api.js` 中的 `BASE_URL` 为后端服务地址
5. 开始开发调试

### API 对接

所有API接口定义在 `utils/api.js` 中：

```javascript
// 首页相关
API.getBanner()        // 获取轮播图
API.getRecommend()     // 获取推荐商品

// 商品相关
API.getCategories()    // 获取分类
API.getGoodsList()     // 获取商品列表

// 订单相关
API.createOrder()      // 创建订单
API.getOrders()        // 获取订单列表

// 用户相关
API.login()            // 登录
API.getAddresses()     // 获取地址列表
```

---

## 二、后台管理系统 (admin)

基于 Vue 3 + Element Plus 的水果商城后台管理系统，支持PC端和iPad端自适应。

### 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4 | 前端框架 |
| Vue Router | 4 | 路由管理 |
| Pinia | 2 | 状态管理 |
| Element Plus | 2.6 | UI组件库 |
| Axios | - | HTTP请求 |
| Vite | 5 | 构建工具 |
| Sass | - | CSS预处理器 |

### 项目结构

```
admin/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API接口
│   ├── layout/            # 布局组件
│   ├── router/            # 路由配置
│   ├── stores/            # Pinia状态管理
│   ├── styles/            # 全局样式
│   ├── utils/             # 工具函数
│   └── views/             # 页面组件
│       ├── dashboard/     # 首页概览
│       ├── login/         # 登录页面
│       ├── banner/        # 轮播图管理
│       ├── category/      # 分类管理
│       ├── goods/         # 商品管理
│       ├── order/         # 订单管理
│       ├── member/        # 会员管理
│       ├── coupon/        # 优惠券管理
│       ├── address/       # 收货地址管理
│       ├── delivery/      # 配送地址管理
│       └── setting/       # 系统设置
├── index.html
├── package.json
└── vite.config.js
```

### 功能模块

| 分类 | 模块 | 功能描述 |
|------|------|----------|
| **内容管理** | 轮播图管理 | 轮播图的增删改查、状态管理 |
| | 分类管理 | 商品分类的增删改查 |
| | 商品管理 | 商品的增删改查、上下架管理 |
| **交易管理** | 订单管理 | 订单列表、详情查看、发货、取消等操作 |
| | 优惠券管理 | 优惠券的增删改查、状态管理 |
| **用户中心** | 会员管理 | 会员列表、详情查看 |
| | 收货地址管理 | 用户收货地址管理 |
| | 配送地址管理 | 配送范围设置 |
| **系统设置** | 基础设置 | 商城名称、客服电话、配送范围等 |
| | 配送设置 | 自提、配送相关配置 |
| | 支付设置 | 微信支付配置 |

### 快速开始

```bash
# 进入目录
cd admin

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

访问 http://localhost:3000

### 默认账号

- 用户名：admin
- 密码：admin123

### 响应式设计

系统支持以下设备：
- PC端（> 1024px）
- iPad端（768px - 1024px）
- 移动端（< 768px）

---

## 三、后端服务 (backend)

鲜果时光小程序后台系统，提供后台管理和小程序端API接口。

### 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 11 | 编程语言 |
| Spring Boot | 2.7.18 | 后端框架 |
| MySQL | 8 | 数据库 |
| Redis | 6.0+ | 缓存 |
| RabbitMQ | 3.8+ | 消息队列 |
| JWT | - | 认证授权 |
| MyBatis-Plus | 3.5.3.1 | ORM框架 |
| Lombok | - | 代码简化 |
| FastJSON | 2.0.25 | JSON处理 |
| Hutool | 5.8.18 | 工具库 |

### 项目结构

```
backend/
├── src/main/java/com/fruitshop/
│   ├── FruitShopApplication.java    # 启动类
│   ├── controller/
│   │   ├── admin/                   # 后台管理Controller
│   │   │   ├── AdminBannerController.java
│   │   │   ├── AdminCategoryController.java
│   │   │   ├── AdminGoodsController.java
│   │   │   ├── AdminOrderController.java
│   │   │   ├── AdminMemberController.java
│   │   │   ├── AdminCouponController.java
│   │   │   └── AdminLoginController.java
│   │   └── web/                     # 小程序端Controller
│   │       ├── WebWechatController.java
│   │       ├── WebBannerController.java
│   │       ├── WebGoodsController.java
│   │       ├── WebOrderController.java
│   │       ├── WebMemberController.java
│   │       ├── WebAddressController.java
│   │       ├── WebCouponController.java
│   │       └── WebWalletController.java
│   ├── service/                     # 服务层
│   ├── mapper/                      # 数据访问层
│   ├── entity/                      # 实体类
│   ├── config/                      # 配置类
│   ├── common/                      # 公共类
│   ├── exception/                   # 异常处理
│   ├── interceptor/                 # 拦截器
│   └── util/                        # 工具类
└── src/main/resources/
    ├── application.yml              # 配置文件
    └── db/
        └── init.sql                 # 数据库初始化脚本
```

### 功能模块

#### 后台管理系统

- **轮播图管理**：新增、编辑、删除、启用/禁用轮播图
- **商品管理**：新增、编辑、删除、上架/下架商品
- **订单管理**：查看订单列表、发货、取消订单
- **会员管理**：查看会员列表、会员信息
- **优惠券管理**：新增、编辑、删除优惠券，发放优惠券
- **配送设置**：设置配送范围、配送费等

#### 小程序端系统

- **轮播图列表**：获取启用的轮播图列表
- **商品列表**：按分类查询商品、商品详情、推荐商品
- **订单列表**：创建订单、查看订单、取消订单、支付订单
- **会员信息**：获取会员信息、更新会员信息、绑定手机号
- **会员优惠券**：领取优惠券、查看我的优惠券
- **收货地址**：地址列表、新增地址、编辑地址、删除地址、设置默认地址
- **微信登录**：微信授权登录、退出登录
- **我的钱包**：查看余额、充值、提现、钱包流水

### 快速开始

#### 环境要求

- JDK 11+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+
- Maven 3.6+

#### 安装步骤

1. **创建数据库**

```bash
mysql -u root -p < src/main/resources/db/init.sql
```

2. **修改配置文件**

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fruit_shop?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

wechat:
  appid: your_appid
  secret: your_secret
```

3. **运行项目**

```bash
mvn clean install
mvn spring-boot:run
```

### API 接口

#### 认证说明

除了登录接口和部分公开接口外，其他接口需要在请求头中携带 JWT Token：

```
Authorization: Bearer <token>
```

#### 后台管理接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/admin/login` | POST | 管理员登录 |
| `/admin/banner/page` | GET | 分页查询轮播图 |
| `/admin/goods/page` | GET | 分页查询商品 |
| `/admin/order/page` | GET | 分页查询订单 |
| `/admin/member/page` | GET | 分页查询会员 |
| `/admin/coupon/page` | GET | 分页查询优惠券 |

#### 小程序端接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/web/wechat/login` | POST | 微信登录 |
| `/web/banner/list` | GET | 获取轮播图列表 |
| `/web/goods/page` | GET | 分页查询商品 |
| `/web/order/page` | GET | 分页查询订单 |
| `/web/member/info` | GET | 获取会员信息 |
| `/web/address/list` | GET | 获取地址列表 |
| `/web/coupon/my` | GET | 获取我的优惠券 |
| `/web/wallet/info` | GET | 获取钱包信息 |

### 默认账号

- 用户名：admin
- 密码：admin123

### 数据库设计规范

- 所有表使用 `t_` 前缀
- 主键使用 `id`，类型为 `bigint`
- 必须包含 `create_time`、`update_time`、`deleted` 字段
- 使用逻辑删除，`deleted` 字段：0-未删除，1-已删除

---

## 部署说明

### 开发环境

1. 启动 MySQL、Redis、RabbitMQ 服务
2. 创建 `fruit_shop` 数据库并执行初始化脚本
3. 启动后端服务（默认端口 8080）
4. 启动后台管理系统（默认端口 3000）
5. 使用微信开发者工具打开小程序项目

### 生产环境

1. **后端服务**
   ```bash
   mvn clean package -DskipTests
   java -jar target/fruitshop-backend.jar
   ```

2. **后台管理**
   ```bash
   npm run build
   # 部署 dist 目录到 Nginx 或静态服务器
   ```

3. **小程序**
   - 上传代码至微信后台
   - 提交审核发布

---

## 业务流程

```
用户端流程：
小程序首页 → 浏览商品 → 加入购物车 → 提交订单 → 支付 → 等待配送 → 确认收货

后台管理流程：
管理员登录 → 商品管理 → 订单管理 → 发货处理 → 数据统计
```

---

## 许可证

MIT License
