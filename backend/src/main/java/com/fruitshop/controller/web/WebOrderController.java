package com.fruitshop.controller.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.dto.OrderCreateDTO;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;
import com.fruitshop.service.OrderItemService;
import com.fruitshop.service.OrderService;
import com.fruitshop.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序-订单
 */
@RestController
@RequestMapping("/web/order")
public class WebOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/page")
    public Result<Page<OrderVO>> page(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) Integer status,
                                      @RequestAttribute Long userId) {
        Page<Order> pageParam = new Page<>(page, size);
        Page<Order> orderPage = orderService.pageOrders(pageParam, userId, status, null);
        
        // 转换为OrderVO并填充订单项
        Page<OrderVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        List<OrderVO> voList = orderPage.getRecords().stream().map(order -> {
            OrderVO vo = OrderVO.fromOrder(order);
            // 查询订单项
            List<OrderItem> items = orderItemService.getByOrderId(order.getId());
            vo.setItems(items);
            // 计算商品数量
            int goodsCount = items.stream().mapToInt(OrderItem::getQuantity).sum();
            vo.setGoodsCount(goodsCount);
            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return Result.success(voPage);
    }

    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.success(null);
        }
        
        OrderVO vo = OrderVO.fromOrder(order);
        // 查询订单项
        List<OrderItem> items = orderItemService.getByOrderId(id);
        vo.setItems(items);
        // 计算商品数量
        int goodsCount = items.stream().mapToInt(OrderItem::getQuantity).sum();
        vo.setGoodsCount(goodsCount);
        
        return Result.success(vo);
    }

    @PostMapping
    public Result<Order> create(@RequestBody OrderCreateDTO orderDTO, @RequestAttribute Long userId) {
        orderDTO.setMemberId(userId);
        Order createdOrder = orderService.createOrderWithItems(orderDTO);
        return Result.success(createdOrder);
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id, @RequestAttribute Long userId) {
        Order order = orderService.getById(id);
        if (order != null && order.getMemberId().equals(userId)) {
            orderService.cancelOrder(id);
        }
        return Result.success();
    }

    @PutMapping("/pay/{id}")
    public Result<Void> pay(@PathVariable Long id, @RequestAttribute Long userId) {
        Order order = orderService.getById(id);
        if (order != null && order.getMemberId().equals(userId)) {
            orderService.payOrder(id);
        }
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result<Void> complete(@PathVariable Long id, @RequestAttribute Long userId) {
        Order order = orderService.getById(id);
        if (order != null && order.getMemberId().equals(userId)) {
            order.setStatus(3); // 已完成
            orderService.updateById(order);
        }
        return Result.success();
    }
}
