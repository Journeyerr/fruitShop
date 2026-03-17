package com.fruitshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fruitshop.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单商品Mapper
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
