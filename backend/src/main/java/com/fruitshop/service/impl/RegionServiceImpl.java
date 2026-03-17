package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Region;
import com.fruitshop.mapper.RegionMapper;
import com.fruitshop.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 省市区Service实现类
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    @Override
    public List<Region> getProvinces() {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Region::getLevel, 1)
                .orderByAsc(Region::getSort);
        return this.list(wrapper);
    }

    @Override
    public List<Region> getCitiesByProvinceCode(String provinceCode) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Region::getParentCode, provinceCode)
                .eq(Region::getLevel, 2)
                .orderByAsc(Region::getSort);
        return this.list(wrapper);
    }

    @Override
    public List<Region> getDistrictsByCityCode(String cityCode) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Region::getParentCode, cityCode)
                .eq(Region::getLevel, 3)
                .orderByAsc(Region::getSort);
        return this.list(wrapper);
    }

    @Override
    public List<Region> getByParentCode(String parentCode) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Region::getParentCode, parentCode)
                .orderByAsc(Region::getSort);
        return this.list(wrapper);
    }
}
