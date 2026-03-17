package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.Address;

import java.util.List;

/**
 * 收货地址Service
 */
public interface AddressService extends IService<Address> {

    /**
     * 获取会员的地址列表
     */
    List<Address> getAddressesByMemberId(Long memberId);

    /**
     * 获取会员的默认地址
     */
    Address getDefaultAddress(Long memberId);

    /**
     * 设置默认地址
     */
    boolean setDefaultAddress(Long memberId, Long addressId);
}
