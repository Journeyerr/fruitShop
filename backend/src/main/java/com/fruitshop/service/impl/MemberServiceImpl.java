package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Member;
import com.fruitshop.mapper.MemberMapper;
import com.fruitshop.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员Service实现
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public Member getByOpenid(String openid) {
        return getOne(new LambdaQueryWrapper<Member>().eq(Member::getOpenid, openid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member wechatLogin(String openid, String nickname, String avatar) {
        Member member = getByOpenid(openid);
        if (member == null) {
            // 新用户注册
            member = new Member();
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(avatar);
            member.setBalance(BigDecimal.ZERO);
            member.setPoints(0);
            member.setLevel(1);
            member.setLastLoginTime(LocalDateTime.now());
            save(member);
        } else {
            // 更新最后登录时间
            member.setLastLoginTime(LocalDateTime.now());
            updateById(member);
        }
        return member;
    }

    @Override
    public Page<Member> pageMembers(Page<Member> page, String keyword) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Member::getNickname, keyword)
                   .or()
                   .like(Member::getPhone, keyword);
        }
        wrapper.orderByDesc(Member::getCreateTime);
        return page(page, wrapper);
    }
}
