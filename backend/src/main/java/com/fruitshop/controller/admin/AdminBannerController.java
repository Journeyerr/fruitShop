package com.fruitshop.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.dto.StatusDTO;
import com.fruitshop.entity.Banner;
import com.fruitshop.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台-轮播图管理
 */
@RestController
@RequestMapping("/admin/banner")
public class AdminBannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/page")
    public Result<Page<Banner>> page(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        Page<Banner> pageParam = new Page<>(page, size);
        return Result.success(bannerService.page(pageParam));
    }

    @GetMapping("/{id}")
    public Result<Banner> getById(@PathVariable Long id) {
        return Result.success(bannerService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Banner banner) {
        bannerService.save(banner);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Banner banner) {
        bannerService.updateById(banner);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody StatusDTO statusDTO) {
        Banner banner = bannerService.getById(id);
        if (banner != null) {
            banner.setStatus(statusDTO.getStatus());
            bannerService.updateById(banner);
        }
        return Result.success();
    }
}
