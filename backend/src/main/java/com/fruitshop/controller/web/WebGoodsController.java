package com.fruitshop.controller.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.entity.Category;
import com.fruitshop.entity.Goods;
import com.fruitshop.service.CategoryService;
import com.fruitshop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序-商品
 */
@RestController
@RequestMapping("/web/goods")
public class WebGoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public Result<List<Category>> categoryList() {
        return Result.success(categoryService.getActiveCategories());
    }

    @GetMapping("/recommend")
    public Result<List<Goods>> recommendList() {
        return Result.success(goodsService.getRecommendGoods());
    }

    @GetMapping("/list/{categoryId}")
    public Result<List<Goods>> listByCategory(@PathVariable Long categoryId) {
        return Result.success(goodsService.getGoodsByCategory(categoryId));
    }

    @GetMapping("/page")
    public Result<Page<Goods>> page(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) Long categoryId,
                                    @RequestParam(required = false) String keyword) {
        Page<Goods> pageParam = new Page<>(page, size);
        return Result.success(goodsService.pageGoods(pageParam, categoryId, keyword, 1));
    }

    @GetMapping("/{id}")
    public Result<Goods> detail(@PathVariable Long id) {
        return Result.success(goodsService.getById(id));
    }
}
