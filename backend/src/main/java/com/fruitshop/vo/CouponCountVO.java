package com.fruitshop.vo;

import lombok.Data;

/**
 * 优惠券数量统计
 */
@Data
public class CouponCountVO {
    
    /**
     * 可使用数量
     */
    private Integer usable;
    
    /**
     * 已使用数量
     */
    private Integer used;
    
    /**
     * 已过期数量
     */
    private Integer expired;
    
    public static CouponCountVO of(Integer usable, Integer used, Integer expired) {
        CouponCountVO vo = new CouponCountVO();
        vo.setUsable(usable != null ? usable : 0);
        vo.setUsed(used != null ? used : 0);
        vo.setExpired(expired != null ? expired : 0);
        return vo;
    }
}
