package com.fruitshop.dto;

import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 订单创建DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCreateDTO extends Order {

    /**
     * 订单项列表
     */
    private List<OrderItem> items;
}
