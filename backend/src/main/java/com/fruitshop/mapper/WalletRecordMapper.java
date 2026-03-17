package com.fruitshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fruitshop.entity.WalletRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 钱包流水Mapper
 */
@Mapper
public interface WalletRecordMapper extends BaseMapper<WalletRecord> {
}
