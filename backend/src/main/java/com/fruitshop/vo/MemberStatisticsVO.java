package com.fruitshop.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员统计视图对象
 */
@Data
public class MemberStatisticsVO {

    /**
     * 总会员数
     */
    private Integer totalCount;

    /**
     * 今日新增会员数
     */
    private Integer todayCount;

    /**
     * 本月新增会员数
     */
    private Integer monthCount;

    /**
     * 活跃会员数（近30天有登录）
     */
    private Integer activeCount;

    /**
     * 会员总余额
     */
    private BigDecimal totalBalance;

    /**
     * 会员总积分
     */
    private Integer totalPoints;
}
