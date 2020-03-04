package com.weiwu.service;

import com.weiwu.pojo.bo.SubmitOrderBO;

public interface OrdersService {

    /**
     * 用于创建订单相关信息
     * @param orderBO
     */
    void createOrder(SubmitOrderBO orderBO);


}
