package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Category;
import com.fruitshop.mapper.CategoryMapper;
import com.fruitshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类Service实现
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> getActiveCategories() {
        return list(new LambdaQueryWrapper<Category>()
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getCreateTime));
    }
}
