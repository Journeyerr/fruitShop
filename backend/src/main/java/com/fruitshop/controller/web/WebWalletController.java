package com.fruitshop.controller.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fruitshop.common.Result;
import com.fruitshop.entity.Member;
import com.fruitshop.entity.WalletRecord;
import com.fruitshop.service.MemberService;
import com.fruitshop.service.WalletRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序-钱包
 */
@RestController
@RequestMapping("/web/wallet")
public class WebWalletController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private WalletRecordService walletRecordService;

    @GetMapping("/info")
    public Result<Map<String, Object>> info(@RequestAttribute Long userId) {
        Member member = memberService.getById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("balance", member.getBalance());
        result.put("points", member.getPoints());
        return Result.success(result);
    }

    @GetMapping("/records")
    public Result<Page<WalletRecord>> records(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              @RequestAttribute Long userId) {
        Page<WalletRecord> pageParam = new Page<>(page, size);
        return Result.success(walletRecordService.pageRecords(pageParam, userId));
    }

    @PostMapping("/recharge")
    public Result<Void> recharge(@RequestParam BigDecimal amount, @RequestAttribute Long userId) {
        walletRecordService.createRecord(userId, 1, amount, null, "充值");
        return Result.success();
    }

    @PostMapping("/withdraw")
    public Result<Void> withdraw(@RequestParam BigDecimal amount, @RequestAttribute Long userId) {
        Member member = memberService.getById(userId);
        if (member.getBalance().compareTo(amount) < 0) {
            return Result.error("余额不足");
        }
        walletRecordService.createRecord(userId, 4, amount, null, "提现");
        return Result.success();
    }
}
