package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 省市区实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_region")
public class Region extends BaseEntity {

    /**
     * 行政区划代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级代码（省级为0，市级为省级代码，区级为市级代码）
     */
    private String parentCode;

    /**
     * 层级（1省，2市，3区）
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sort;
}
