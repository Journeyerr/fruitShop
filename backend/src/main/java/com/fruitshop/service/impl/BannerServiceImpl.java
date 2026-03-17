package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Banner;
import com.fruitshop.mapper.BannerMapper;
import com.fruitshop.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图Service实现
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public List<Banner> getActiveBanners() {
        return list(new LambdaQueryWrapper<Banner>()
                .eq(Banner::getStatus, 1)
                .orderByAsc(Banner::getSort)
                .orderByDesc(Banner::getCreateTime));
    }
}
