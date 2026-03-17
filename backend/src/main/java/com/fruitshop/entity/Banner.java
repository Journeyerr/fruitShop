package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轮播图实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_banner")
public class Banner extends BaseEntity {

    private String title;

    private String imageUrl;

    private Integer linkType;

    private String linkParam;

    private Integer sort;

    private Integer status;
}
