package com.fruitshop.controller.web;

import com.fruitshop.common.Result;
import com.fruitshop.entity.Region;
import com.fruitshop.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序-省市区
 */
@RestController
@RequestMapping("/web/region")
public class WebRegionController {

    @Autowired
    private RegionService regionService;

    /**
     * 获取所有省份
     */
    @GetMapping("/provinces")
    public Result<List<Region>> getProvinces() {
        return Result.success(regionService.getProvinces());
    }

    /**
     * 根据省份代码获取城市列表
     */
    @GetMapping("/cities/{provinceCode}")
    public Result<List<Region>> getCities(@PathVariable String provinceCode) {
        return Result.success(regionService.getCitiesByProvinceCode(provinceCode));
    }

    /**
     * 根据城市代码获取区县列表
     */
    @GetMapping("/districts/{cityCode}")
    public Result<List<Region>> getDistricts(@PathVariable String cityCode) {
        return Result.success(regionService.getDistrictsByCityCode(cityCode));
    }

    /**
     * 根据父级代码获取子级区域列表
     */
    @GetMapping("/children/{parentCode}")
    public Result<List<Region>> getByParentCode(@PathVariable String parentCode) {
        return Result.success(regionService.getByParentCode(parentCode));
    }
}
