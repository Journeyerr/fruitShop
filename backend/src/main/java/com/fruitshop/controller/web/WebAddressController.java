package com.fruitshop.controller.web;

import com.fruitshop.common.Result;
import com.fruitshop.entity.Address;
import com.fruitshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序-收货地址
 */
@RestController
@RequestMapping("/web/address")
public class WebAddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/list")
    public Result<List<Address>> list(@RequestAttribute Long userId) {
        return Result.success(addressService.getAddressesByMemberId(userId));
    }

    @GetMapping("/default")
    public Result<Address> getDefault(@RequestAttribute Long userId) {
        return Result.success(addressService.getDefaultAddress(userId));
    }

    @GetMapping("/{id}")
    public Result<Address> getById(@PathVariable Long id) {
        return Result.success(addressService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Address address, @RequestAttribute Long userId) {
        address.setMemberId(userId);
        addressService.save(address);
        // 如果是第一个地址，设置为默认
        if (address.getIsDefault() == 1) {
            addressService.setDefaultAddress(userId, address.getId());
        }
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Address address, @RequestAttribute Long userId) {
        address.setMemberId(userId);
        addressService.updateById(address);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, @RequestAttribute Long userId) {
        Address address = addressService.getById(id);
        if (address != null && address.getMemberId().equals(userId)) {
            addressService.removeById(id);
        }
        return Result.success();
    }

    @PutMapping("/default/{id}")
    public Result<Void> setDefault(@PathVariable Long id, @RequestAttribute Long userId) {
        addressService.setDefaultAddress(userId, id);
        return Result.success();
    }
}
