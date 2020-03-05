package com.weiwu.service.center;

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
}
