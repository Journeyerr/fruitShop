package com.fruitshop.controller.admin;

import com.fruitshop.common.Result;
import com.fruitshop.dto.DeliverySettingsDTO;
import com.fruitshop.vo.DeliverySettingsVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 后台-配送设置
 */
@RestController
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {

    @Value("${business.delivery.max-distance:5}")
    private Integer maxDistance;

    @Value("${business.delivery.free-delivery-amount:59}")
    private Integer freeDeliveryAmount;

    @Value("${business.delivery.delivery-fee:5}")
    private Integer deliveryFee;

    @GetMapping
    public Result<DeliverySettingsVO> getSettings() {
        DeliverySettingsVO settingsVO = new DeliverySettingsVO();
        settingsVO.setMaxDistance(maxDistance);
        settingsVO.setFreeDeliveryAmount(freeDeliveryAmount);
        settingsVO.setDeliveryFee(deliveryFee);
        return Result.success(settingsVO);
    }

    @PutMapping
    public Result<Void> updateSettings(@RequestBody DeliverySettingsDTO settingsDTO) {
        // TODO: 将设置保存到数据库或配置中心
        return Result.success();
    }
}
