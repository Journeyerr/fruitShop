package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_coupon")
public class Coupon extends BaseEntity {

    private String name;

    private Integer type;

    private BigDecimal amount;

    private BigDecimal discount;

    private BigDecimal minAmount;

    private Integer totalCount;

    private Integer receivedCount;

    private Integer limitCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer validDays;

    private Integer status;
}
