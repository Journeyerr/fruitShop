package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.Member;

/**
 * 会员Service
 */
public interface MemberService extends IService<Member> {

    /**
     * 根据openid查询会员
     */
    Member getByOpenid(String openid);

    /**
     * 微信登录
     */
    Member wechatLogin(String openid, String nickname, String avatar);

    /**
     * 分页查询会员
     */
    Page<Member> pageMembers(Page<Member> page, String keyword);
}
