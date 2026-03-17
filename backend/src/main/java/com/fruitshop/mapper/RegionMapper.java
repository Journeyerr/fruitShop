package com.fruitshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fruitshop.entity.Region;
import org.apache.ibatis.annotations.Mapper;

/**
 * 省市区Mapper
 */
@Mapper
public interface RegionMapper extends BaseMapper<Region> {
}
