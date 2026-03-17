package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.OrderItem;

import java.util.List;

/**
 * 订单商品Service
 */
public interface OrderItemService extends IService<OrderItem> {

    /**
     * 根据订单ID查询订单项
     */
    List<OrderItem> getByOrderId(Long orderId);
}
