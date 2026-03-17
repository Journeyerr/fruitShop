package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order")
public class Order extends BaseEntity {

    private String orderNo;

    private Long memberId;

    private Long addressId;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private BigDecimal goodsAmount;

    private BigDecimal deliveryFee;

    private BigDecimal discountAmount;

    private BigDecimal payAmount;

    private Integer status;

    private Integer payType;

    private LocalDateTime payTime;

    private LocalDateTime deliveryTime;

    private LocalDateTime completeTime;

    private String remark;

    /**
     * 配送方式：pickup-自提，delivery-配送
     */
    private String deliveryMethod;

    private Long couponId;
}
