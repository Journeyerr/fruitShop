package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会员优惠券实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_member_coupon")
public class MemberCoupon extends BaseEntity {

    private Long memberId;

    private Long couponId;

    private String couponName;

    private Integer couponType;

    private java.math.BigDecimal amount;

    private java.math.BigDecimal discount;

    private java.math.BigDecimal minAmount;

    private Integer status;

    private LocalDateTime receiveTime;

    private LocalDateTime useTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
