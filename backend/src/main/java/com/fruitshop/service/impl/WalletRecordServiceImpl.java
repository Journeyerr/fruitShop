package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fruitshop.entity.Member;
import com.fruitshop.entity.WalletRecord;
import com.fruitshop.mapper.WalletRecordMapper;
import com.fruitshop.service.MemberService;
import com.fruitshop.service.WalletRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包流水Service实现
 */
@Service
public class WalletRecordServiceImpl extends ServiceImpl<WalletRecordMapper, WalletRecord> implements WalletRecordService {

    @Autowired
    private MemberService memberService;

    @Override
    public List<WalletRecord> getRecordsByMemberId(Long memberId) {
        return list(new LambdaQueryWrapper<WalletRecord>()
                .eq(WalletRecord::getMemberId, memberId)
                .orderByDesc(WalletRecord::getCreateTime));
    }

    @Override
    public Page<WalletRecord> pageRecords(Page<WalletRecord> page, Long memberId) {
        LambdaQueryWrapper<WalletRecord> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(WalletRecord::getMemberId, memberId);
        }
        wrapper.orderByDesc(WalletRecord::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRecord(Long memberId, Integer type, BigDecimal amount, String orderNo, String remark) {
        Member member = memberService.getById(memberId);
        if (member == null) {
            return false;
        }

        // 更新余额
        BigDecimal balanceAfter = member.getBalance();
        if (type == 1) { // 充值
            balanceAfter = balanceAfter.add(amount);
        } else if (type == 2 || type == 4) { // 消费或提现
            balanceAfter = balanceAfter.subtract(amount);
        } else if (type == 3) { // 退款
            balanceAfter = balanceAfter.add(amount);
        }
        member.setBalance(balanceAfter);
        memberService.updateById(member);

        // 创建流水记录
        WalletRecord record = new WalletRecord();
        record.setMemberId(memberId);
        record.setType(type);
        record.setAmount(amount);
        record.setBalanceAfter(balanceAfter);
        record.setOrderNo(orderNo);
        record.setRemark(remark);
        return save(record);
    }
}
