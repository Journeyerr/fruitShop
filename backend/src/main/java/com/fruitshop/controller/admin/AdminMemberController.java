package com.fruitshop.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.entity.Member;
import com.fruitshop.service.MemberService;
import com.fruitshop.vo.MemberStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台-会员管理
 */
@RestController
@RequestMapping("/admin/member")
public class AdminMemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/page")
    public Result<Page<Member>> page(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String keyword) {
        Page<Member> pageParam = new Page<>(page, size);
        return Result.success(memberService.pageMembers(pageParam, keyword));
    }

    @GetMapping("/{id}")
    public Result<Member> getById(@PathVariable Long id) {
        return Result.success(memberService.getById(id));
    }

    @GetMapping("/statistics")
    public Result<MemberStatisticsVO> statistics() {
        // TODO: 实现会员统计逻辑
        MemberStatisticsVO statisticsVO = new MemberStatisticsVO();
        return Result.success(statisticsVO);
    }
}
