package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Address;
import com.fruitshop.mapper.AddressMapper;
import com.fruitshop.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址Service实现
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<Address> getAddressesByMemberId(Long memberId) {
        return list(new LambdaQueryWrapper<Address>()
                .eq(Address::getMemberId, memberId)
                .orderByDesc(Address::getIsDefault)
                .orderByDesc(Address::getCreateTime));
    }

    @Override
    public Address getDefaultAddress(Long memberId) {
        return getOne(new LambdaQueryWrapper<Address>()
                .eq(Address::getMemberId, memberId)
                .eq(Address::getIsDefault, 1));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Long memberId, Long addressId) {
        // 先取消所有默认地址
        update(new LambdaUpdateWrapper<Address>()
                .eq(Address::getMemberId, memberId)
                .set(Address::getIsDefault, 0));
        
        // 设置新的默认地址
        return update(new LambdaUpdateWrapper<Address>()
                .eq(Address::getId, addressId)
                .eq(Address::getMemberId, memberId)
                .set(Address::getIsDefault, 1));
    }
}
