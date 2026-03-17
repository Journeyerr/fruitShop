package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.Coupon;

import java.util.List;

/**
 * 优惠券Service
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 分页查询优惠券
     */
    Page<Coupon> pageCoupons(Page<Coupon> page, Integer status);

    /**
     * 获取可领取的优惠券列表
     */
    List<Coupon> getAvailableCoupons();

    /**
     * 发放优惠券
     */
    boolean issueCoupon(Long couponId, Long memberId);
}
