package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会员实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_member")
public class Member extends BaseEntity {

    private String openid;

    private String unionid;

    private String nickname;

    private String avatar;

    private String phone;

    private Integer gender;

    private BigDecimal balance;

    private Integer points;

    private Integer level;

    private java.time.LocalDateTime lastLoginTime;
}
