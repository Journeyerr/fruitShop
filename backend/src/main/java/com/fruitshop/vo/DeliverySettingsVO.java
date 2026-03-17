package com.fruitshop.vo;

import lombok.Data;

/**
 * 配送设置视图对象
 */
@Data
public class DeliverySettingsVO {

    /**
     * 最大配送距离（公里）
     */
    private Integer maxDistance;

    /**
     * 免配送费金额
     */
    private Integer freeDeliveryAmount;

    /**
     * 配送费
     */
    private Integer deliveryFee;
}
