package com.fruitshop.controller.web;

import com.fruitshop.common.Result;
import com.fruitshop.entity.Coupon;
import com.fruitshop.entity.MemberCoupon;
import com.fruitshop.service.CouponService;
import com.fruitshop.service.MemberCouponService;
import com.fruitshop.vo.CouponCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序-优惠券
 */
@RestController
@RequestMapping("/web/coupon")
public class WebCouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private MemberCouponService memberCouponService;

    @GetMapping("/available")
    public Result<List<Coupon>> available() {
        return Result.success(couponService.getAvailableCoupons());
    }

    @PostMapping("/receive/{couponId}")
    public Result<Void> receive(@PathVariable Long couponId, @RequestAttribute Long userId) {
        couponService.issueCoupon(couponId, userId);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<List<MemberCoupon>> myCoupons(@RequestParam(required = false) Integer status,
                                                @RequestAttribute Long userId) {
        return Result.success(memberCouponService.getCouponsByMemberId(userId, status));
    }

    @GetMapping("/my/counts")
    public Result<CouponCountVO> myCouponCounts(@RequestAttribute Long userId) {
        return Result.success(memberCouponService.getCouponCounts(userId));
    }

    @GetMapping("/usable")
    public Result<List<MemberCoupon>> usable(@RequestParam java.math.BigDecimal amount, @RequestAttribute Long userId) {
        List<MemberCoupon> coupons = memberCouponService.getCouponsByMemberId(userId, 0);
        coupons.removeIf(coupon -> coupon.getMinAmount() != null && amount.compareTo(coupon.getMinAmount()) < 0);
        return Result.success(coupons);
    }
}
