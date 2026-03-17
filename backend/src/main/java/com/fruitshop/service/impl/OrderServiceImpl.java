package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.dto.OrderCreateDTO;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;
import com.fruitshop.mapper.OrderMapper;
import com.fruitshop.service.OrderItemService;
import com.fruitshop.service.OrderService;
import com.fruitshop.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订单Service实现
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderItemService orderItemService;

    @Override
    public Page<Order> pageOrders(Page<Order> page, Long memberId, Integer status, String orderNo) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(Order::getMemberId, memberId);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        if (StringUtils.hasText(orderNo)) {
            wrapper.like(Order::getOrderNo, orderNo);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        // 生成订单号
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(0); // 待付款
        save(order);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrderWithItems(OrderCreateDTO orderDTO) {
        // 生成订单号
        String orderNo = generateOrderNo();
        orderDTO.setOrderNo(orderNo);
        orderDTO.setStatus(0); // 待付款
        
        // 保存订单
        save(orderDTO);
        
        // 保存订单项
        List<OrderItem> items = orderDTO.getItems();
        if (items != null && !items.isEmpty()) {
            for (OrderItem item : items) {
                item.setOrderId(orderDTO.getId());
                // 计算小计金额
                if (item.getPrice() != null && item.getQuantity() != null) {
                    item.setAmount(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                }
            }
            orderItemService.saveBatch(items);
        }
        
        return orderDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId) {
        return update(new LambdaUpdateWrapper<Order>()
                .eq(Order::getId, orderId)
                .set(Order::getStatus, 4) // 已取消
                .set(Order::getUpdateTime, LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(Long orderId) {
        // 获取订单信息
        Order order = getById(orderId);
        if (order == null) {
            return false;
        }
        
        // 根据配送方式设置不同状态
        // 自提订单：status=2 待收货
        // 配送订单：status=1 待配送
        int newStatus = "pickup".equals(order.getDeliveryMethod()) ? 2 : 1;
        
        return update(new LambdaUpdateWrapper<Order>()
                .eq(Order::getId, orderId)
                .set(Order::getStatus, newStatus)
                .set(Order::getPayTime, LocalDateTime.now())
                .set(Order::getUpdateTime, LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deliveryOrder(Long orderId) {
        return update(new LambdaUpdateWrapper<Order>()
                .eq(Order::getId, orderId)
                .set(Order::getStatus, 2) // 待收货
                .set(Order::getDeliveryTime, LocalDateTime.now())
                .set(Order::getUpdateTime, LocalDateTime.now()));
    }

    @Override
    public OrderVO getOrderDetail(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return null;
        }
        
        OrderVO orderVO = OrderVO.fromOrder(order);
        
        // 查询订单项
        List<OrderItem> items = orderItemService.getByOrderId(orderId);
        orderVO.setItems(items);
        
        // 计算商品数量
        int goodsCount = items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
        orderVO.setGoodsCount(goodsCount);
        
        return orderVO;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        // 获取当前时间并格式化为 yyyyMMddHHmmss
        String timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        );
        
        // 生成 4 位随机数 (1000-9999)
        int randomNum = new java.util.Random().nextInt(9000) + 1000;
        
        return timestamp + randomNum;
    }
}
