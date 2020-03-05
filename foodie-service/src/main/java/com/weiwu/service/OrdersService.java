package com.weiwu.service;

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

}
