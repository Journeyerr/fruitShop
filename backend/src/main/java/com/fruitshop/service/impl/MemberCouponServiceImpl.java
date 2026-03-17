package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.MemberCoupon;
import com.fruitshop.mapper.MemberCouponMapper;
import com.fruitshop.service.MemberCouponService;
import com.fruitshop.vo.CouponCountVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员优惠券Service实现
 */
@Service
public class MemberCouponServiceImpl extends ServiceImpl<MemberCouponMapper, MemberCoupon> implements MemberCouponService {

    @Override
    public List<MemberCoupon> getCouponsByMemberId(Long memberId, Integer status) {
        // 先更新过期优惠券状态
        updateExpiredCoupons(memberId);
        
        LambdaQueryWrapper<MemberCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberCoupon::getMemberId, memberId);
        if (status != null) {
            wrapper.eq(MemberCoupon::getStatus, status);
        }
        wrapper.orderByDesc(MemberCoupon::getCreateTime);
        return list(wrapper);
    }

    @Override
    public boolean useCoupon(Long memberCouponId) {
        return update(new LambdaUpdateWrapper<MemberCoupon>()
                .eq(MemberCoupon::getId, memberCouponId)
                .set(MemberCoupon::getStatus, 1) // 已使用
                .set(MemberCoupon::getUseTime, LocalDateTime.now()));
    }

    @Override
    public CouponCountVO getCouponCounts(Long memberId) {
        // 先更新过期优惠券状态
        updateExpiredCoupons(memberId);
        
        // 可使用 (status=0)
        Long usable = count(new LambdaQueryWrapper<MemberCoupon>()
                .eq(MemberCoupon::getMemberId, memberId)
                .eq(MemberCoupon::getStatus, 0));
        
        // 已使用 (status=1)
        Long used = count(new LambdaQueryWrapper<MemberCoupon>()
                .eq(MemberCoupon::getMemberId, memberId)
                .eq(MemberCoupon::getStatus, 1));
        
        // 已过期 (status=2)
        Long expired = count(new LambdaQueryWrapper<MemberCoupon>()
                .eq(MemberCoupon::getMemberId, memberId)
                .eq(MemberCoupon::getStatus, 2));
        
        return CouponCountVO.of(usable.intValue(), used.intValue(), expired.intValue());
    }

    /**
     * 更新过期优惠券状态
     * 将已过期但状态仍为"可使用"的优惠券标记为"已过期"
     */
    private void updateExpiredCoupons(Long memberId) {
        update(new LambdaUpdateWrapper<MemberCoupon>()
                .eq(MemberCoupon::getMemberId, memberId)
                .eq(MemberCoupon::getStatus, 0) // 状态为"可使用"
                .lt(MemberCoupon::getEndTime, LocalDateTime.now()) // 结束时间小于当前时间
                .set(MemberCoupon::getStatus, 2)); // 设置为"已过期"
    }
}
