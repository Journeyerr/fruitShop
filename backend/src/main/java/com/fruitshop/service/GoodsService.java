package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.Goods;

import java.util.List;

/**
 * 商品Service
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 分页查询商品
     */
    Page<Goods> pageGoods(Page<Goods> page, Long categoryId, String keyword, Integer status);

    /**
     * 获取推荐商品
     */
    List<Goods> getRecommendGoods();

    /**
     * 根据分类获取商品列表
     */
    List<Goods> getGoodsByCategory(Long categoryId);

    /**
     * 更新库存
     */
    boolean updateStock(Long goodsId, Integer quantity);
}
