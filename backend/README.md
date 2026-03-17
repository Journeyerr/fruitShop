# 鲜果时光小程序后台系统

## 项目简介

鲜果时光小程序后台系统，提供后台管理和小程序端API接口。

## 技术栈

- **Java 11**
- **Spring Boot 2.7.18**
- **MySQL 8**
- **Redis**
- **RabbitMQ**
- **JWT**
- **Lombok**
- **MyBatis-Plus 3.5.3.1**
- **FastJSON 2.0.25**
- **Hutool 5.8.18**

## 项目结构

```
backend
├── src/main/java/com/fruitshop
│   ├── controller
│   │   ├── admin          # 后台管理Controller
│   │   │   ├── AdminBannerController.java
│   │   │   ├── AdminCategoryController.java
│   │   │   ├── AdminCouponController.java
│   │   │   ├── AdminDeliveryController.java
│   │   │   ├── AdminGoodsController.java
│   │   │   ├── AdminLoginController.java
│   │   │   ├── AdminMemberController.java
│   │   │   └── AdminOrderController.java
│   │   └── web            # 小程序端Controller
│   │       ├── WebAddressController.java
│   │       ├── WebBannerController.java
│   │       ├── WebCouponController.java
│   │       ├── WebGoodsController.java
│   │       ├── WebMemberController.java
│   │       ├── WebOrderController.java
│   │       ├── WebWalletController.java
│   │       └── WebWechatController.java
│   ├── service           # 服务层
│   ├── mapper            # 数据访问层
│   ├── entity            # 实体类
│   ├── config            # 配置类
│   ├── common            # 公共类
│   ├── exception         # 异常处理
│   ├── interceptor       # 拦截器
│   └── util              # 工具类
└── src/main/resources
    ├── application.yml   # 配置文件
    └── db
        └── init.sql      # 数据库初始化脚本
```

## 功能模块

### 后台管理系统

- **轮播图管理**：新增、编辑、删除、启用/禁用轮播图
- **商品管理**：新增、编辑、删除、上架/下架商品
- **订单管理**：查看订单列表、发货、取消订单
- **会员管理**：查看会员列表、会员信息
- **优惠券管理**：新增、编辑、删除优惠券，发放优惠券
- **配送设置**：设置配送范围、配送费等

### 小程序端系统

- **轮播图列表**：获取启用的轮播图列表
- **商品列表**：按分类查询商品、商品详情、推荐商品
- **订单列表**：创建订单、查看订单、取消订单、支付订单
- **会员信息**：获取会员信息、更新会员信息、绑定手机号
- **会员优惠券**：领取优惠券、查看我的优惠券
- **收货地址**：地址列表、新增地址、编辑地址、删除地址、设置默认地址
- **微信登录**：微信授权登录、退出登录
- **我的钱包**：查看余额、充值、提现、钱包流水

## 快速开始

### 环境要求

- JDK 11+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+
- Maven 3.6+

### 安装步骤

1. **克隆项目**

```bash
git clone <repository-url>
cd backend
```

2. **创建数据库**

```bash
# 执行数据库初始化脚本
mysql -u root -p < src/main/resources/db/init.sql
```

3. **修改配置文件**

编辑 `src/main/resources/application.yml`，修改以下配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fruit_shop?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: 
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

wechat:
  appid: your_appid
  secret: your_secret
```

4. **运行项目**

```bash
mvn clean install
mvn spring-boot:run
```

## API 接口说明

### 认证说明

除了登录接口和部分公开接口外，其他接口需要在请求头中携带 JWT Token：

```
Authorization: Bearer <token>
```

### 后台管理接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/admin/login` | POST | 管理员登录 |
| `/admin/banner/page` | GET | 分页查询轮播图 |
| `/admin/goods/page` | GET | 分页查询商品 |
| `/admin/order/page` | GET | 分页查询订单 |
| `/admin/member/page` | GET | 分页查询会员 |
| `/admin/coupon/page` | GET | 分页查询优惠券 |
| `/admin/delivery` | GET | 获取配送设置 |

### 小程序端接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/web/wechat/login` | POST | 微信登录 |
| `/web/banner/list` | GET | 获取轮播图列表 |
| `/web/goods/page` | GET | 分页查询商品 |
| `/web/order/page` | GET | 分页查询订单 |
| `/web/member/info` | GET | 获取会员信息 |
| `/web/coupon/my` | GET | 获取我的优惠券 |
| `/web/address/list` | GET | 获取地址列表 |
| `/web/wallet/info` | GET | 获取钱包信息 |

## 默认账号

### 后台管理系统
- 用户名：admin
- 密码：admin123

## 开发说明

### 代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok简化代码
- 统一使用Result类返回结果
- 异常统一由GlobalExceptionHandler处理

### 数据库设计

- 所有表使用 `t_` 前缀
- 主键使用 `id`，类型为 `bigint`
- 必须包含 `create_time`、`update_time`、`deleted` 字段
- 使用逻辑删除，`deleted` 字段：0-未删除，1-已删除

## 许可证

MIT License
