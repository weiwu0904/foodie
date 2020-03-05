package com.weiwu.service.center;

import com.weiwu.pojo.Orders;
import com.weiwu.utils.PagedGridResult;

public interface MyOrderService {

    /**
     * 查询用户订单信息
     *
     * @param userId
     * @param orderStatus
     * @return
     */
    PagedGridResult queryMyOrders(String userId,
                                  Integer orderStatus,
                                  Integer page,
                                  Integer pageSize);

    /**
     * 修改订单状态为已发货
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);


    /**
     * 查询我的订单
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 更新订单状态--> 确认收货
     * @param orderId
     * @return
     */
    boolean updateReceiveOrderstatus(String orderId);

    /**
     * 逻辑删除订单
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId, String orderId);
}
