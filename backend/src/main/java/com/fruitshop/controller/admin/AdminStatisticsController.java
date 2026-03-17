package com.fruitshop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fruitshop.common.Result;
import com.fruitshop.entity.Goods;
import com.fruitshop.entity.Order;
import com.fruitshop.service.GoodsService;
import com.fruitshop.service.MemberService;
import com.fruitshop.service.OrderService;
import com.fruitshop.vo.OverviewStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 后台-数据统计
 */
@RestController
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    /**
     * 统计概览
     */
    @GetMapping("/overview")
    public Result<OverviewStatisticsVO> overview() {
        OverviewStatisticsVO vo = new OverviewStatisticsVO();
        
        // 商品总数（上架状态）
        Long goodsCount = goodsService.count(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getStatus, 1));
        vo.setGoodsCount(goodsCount);
        
        // 订单总数（已完成）
        Long orderCount = orderService.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, 3));
        vo.setOrderCount(orderCount);
        
        // 会员总数
        Long memberCount = memberService.count();
        vo.setMemberCount(memberCount);
        
        // 销售总额（已完成订单的总金额）
        BigDecimal totalSales = orderService.lambdaQuery()
                .eq(Order::getStatus, 3)
                .list()
                .stream()
                .map(Order::getPayAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalSales(totalSales);
        
        return Result.success(vo);
    }
}
