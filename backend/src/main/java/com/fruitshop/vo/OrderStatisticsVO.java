package com.fruitshop.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单统计视图对象
 */
@Data
public class OrderStatisticsVO {

    /**
     * 今日订单数
     */
    private Integer todayCount;

    /**
     * 今日销售额
     */
    private BigDecimal todayAmount;

    /**
     * 待付款订单数
     */
    private Integer pendingPaymentCount;

    /**
     * 待配送订单数
     */
    private Integer pendingDeliveryCount;

    /**
     * 待收货订单数
     */
    private Integer pendingReceiveCount;

    /**
     * 已完成订单数
     */
    private Integer completedCount;

    /**
     * 已取消订单数
     */
    private Integer cancelledCount;
}
