package com.fruitshop.controller.web;

import com.fruitshop.common.Result;
import com.fruitshop.entity.Banner;
import com.fruitshop.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序-轮播图
 */
@RestController
@RequestMapping("/web/banner")
public class WebBannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/list")
    public Result<List<Banner>> list() {
        return Result.success(bannerService.getActiveBanners());
    }
}
