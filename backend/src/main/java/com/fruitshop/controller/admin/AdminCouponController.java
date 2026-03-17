package com.fruitshop.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.dto.StatusDTO;
import com.fruitshop.entity.Coupon;
import com.fruitshop.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台-优惠券管理
 */
@RestController
@RequestMapping("/admin/coupon")
public class AdminCouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/page")
    public Result<Page<Coupon>> page(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) Integer status) {
        Page<Coupon> pageParam = new Page<>(page, size);
        return Result.success(couponService.pageCoupons(pageParam, status));
    }

    @GetMapping("/{id}")
    public Result<Coupon> getById(@PathVariable Long id) {
        return Result.success(couponService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Coupon coupon) {
        coupon.setReceivedCount(0);
        couponService.save(coupon);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Coupon coupon) {
        couponService.updateById(coupon);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        couponService.removeById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody StatusDTO statusDTO) {
        Coupon coupon = new Coupon();
        coupon.setId(id);
        coupon.setStatus(statusDTO.getStatus());
        couponService.updateById(coupon);
        return Result.success();
    }

    @PostMapping("/issue")
    public Result<Void> issueCoupon(@RequestParam Long couponId, @RequestParam Long memberId) {
        couponService.issueCoupon(couponId, memberId);
        return Result.success();
    }
}
