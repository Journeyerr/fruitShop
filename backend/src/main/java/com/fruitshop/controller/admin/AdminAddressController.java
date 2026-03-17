package com.fruitshop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.entity.Address;
import com.fruitshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 后台-收货地址管理
 */
@RestController
@RequestMapping("/admin/address")
public class AdminAddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/page")
    public Result<Page<Address>> page(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) Long memberId,
                                      @RequestParam(required = false) String receiverName,
                                      @RequestParam(required = false) String phone) {
        Page<Address> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(Address::getMemberId, memberId);
        }
        if (StringUtils.hasText(receiverName)) {
            wrapper.like(Address::getReceiverName, receiverName);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(Address::getReceiverPhone, phone);
        }
        wrapper.orderByDesc(Address::getCreateTime);
        return Result.success(addressService.page(pageParam, wrapper));
    }

    @GetMapping("/{id}")
    public Result<Address> getById(@PathVariable Long id) {
        return Result.success(addressService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        addressService.removeById(id);
        return Result.success();
    }
}
