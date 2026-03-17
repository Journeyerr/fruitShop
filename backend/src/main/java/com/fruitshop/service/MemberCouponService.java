package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.MemberCoupon;
import com.fruitshop.vo.CouponCountVO;

import java.util.List;

/**
 * 会员优惠券Service
 */
public interface MemberCouponService extends IService<MemberCoupon> {

    /**
     * 获取会员的优惠券列表
     */
    List<MemberCoupon> getCouponsByMemberId(Long memberId, Integer status);

    /**
     * 使用优惠券
     */
    boolean useCoupon(Long memberCouponId);

    /**
     * 获取会员优惠券数量统计
     */
    CouponCountVO getCouponCounts(Long memberId);
}
