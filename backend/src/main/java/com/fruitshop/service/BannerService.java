package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.Banner;

import java.util.List;

/**
 * 轮播图Service
 */
public interface BannerService extends IService<Banner> {

    /**
     * 获取启用的轮播图列表
     */
    List<Banner> getActiveBanners();
}
