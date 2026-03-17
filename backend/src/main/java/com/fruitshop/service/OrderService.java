package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.dto.OrderCreateDTO;
import com.fruitshop.entity.Order;
import com.fruitshop.vo.OrderVO;

/**
 * 订单Service
 */
public interface OrderService extends IService<Order> {

    /**
     * 分页查询订单
     */
    Page<Order> pageOrders(Page<Order> page, Long memberId, Integer status, String orderNo);

    /**
     * 创建订单
     */
    Order createOrder(Order order);

    /**
     * 创建订单（包含订单项）
     */
    Order createOrderWithItems(OrderCreateDTO orderDTO);

    /**
     * 取消订单
     */
    boolean cancelOrder(Long orderId);

    /**
     * 支付订单
     */
    boolean payOrder(Long orderId);

    /**
     * 发货
     */
    boolean deliveryOrder(Long orderId);

    /**
     * 获取订单详情（包含订单项）
     */
    OrderVO getOrderDetail(Long orderId);
}
