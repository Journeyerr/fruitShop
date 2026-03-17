package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Coupon;
import com.fruitshop.entity.MemberCoupon;
import com.fruitshop.mapper.CouponMapper;
import com.fruitshop.service.CouponService;
import com.fruitshop.service.MemberCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券Service实现
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private MemberCouponService memberCouponService;

    @Override
    public Page<Coupon> pageCoupons(Page<Coupon> page, Integer status) {
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Coupon::getStatus, status);
        }
        wrapper.orderByDesc(Coupon::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public List<Coupon> getAvailableCoupons() {
        return list(new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getStatus, 1)
                .gt(Coupon::getEndTime, LocalDateTime.now())
                .apply("received_count < total_count")
                .orderByDesc(Coupon::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean issueCoupon(Long couponId, Long memberId) {
        Coupon coupon = getById(couponId);
        if (coupon == null || coupon.getStatus() != 1) {
            return false;
        }

        // 检查是否已领取
        long count = memberCouponService.count(new LambdaQueryWrapper<MemberCoupon>()
                .eq(MemberCoupon::getMemberId, memberId)
                .eq(MemberCoupon::getCouponId, couponId));
        if (count >= coupon.getLimitCount()) {
            return false;
        }

        // 创建会员优惠券
        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setMemberId(memberId);
        memberCoupon.setCouponId(couponId);
        memberCoupon.setCouponName(coupon.getName());
        memberCoupon.setCouponType(coupon.getType());
        memberCoupon.setAmount(coupon.getAmount());
        memberCoupon.setDiscount(coupon.getDiscount());
        memberCoupon.setMinAmount(coupon.getMinAmount());
        memberCoupon.setStatus(0); // 未使用
        memberCoupon.setReceiveTime(LocalDateTime.now());
        
        if (coupon.getValidDays() != null && coupon.getValidDays() > 0) {
            memberCoupon.setStartTime(LocalDateTime.now());
            memberCoupon.setEndTime(LocalDateTime.now().plusDays(coupon.getValidDays()));
        } else {
            memberCoupon.setStartTime(coupon.getStartTime());
            memberCoupon.setEndTime(coupon.getEndTime());
        }

        memberCouponService.save(memberCoupon);

        // 更新已领取数量
        coupon.setReceivedCount(coupon.getReceivedCount() + 1);
        updateById(coupon);

        return true;
    }
}
