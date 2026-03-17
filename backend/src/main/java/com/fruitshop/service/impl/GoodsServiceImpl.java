package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Goods;
import com.fruitshop.mapper.GoodsMapper;
import com.fruitshop.service.GoodsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品Service实现
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public Page<Goods> pageGoods(Page<Goods> page, Long categoryId, String keyword, Integer status) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(Goods::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Goods::getName, keyword);
        }
        if (status != null) {
            wrapper.eq(Goods::getStatus, status);
        }
        wrapper.orderByAsc(Goods::getSort)
               .orderByDesc(Goods::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public List<Goods> getRecommendGoods() {
        return list(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getStatus, 1)
                .eq(Goods::getRecommend, 1)
                .orderByAsc(Goods::getSort)
                .orderByDesc(Goods::getCreateTime));
    }

    @Override
    public List<Goods> getGoodsByCategory(Long categoryId) {
        return list(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getStatus, 1)
                .eq(Goods::getCategoryId, categoryId)
                .orderByAsc(Goods::getSort)
                .orderByDesc(Goods::getCreateTime));
    }

    @Override
    public boolean updateStock(Long goodsId, Integer quantity) {
        return update(new LambdaUpdateWrapper<Goods>()
                .eq(Goods::getId, goodsId)
                .setSql("stock = stock + " + quantity));
    }
}
