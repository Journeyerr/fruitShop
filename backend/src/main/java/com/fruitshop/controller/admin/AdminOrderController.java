package com.fruitshop.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.entity.Order;
import com.fruitshop.service.OrderService;
import com.fruitshop.vo.OrderStatisticsVO;
import com.fruitshop.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台-订单管理
 */
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/page")
    public Result<Page<Order>> page(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) Integer status,
                                    @RequestParam(required = false) String orderNo) {
        Page<Order> pageParam = new Page<>(page, size);
        return Result.success(orderService.pageOrders(pageParam, null, status, orderNo));
    }

    @GetMapping("/{id}")
    public Result<OrderVO> getById(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    @PutMapping("/deliver/{id}")
    public Result<Void> deliver(@PathVariable Long id) {
        orderService.deliveryOrder(id);
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        // TODO: 实现订单统计逻辑
        OrderStatisticsVO statisticsVO = new OrderStatisticsVO();
        return Result.success(statisticsVO);
    }
}
