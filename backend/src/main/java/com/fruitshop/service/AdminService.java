package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.Admin;

/**
 * 管理员Service
 */
public interface AdminService extends IService<Admin> {

    /**
     * 根据用户名查询管理员
     */
    Admin getByUsername(String username);

    /**
     * 更新最后登录时间
     */
    void updateLastLoginTime(Long adminId);
}
