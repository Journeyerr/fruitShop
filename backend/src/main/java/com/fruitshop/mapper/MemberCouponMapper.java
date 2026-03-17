package com.fruitshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fruitshop.entity.MemberCoupon;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员优惠券Mapper
 */
@Mapper
public interface MemberCouponMapper extends BaseMapper<MemberCoupon> {
}
