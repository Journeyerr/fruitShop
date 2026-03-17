package com.fruitshop.controller.web;

import com.fruitshop.common.Result;
import com.fruitshop.entity.Member;
import com.fruitshop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序-会员信息
 */
@RestController
@RequestMapping("/web/member")
public class WebMemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/info")
    public Result<Member> info(@RequestAttribute Long userId) {
        return Result.success(memberService.getById(userId));
    }

    @PutMapping
    public Result<Void> update(@RequestBody Member member, @RequestAttribute Long userId) {
        member.setId(userId);
        memberService.updateById(member);
        return Result.success();
    }

    @PutMapping("/phone")
    public Result<Void> bindPhone(@RequestParam String phone, @RequestAttribute Long userId) {
        Member member = memberService.getById(userId);
        if (member != null) {
            member.setPhone(phone);
            memberService.updateById(member);
        }
        return Result.success();
    }
}
