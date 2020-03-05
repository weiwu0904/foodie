package com.weiwu.service;

import com.weiwu.enums.OrderStatusEnum;
import com.weiwu.pojo.OrderStatus;
import com.weiwu.pojo.bo.SubmitOrderBO;
import com.weiwu.pojo.vo.OrderVO;

public interface OrdersService {

    /**
     * 用于创建订单相关信息
     * @param orderBO
     */
    OrderVO createOrder(SubmitOrderBO orderBO);

    /**
     * 更新订单状态
     * @param orderId
     * @param orderStatus
     */
    void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    void closeOrder();

}
