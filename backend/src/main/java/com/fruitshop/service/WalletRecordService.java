package com.fruitshop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fruitshop.entity.WalletRecord;

import java.util.List;

/**
 * 钱包流水Service
 */
public interface WalletRecordService extends IService<WalletRecord> {

    /**
     * 获取会员的钱包流水
     */
    List<WalletRecord> getRecordsByMemberId(Long memberId);

    /**
     * 分页查询钱包流水
     */
    Page<WalletRecord> pageRecords(Page<WalletRecord> page, Long memberId);

    /**
     * 创建流水记录
     */
    boolean createRecord(Long memberId, Integer type, java.math.BigDecimal amount, String orderNo, String remark);
}
