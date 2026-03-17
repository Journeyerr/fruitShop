package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.Category;

import java.util.List;

/**
 * 商品分类Service
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取启用的分类列表
     */
    List<Category> getActiveCategories();
}
