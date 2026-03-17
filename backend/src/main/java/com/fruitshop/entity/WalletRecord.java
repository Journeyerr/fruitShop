package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 钱包流水实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_wallet_record")
public class WalletRecord extends BaseEntity {

    private Long memberId;

    private Integer type;

    private BigDecimal amount;

    private BigDecimal balanceAfter;

    private String orderNo;

    private String remark;
}
