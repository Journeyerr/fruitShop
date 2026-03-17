-- 创建数据库
CREATE DATABASE IF NOT EXISTS fruit_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fruit_shop;

/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012 (8.0.12)
 Source Host           : localhost:3306
 Source Schema         : fruit_shop

 Target Server Type    : MySQL
 Target Server Version : 80012 (8.0.12)
 File Encoding         : 65001

 Date: 16/03/2026 10:32:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_address
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区县',
  `detail_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `is_default` int(11) NULL DEFAULT 0 COMMENT '是否默认 0-否 1-是',
  `longitude` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '纬度',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收货地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_address
-- ----------------------------
INSERT INTO `t_address` VALUES (1, 1, '张三', '15012341234', '广东省', '广州市', '海珠区', '海珠区人民政府', 1, NULL, NULL, '2026-03-12 16:40:45', '2026-03-12 16:40:44', 0);
INSERT INTO `t_address` VALUES (2, 1, '李四', '15012341234', '广东省', '广州市', '越秀区', '越秀区人民政府', 0, NULL, NULL, '2026-03-12 18:02:52', '2026-03-12 18:02:52', 0);

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES (1, 'admin', '$2a$10$QIqrhae.xSsSY5jTChhKD.M.L2YWbI4Uf7d368lO4pphbFMrf5HrK', '超级管理员', NULL, '13800138000', 'admin@fruitshop.com', 1, '2026-03-13 17:04:23', '2026-03-13 11:46:04', '2026-03-13 17:04:23', 0);
INSERT INTO `t_admin` VALUES (2, 'operator', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '运营管理员', NULL, '13800138001', 'operator@fruitshop.com', 1, NULL, '2026-03-13 11:46:04', '2026-03-13 11:46:04', 0);

-- ----------------------------
-- Table structure for t_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '轮播图标题',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片URL',
  `link_type` int(11) NULL DEFAULT NULL COMMENT '跳转类型 1-商品详情 2-分类 3-外链',
  `link_param` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转参数',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '轮播图' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_banner
-- ----------------------------
INSERT INTO `t_banner` VALUES (1, '新鲜水果上市', 'https://oss.echophp.top/fruit/banner1.jpg', 1, '1', 1, 0, '2026-03-11 16:48:26', '2026-03-13 15:44:03', 0);
INSERT INTO `t_banner` VALUES (2, '限时特惠', 'https://oss.echophp.top/fruit/banner2.png', 1, '2', 2, 0, '2026-03-11 16:48:26', '2026-03-13 15:44:03', 0);
INSERT INTO `t_banner` VALUES (3, '会员专享', 'https://oss.echophp.top/fruit/banner3.png', 3, 'https://www.echophp.top', 3, 0, '2026-03-11 16:48:26', '2026-03-13 15:44:03', 0);
INSERT INTO `t_banner` VALUES (4, '测试', 'https://oss.echophp.top/fruit/upload/2026/03/13/fc92bdf462864faa88cba79d308f914e.png', 1, '', 4, 1, '2026-03-13 16:48:30', '2026-03-13 16:48:30', 0);
INSERT INTO `t_banner` VALUES (5, '1111', 'https://oss.echophp.top/fruit/upload/2026/03/13/225bd4e03a184740a144a7d224e70709.png', 1, '11', 6, 1, '2026-03-13 16:49:25', '2026-03-13 16:49:50', 1);
INSERT INTO `t_banner` VALUES (6, '313图', 'https://oss.echophp.top/fruit/upload/2026/03/13/3015a5873c4943fba61d2f772f3bf82e.png', 2, '', 0, 0, '2026-03-13 17:08:40', '2026-03-13 17:08:40', 0);

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES (1, '时令水果', 'https://oss.echophp.top/fruit/category/category1.png', 1, 1, '2026-03-11 16:48:26', '2026-03-13 15:44:14', 0);
INSERT INTO `t_category` VALUES (2, '进口水果', 'https://oss.echophp.top/fruit/category/category2.png', 2, 1, '2026-03-11 16:48:26', '2026-03-13 15:44:14', 0);
INSERT INTO `t_category` VALUES (3, '热带水果', 'https://oss.echophp.top/fruit/category/category3.png', 3, 1, '2026-03-11 16:48:26', '2026-03-13 15:44:14', 0);
INSERT INTO `t_category` VALUES (4, '果切拼盘', 'https://oss.echophp.top/fruit/category/category4.png', 4, 1, '2026-03-11 16:48:26', '2026-03-13 15:44:14', 0);
INSERT INTO `t_category` VALUES (5, '果汁饮品', 'https://oss.echophp.top/fruit/category/category5.png', 5, 1, '2026-03-11 16:48:26', '2026-03-13 15:44:14', 0);
INSERT INTO `t_category` VALUES (6, '我的分类', 'https://oss.echophp.top/fruit/category/category6.png', 6, 0, '2026-03-11 16:48:26', '2026-03-13 16:54:22', 1);
INSERT INTO `t_category` VALUES (7, '柠檬', 'https://oss.echophp.top/fruit/upload/2026/03/13/aedbfc9dab0b4bdcbb085e72b768a4b5.png', 7, 1, '2026-03-13 16:53:53', '2026-03-13 16:53:53', 0);

-- ----------------------------
-- Table structure for t_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon`;
CREATE TABLE `t_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券名称',
  `type` int(11) NOT NULL COMMENT '优惠券类型 1-满减券 2-折扣券 3-无门槛券',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `discount` decimal(5, 2) NULL DEFAULT NULL COMMENT '折扣比例',
  `min_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '最低消费金额',
  `total_count` int(11) NOT NULL COMMENT '发放总量',
  `received_count` int(11) NULL DEFAULT 0 COMMENT '已领取数量',
  `limit_count` int(11) NULL DEFAULT 1 COMMENT '每人限领数量',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `valid_days` int(11) NULL DEFAULT NULL COMMENT '有效天数',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_coupon
-- ----------------------------
INSERT INTO `t_coupon` VALUES (1, '新人专享券', 1, 10.00, NULL, 50.00, 1000, 0, 1, '2026-03-11 16:48:27', '2026-04-10 16:48:27', NULL, 0, '2026-03-11 16:48:27', '2026-03-13 17:31:53', 1);
INSERT INTO `t_coupon` VALUES (2, '满减优惠券', 1, 20.00, NULL, 100.00, 500, 0, 2, '2026-03-11 16:48:27', '2026-03-26 16:48:27', NULL, 1, '2026-03-11 16:48:27', '2026-03-13 15:10:39', 0);
INSERT INTO `t_coupon` VALUES (3, '无门槛优惠券', 3, 5.00, NULL, NULL, 300, 0, 1, '2026-03-11 16:48:27', '2026-03-18 16:48:27', NULL, 1, '2026-03-11 16:48:27', '2026-03-13 15:10:41', 0);

-- ----------------------------
-- Table structure for t_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品图片列表，逗号分隔',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品简介',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `price` decimal(10, 2) NOT NULL COMMENT '现价',
  `stock` int(11) NULL DEFAULT 0 COMMENT '库存',
  `sales` int(11) NULL DEFAULT 0 COMMENT '销量',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位',
  `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 0-下架 1-上架',
  `recommend` int(11) NULL DEFAULT 0 COMMENT '是否推荐 0-否 1-是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_goods
-- ----------------------------
INSERT INTO `t_goods` VALUES (1, 1, '新疆阿克苏苹果', 'https://oss.echophp.top/fruit/apple.png', NULL, '正宗新疆阿克苏苹果，香甜可口', '', NULL, 12.90, 0, 156, '斤', '5斤装', 1, 1, 1, '2026-03-11 16:48:27', '2026-03-13 15:44:27', 0);
INSERT INTO `t_goods` VALUES (2, 1, '海南香蕉', 'https://oss.echophp.top/fruit/banana.png', NULL, '新鲜海南香蕉，口感香甜', NULL, NULL, 5.90, 200, 89, '斤', '2斤装', 2, 1, 1, '2026-03-11 16:48:27', '2026-03-13 15:44:27', 0);
INSERT INTO `t_goods` VALUES (3, 2, '智利车厘子', 'https://oss.echophp.top/fruit/cherry.png', NULL, '进口智利车厘子，个大味甜', NULL, NULL, 89.90, 50, 45, '盒', '500g/盒', 1, 1, 1, '2026-03-11 16:48:27', '2026-03-13 15:44:27', 0);
INSERT INTO `t_goods` VALUES (4, 2, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', NULL, '金枕头榴莲，果肉饱满', NULL, NULL, 168.00, 20, 23, '个', '3-4斤/个', 2, 1, 1, '2026-03-11 16:48:27', '2026-03-13 15:44:27', 0);
INSERT INTO `t_goods` VALUES (5, 3, '海南芒果', 'https://oss.echophp.top/fruit/mango.png', NULL, '树上熟芒果，香甜多汁', NULL, NULL, 15.90, 150, 67, '斤', '2斤装', 1, 1, 1, '2026-03-11 16:48:27', '2026-03-13 15:44:27', 0);
INSERT INTO `t_goods` VALUES (6, 3, '泰国山竹', 'https://oss.echophp.top/fruit/mangosteen.png', NULL, '新鲜山竹，果肉雪白', NULL, NULL, 29.90, 80, 34, '斤', '1斤装', 2, 1, 0, '2026-03-11 16:48:27', '2026-03-13 15:44:27', 0);
INSERT INTO `t_goods` VALUES (7, 4, '水果拼盘', 'https://oss.echophp.top/fruit/fruit-platter.png', NULL, '精选水果拼盘，现切现做', NULL, NULL, 39.90, 30, 78, '份', '500g/份', 1, 1, 1, '2026-03-11 16:48:27', '2026-03-13 15:44:27', 0);
INSERT INTO `t_goods` VALUES (8, 5, '鲜榨橙汁', 'https://oss.echophp.top/fruit/orange-juice.png', NULL, '新鲜橙子现榨，不加水', NULL, NULL, 18.00, 100, 156, '杯', '500ml/杯', 1, 1, 0, '2026-03-11 16:48:27', '2026-03-13 15:44:27', 0);
INSERT INTO `t_goods` VALUES (9, 7, '新疆木瓜', 'https://oss.echophp.top/fruit/upload/2026/03/13/174f2b45caf04e3eac00f6944d625b94.png', NULL, '新疆木瓜', '新疆木瓜', 12.00, 11.00, 100, 0, '斤', '500g', 0, 1, 0, '2026-03-13 16:57:48', '2026-03-13 16:57:48', 0);

-- ----------------------------
-- Table structure for t_member
-- ----------------------------
DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `openid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信openid',
  `unionid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信unionid',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `gender` int(11) NULL DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
  `balance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `points` int(11) NULL DEFAULT 0 COMMENT '积分',
  `level` int(11) NULL DEFAULT 1 COMMENT '会员等级',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_openid`(`openid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_member
-- ----------------------------
INSERT INTO `t_member` VALUES (1, 'olQy45Q6UoLyAWBYVeRcRZz-YleE', NULL, '张三', 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132', NULL, 0, 0.00, 0, 1, '2026-03-13 17:24:33', '2026-03-12 10:48:52', '2026-03-12 14:17:30', 0);

-- ----------------------------
-- Table structure for t_member_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_member_coupon`;
CREATE TABLE `t_member_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `coupon_id` bigint(20) NOT NULL COMMENT '优惠券ID',
  `coupon_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `coupon_type` int(11) NULL DEFAULT NULL COMMENT '优惠券类型',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `discount` decimal(5, 2) NULL DEFAULT NULL COMMENT '折扣比例',
  `min_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '最低消费金额',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态 0-未使用 1-已使用 2-已过期',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '领取时间',
  `use_time` datetime NULL DEFAULT NULL COMMENT '使用时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员优惠券' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_member_coupon
-- ----------------------------
INSERT INTO `t_member_coupon` VALUES (1, 1, 1, '新人专享券1', 1, 10.00, 1.00, 1.00, 0, '2026-03-13 17:31:19', NULL, '2026-03-11 17:31:24', '2026-03-20 17:31:29', '2026-03-13 17:31:36', '2026-03-13 17:33:19', 0);

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `address_id` bigint(20) NULL DEFAULT NULL COMMENT '收货地址ID',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货地址',
  `goods_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '商品总金额',
  `delivery_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '配送费',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠金额',
  `pay_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实付金额',
  `status` int(11) NULL DEFAULT 0 COMMENT '订单状态 0-待付款 1-待配送 2-待收货 3-已完成 4-已取消',
  `pay_type` int(11) NULL DEFAULT NULL COMMENT '支付方式 1-微信支付',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '配送时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `delivery_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'delivery' COMMENT '配送方式 pickup-自提 delivery-配送',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (1, '17733081476216606548E', 1, 1, '张三', '15012341234', '广东省广州市海珠区海珠区人民政府', 168.00, 0.00, 0.00, 168.00, 1, NULL, '2026-03-12 17:35:53', NULL, NULL, '', 'delivery', NULL, '2026-03-12 17:35:48', '2026-03-12 17:35:53', 0);
INSERT INTO `t_order` VALUES (2, '17733082288645CB320BC', 1, NULL, NULL, NULL, NULL, 186.80, 0.00, 0.00, 186.80, 2, NULL, '2026-03-12 17:40:05', NULL, NULL, '帮忙选个好的', 'delivery', NULL, '2026-03-12 17:37:09', '2026-03-12 17:40:41', 0);
INSERT INTO `t_order` VALUES (3, '1773308640785112D94D9', 1, NULL, NULL, NULL, NULL, 168.00, 0.00, 0.00, 168.00, 1, NULL, '2026-03-12 17:45:26', NULL, NULL, '', 'delivery', NULL, '2026-03-12 17:44:01', '2026-03-12 17:45:26', 0);
INSERT INTO `t_order` VALUES (4, '177330878743390174AF0', 1, NULL, NULL, NULL, NULL, 168.00, 0.00, 0.00, 168.00, 1, NULL, '2026-03-12 17:46:35', NULL, NULL, '', 'delivery', NULL, '2026-03-12 17:46:27', '2026-03-12 17:46:35', 0);
INSERT INTO `t_order` VALUES (5, '1773308807730631EF63E', 1, NULL, NULL, NULL, NULL, 168.00, 0.00, 0.00, 168.00, 2, NULL, '2026-03-12 17:46:54', '2026-03-13 14:56:46', NULL, '', 'delivery', NULL, '2026-03-12 17:46:48', '2026-03-13 14:56:46', 0);
INSERT INTO `t_order` VALUES (6, '177330887648587243A5E', 1, NULL, NULL, NULL, NULL, 168.00, 0.00, 0.00, 168.00, 3, NULL, '2026-03-12 17:53:39', NULL, NULL, '', 'delivery', NULL, '2026-03-12 17:47:56', '2026-03-12 17:53:39', 0);
INSERT INTO `t_order` VALUES (7, '1773309087473A47F0E12', 1, NULL, NULL, NULL, NULL, 168.00, 0.00, 0.00, 168.00, 3, NULL, '2026-03-12 17:51:41', NULL, NULL, '', 'pickup', NULL, '2026-03-12 17:51:27', '2026-03-12 17:51:41', 0);
INSERT INTO `t_order` VALUES (8, '202603121753031242', 1, NULL, NULL, NULL, NULL, 12.90, 0.00, 0.00, 12.90, 3, NULL, '2026-03-12 17:53:13', NULL, NULL, '', 'pickup', NULL, '2026-03-12 17:53:04', '2026-03-12 17:53:13', 0);
INSERT INTO `t_order` VALUES (9, '202603121758597548', 1, NULL, NULL, NULL, NULL, 89.90, 0.00, 0.00, 89.90, 3, NULL, '2026-03-12 18:11:02', NULL, NULL, '', 'pickup', NULL, '2026-03-12 17:58:59', '2026-03-12 18:11:02', 0);
INSERT INTO `t_order` VALUES (10, '202603121810328233', 1, 1, '张三', '15012341234', '广东省广州市海珠区海珠区人民政府', 5.90, 0.00, 0.00, 5.90, 3, NULL, '2026-03-12 18:10:41', NULL, NULL, '', 'delivery', NULL, '2026-03-12 18:10:33', '2026-03-12 18:10:41', 0);
INSERT INTO `t_order` VALUES (11, '202603131443387331', 1, 2, '李四', '15012341234', '广东省广州市越秀区越秀区人民政府', 18.00, 0.00, 0.00, 18.00, 2, NULL, '2026-03-13 14:43:52', '2026-03-13 14:56:40', NULL, '', 'delivery', NULL, '2026-03-13 14:43:39', '2026-03-13 14:56:40', 0);
INSERT INTO `t_order` VALUES (12, '202603131453579699', 1, NULL, NULL, NULL, NULL, 39.90, 0.00, 0.00, 39.90, 2, NULL, '2026-03-13 14:54:09', NULL, NULL, '', 'pickup', NULL, '2026-03-13 14:53:57', '2026-03-13 14:54:09', 0);
INSERT INTO `t_order` VALUES (13, '202603131659476230', 1, 2, '李四', '15012341234', '广东省广州市越秀区越秀区人民政府', 126.70, 0.00, 0.00, 126.70, 3, NULL, '2026-03-13 16:59:57', NULL, NULL, '', 'delivery', NULL, '2026-03-13 16:59:48', '2026-03-13 16:59:57', 0);
INSERT INTO `t_order` VALUES (14, '202603131701447635', 1, 2, '李四', '15012341234', '广东省广州市越秀区越秀区人民政府', 119.80, 0.00, 0.00, 119.80, 3, NULL, '2026-03-13 17:01:50', NULL, NULL, '', 'delivery', NULL, '2026-03-13 17:01:44', '2026-03-13 17:01:50', 0);
INSERT INTO `t_order` VALUES (15, '202603131712302215', 1, NULL, NULL, NULL, NULL, 257.90, 0.00, 0.00, 257.90, 2, NULL, '2026-03-13 17:12:35', NULL, NULL, '', 'pickup', NULL, '2026-03-13 17:12:31', '2026-03-13 17:12:35', 0);
INSERT INTO `t_order` VALUES (16, '202603131718328424', 1, NULL, NULL, NULL, NULL, 173.90, 0.00, 0.00, 173.90, 2, NULL, '2026-03-13 17:18:43', NULL, NULL, '', 'pickup', NULL, '2026-03-13 17:18:33', '2026-03-13 17:18:43', 0);
INSERT INTO `t_order` VALUES (17, '202603131748595462', 1, NULL, NULL, NULL, NULL, 68.90, 0.00, 0.00, 68.90, 2, NULL, '2026-03-13 17:49:08', NULL, NULL, '', 'pickup', NULL, '2026-03-13 17:49:00', '2026-03-13 17:49:08', 0);

-- ----------------------------
-- Table structure for t_order_item
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `goods_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `goods_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `quantity` int(11) NOT NULL COMMENT '购买数量',
  `amount` decimal(10, 2) NOT NULL COMMENT '小计金额',
  `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品规格',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_goods_id`(`goods_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单商品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order_item
-- ----------------------------
INSERT INTO `t_order_item` VALUES (1, 2, 8, '鲜榨橙汁', 'https://oss.echophp.top/fruit/orange-juice.png', 18.00, 1, 18.00, '杯', '2026-03-12 17:02:44', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (2, 3, 3, '智利车厘子', 'https://oss.echophp.top/fruit/cherry.png', 89.90, 1, 89.90, '盒', '2026-03-12 17:13:25', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (3, 4, 2, '海南香蕉', 'https://oss.echophp.top/fruit/banana.png', 5.90, 1, 5.90, '斤', '2026-03-12 17:13:58', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (4, 1, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-12 17:35:48', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (5, 2, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-12 17:37:09', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (6, 2, 2, '海南香蕉', 'https://oss.echophp.top/fruit/banana.png', 5.90, 1, 5.90, '斤', '2026-03-12 17:37:09', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (7, 2, 1, '新疆阿克苏苹果', 'https://oss.echophp.top/fruit/apple.png', 12.90, 1, 12.90, '斤', '2026-03-12 17:37:09', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (8, 3, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-12 17:44:01', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (9, 4, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-12 17:46:27', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (10, 5, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-12 17:46:48', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (11, 6, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-12 17:47:56', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (12, 7, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-12 17:51:28', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (13, 8, 1, '新疆阿克苏苹果', 'https://oss.echophp.top/fruit/apple.png', 12.90, 1, 12.90, '斤', '2026-03-12 17:53:04', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (14, 9, 3, '智利车厘子', 'https://oss.echophp.top/fruit/cherry.png', 89.90, 1, 89.90, '盒', '2026-03-12 17:58:59', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (15, 10, 2, '海南香蕉', 'https://oss.echophp.top/fruit/banana.png', 5.90, 1, 5.90, '斤', '2026-03-12 18:10:33', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (16, 11, 8, '鲜榨橙汁', 'https://oss.echophp.top/fruit/orange-juice.png', 18.00, 1, 18.00, '杯', '2026-03-13 14:43:39', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (17, 12, 7, '水果拼盘', 'https://oss.echophp.top/fruit/fruit-platter.png', 39.90, 1, 39.90, '份', '2026-03-13 14:53:57', '2026-03-13 15:45:20', 0);
INSERT INTO `t_order_item` VALUES (18, 13, 1, '新疆阿克苏苹果', 'https://oss.echophp.top/fruit/apple.png', 12.90, 2, 25.80, '斤', '2026-03-13 16:59:48', '2026-03-13 16:59:48', 0);
INSERT INTO `t_order_item` VALUES (19, 13, 9, '新疆木瓜', 'https://oss.echophp.top/fruit/upload/2026/03/13/174f2b45caf04e3eac00f6944d625b94.png', 11.00, 1, 11.00, '斤', '2026-03-13 16:59:48', '2026-03-13 16:59:48', 0);
INSERT INTO `t_order_item` VALUES (20, 13, 3, '智利车厘子', 'https://oss.echophp.top/fruit/cherry.png', 89.90, 1, 89.90, '盒', '2026-03-13 16:59:48', '2026-03-13 16:59:48', 0);
INSERT INTO `t_order_item` VALUES (21, 14, 3, '智利车厘子', 'https://oss.echophp.top/fruit/cherry.png', 89.90, 1, 89.90, '盒', '2026-03-13 17:01:44', '2026-03-13 17:01:44', 0);
INSERT INTO `t_order_item` VALUES (22, 14, 6, '泰国山竹', 'https://oss.echophp.top/fruit/mangosteen.png', 29.90, 1, 29.90, '斤', '2026-03-13 17:01:44', '2026-03-13 17:01:44', 0);
INSERT INTO `t_order_item` VALUES (23, 15, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-13 17:12:31', '2026-03-13 17:12:31', 0);
INSERT INTO `t_order_item` VALUES (24, 15, 3, '智利车厘子', 'https://oss.echophp.top/fruit/cherry.png', 89.90, 1, 89.90, '盒', '2026-03-13 17:12:31', '2026-03-13 17:12:31', 0);
INSERT INTO `t_order_item` VALUES (25, 16, 4, '泰国榴莲', 'https://oss.echophp.top/fruit/durian.png', 168.00, 1, 168.00, '个', '2026-03-13 17:18:33', '2026-03-13 17:18:33', 0);
INSERT INTO `t_order_item` VALUES (26, 16, 2, '海南香蕉', 'https://oss.echophp.top/fruit/banana.png', 5.90, 1, 5.90, '斤', '2026-03-13 17:18:33', '2026-03-13 17:18:33', 0);
INSERT INTO `t_order_item` VALUES (27, 17, 9, '新疆木瓜', 'https://oss.echophp.top/fruit/upload/2026/03/13/174f2b45caf04e3eac00f6944d625b94.png', 11.00, 1, 11.00, '斤', '2026-03-13 17:49:00', '2026-03-13 17:49:00', 0);
INSERT INTO `t_order_item` VALUES (28, 17, 8, '鲜榨橙汁', 'https://oss.echophp.top/fruit/orange-juice.png', 18.00, 1, 18.00, '杯', '2026-03-13 17:49:00', '2026-03-13 17:49:00', 0);
INSERT INTO `t_order_item` VALUES (29, 17, 7, '水果拼盘', 'https://oss.echophp.top/fruit/fruit-platter.png', 39.90, 1, 39.90, '份', '2026-03-13 17:49:00', '2026-03-13 17:49:00', 0);

-- ----------------------------
-- Table structure for t_region
-- ----------------------------
DROP TABLE IF EXISTS `t_region`;
CREATE TABLE `t_region`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行政区划代码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `parent_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父级代码（省级为0，市级为省级代码，区级为市级代码）',
  `level` int(11) NOT NULL COMMENT '层级（1省，2市，3区）',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE,
  INDEX `idx_parent_code`(`parent_code` ASC) USING BTREE,
  INDEX `idx_level`(`level` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '省市区' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_region
-- ----------------------------
INSERT INTO `t_region` VALUES (1, '110000', '北京市', '0', 1, 1, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (2, '120000', '天津市', '0', 1, 2, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (3, '130000', '河北省', '0', 1, 3, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (4, '140000', '山西省', '0', 1, 4, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (5, '150000', '内蒙古自治区', '0', 1, 5, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (6, '310000', '上海市', '0', 1, 6, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (7, '320000', '江苏省', '0', 1, 7, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (8, '330000', '浙江省', '0', 1, 8, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (9, '340000', '安徽省', '0', 1, 9, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (10, '350000', '福建省', '0', 1, 10, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (11, '360000', '江西省', '0', 1, 11, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (12, '370000', '山东省', '0', 1, 12, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (13, '410000', '河南省', '0', 1, 13, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (14, '420000', '湖北省', '0', 1, 14, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (15, '430000', '湖南省', '0', 1, 15, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (16, '440000', '广东省', '0', 1, 16, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (17, '450000', '广西壮族自治区', '0', 1, 17, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (18, '460000', '海南省', '0', 1, 18, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (19, '500000', '重庆市', '0', 1, 19, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (20, '510000', '四川省', '0', 1, 20, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (21, '520000', '贵州省', '0', 1, 21, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (22, '530000', '云南省', '0', 1, 22, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (23, '540000', '西藏自治区', '0', 1, 23, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (24, '610000', '陕西省', '0', 1, 24, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (25, '620000', '甘肃省', '0', 1, 25, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (26, '630000', '青海省', '0', 1, 26, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (27, '640000', '宁夏回族自治区', '0', 1, 27, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (28, '650000', '新疆维吾尔自治区', '0', 1, 28, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (29, '440100', '广州市', '440000', 2, 1, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (30, '440200', '韶关市', '440000', 2, 2, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (31, '440300', '深圳市', '440000', 2, 3, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (32, '440400', '珠海市', '440000', 2, 4, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (33, '440500', '汕头市', '440000', 2, 5, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (34, '440600', '佛山市', '440000', 2, 6, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (35, '440700', '江门市', '440000', 2, 7, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (36, '440800', '湛江市', '440000', 2, 8, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (37, '440900', '茂名市', '440000', 2, 9, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (38, '441200', '肇庆市', '440000', 2, 10, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (39, '441300', '惠州市', '440000', 2, 11, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (40, '441400', '梅州市', '440000', 2, 12, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (41, '441500', '汕尾市', '440000', 2, 13, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (42, '441600', '河源市', '440000', 2, 14, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (43, '441700', '阳江市', '440000', 2, 15, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (44, '441800', '清远市', '440000', 2, 16, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (45, '441900', '东莞市', '440000', 2, 17, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (46, '442000', '中山市', '440000', 2, 18, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (47, '445100', '潮州市', '440000', 2, 19, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (48, '445200', '揭阳市', '440000', 2, 20, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (49, '445300', '云浮市', '440000', 2, 21, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (50, '440103', '荔湾区', '440100', 3, 1, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (51, '440104', '越秀区', '440100', 3, 2, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (52, '440105', '海珠区', '440100', 3, 3, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (53, '440106', '天河区', '440100', 3, 4, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (54, '440111', '白云区', '440100', 3, 5, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (55, '440112', '黄埔区', '440100', 3, 6, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (56, '440113', '番禺区', '440100', 3, 7, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (57, '440114', '花都区', '440100', 3, 8, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (58, '440115', '南沙区', '440100', 3, 9, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (59, '440117', '从化区', '440100', 3, 10, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (60, '440118', '增城区', '440100', 3, 11, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (61, '440303', '罗湖区', '440300', 3, 1, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (62, '440304', '福田区', '440300', 3, 2, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (63, '440305', '南山区', '440300', 3, 3, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (64, '440306', '宝安区', '440300', 3, 4, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (65, '440307', '龙岗区', '440300', 3, 5, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (66, '440308', '盐田区', '440300', 3, 6, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (67, '440309', '龙华区', '440300', 3, 7, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (68, '440310', '坪山区', '440300', 3, 8, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);
INSERT INTO `t_region` VALUES (69, '440311', '光明区', '440300', 3, 9, '2026-03-12 16:23:49', '2026-03-12 16:23:49', 0);

-- ----------------------------
-- Table structure for t_wallet_record
-- ----------------------------
DROP TABLE IF EXISTS `t_wallet_record`;
CREATE TABLE `t_wallet_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `type` int(11) NOT NULL COMMENT '流水类型 1-充值 2-消费 3-退款 4-提现',
  `amount` decimal(10, 2) NOT NULL COMMENT '变动金额',
  `balance_after` decimal(10, 2) NOT NULL COMMENT '变动后余额',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联订单号',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '钱包流水' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_wallet_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
