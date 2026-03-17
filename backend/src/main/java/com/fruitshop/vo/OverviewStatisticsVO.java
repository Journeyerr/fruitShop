package com.fruitshop.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 统计概览视图对象
 */
@Data
public class OverviewStatisticsVO {

    /**
     * 商品总数
     */
    private Long goodsCount;

    /**
     * 订单总数
     */
    private Long orderCount;

    /**
     * 会员总数
     */
    private Long memberCount;

    /**
     * 销售总额
     */
    private BigDecimal totalSales;
}
