package com.fruitshop.vo;

import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单视图对象
 */
@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private Integer status;
    private String statusText;
    private BigDecimal payAmount;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime completeTime;
    
    // 收货信息
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    
    // 配送方式
    private String deliveryMethod;
    
    // 备注
    private String remark;
    
    // 订单项列表
    private List<OrderItem> items;
    
    // 商品数量统计
    private Integer goodsCount;
    
    /**
     * 从Order实体转换
     */
    public static OrderVO fromOrder(Order order) {
        if (order == null) return null;
        
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setStatus(order.getStatus());
        vo.setPayAmount(order.getPayAmount());
        vo.setCreateTime(order.getCreateTime());
        vo.setPayTime(order.getPayTime());
        vo.setDeliveryTime(order.getDeliveryTime());
        vo.setCompleteTime(order.getCompleteTime());
        vo.setReceiverName(order.getReceiverName());
        vo.setReceiverPhone(order.getReceiverPhone());
        vo.setReceiverAddress(order.getReceiverAddress());
        vo.setDeliveryMethod(order.getDeliveryMethod());
        vo.setRemark(order.getRemark());
        
        // 设置状态文本
        vo.setStatusText(getStatusText(order.getStatus()));
        
        return vo;
    }
    
    /**
     * 获取状态文本
     */
    public static String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待付款";
            case 1: return "待配送";
            case 2: return "待收货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知";
        }
    }
}
