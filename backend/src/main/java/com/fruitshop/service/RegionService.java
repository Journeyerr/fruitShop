package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.Region;

import java.util.List;

/**
 * 省市区Service
 */
public interface RegionService extends IService<Region> {

    /**
     * 获取所有省份
     */
    List<Region> getProvinces();

    /**
     * 根据省份代码获取城市列表
     */
    List<Region> getCitiesByProvinceCode(String provinceCode);

    /**
     * 根据城市代码获取区县列表
     */
    List<Region> getDistrictsByCityCode(String cityCode);

    /**
     * 根据父级代码获取子级区域列表
     */
    List<Region> getByParentCode(String parentCode);
}
