package com.weiwu.mapper;

import com.weiwu.pojo.vo.MyOrdersVO;
import com.weiwu.pojo.vo.MySubOrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersMapperCustom {

    /**
     * 查询用户订单信息
     * @param userId
     * @param orderStatus
     * @return
     */
    List<MyOrdersVO> queryMyOrders(@Param("userId") String userId,
                                   @Param("orderStatus") Integer orderStatus);

    /**
     * 根据订单ID 查询商品 订单item
     * @param orderId
     * @return
     */
    List<MySubOrderItemVO> getSubItems(@Param("orderId") String orderId);
}