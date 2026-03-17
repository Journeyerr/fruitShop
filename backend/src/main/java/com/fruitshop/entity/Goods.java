package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_goods")
public class Goods extends BaseEntity {

    private Long categoryId;

    private String name;

    private String image;

    private String images;

    private String description;

    private String detail;

    private BigDecimal originalPrice;

    private BigDecimal price;

    private Integer stock;

    private Integer sales;

    private String unit;

    private String specification;

    private Integer sort;

    private Integer status;

    private Integer recommend;
}
