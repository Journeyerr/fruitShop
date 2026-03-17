package com.fruitshop.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.dto.StatusDTO;
import com.fruitshop.entity.Goods;
import com.fruitshop.service.CategoryService;
import com.fruitshop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台-商品管理
 */
@RestController
@RequestMapping("/admin/goods")
public class AdminGoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<Page<Goods>> page(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) Long categoryId,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer status) {
        Page<Goods> pageParam = new Page<>(page, size);
        return Result.success(goodsService.pageGoods(pageParam, categoryId, keyword, status));
    }

    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable Long id) {
        return Result.success(goodsService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Goods goods) {
        goods.setSales(0);
        goodsService.save(goods);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Goods goods) {
        goodsService.updateById(goods);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        goodsService.removeById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody StatusDTO statusDTO) {
        Goods goods = goodsService.getById(id);
        if (goods != null) {
            goods.setStatus(statusDTO.getStatus());
            goodsService.updateById(goods);
        }
        return Result.success();
    }
}
