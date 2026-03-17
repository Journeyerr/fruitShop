package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Admin;
import com.fruitshop.mapper.AdminMapper;
import com.fruitshop.service.AdminService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 管理员Service实现
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public Admin getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
    }

    @Override
    public void updateLastLoginTime(Long adminId) {
        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setLastLoginTime(LocalDateTime.now());
        updateById(admin);
    }
}
