package com.weiwu.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weiwu.enums.OrderStatusEnum;
import com.weiwu.enums.YesOrNo;
import com.weiwu.mapper.OrderStatusMapper;
import com.weiwu.mapper.OrdersMapper;
import com.weiwu.mapper.OrdersMapperCustom;
import com.weiwu.pojo.OrderStatus;
import com.weiwu.pojo.Orders;
import com.weiwu.pojo.vo.MyOrdersVO;
import com.weiwu.service.center.MyOrderService;
import com.weiwu.utils.PagedGridResult;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MyOrderServiceImpl  implements MyOrderService {

    @Resource
    private OrdersMapperCustom ordersMapperCustom;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> myOrdersVOList = ordersMapperCustom.queryMyOrders(userId, orderStatus);
        return setterGridPaged(myOrdersVOList, page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus updateOderStatus = new OrderStatus();
        updateOderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECIVE.type);
        updateOderStatus.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("orderStatus",OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateOderStatus,example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setIsDelete(YesOrNo.NO.type);

        return ordersMapper.selectOne(orders);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateReceiveOrderstatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("orderStatus",OrderStatusEnum.WAIT_RECIVE.type);

        int result = orderStatusMapper.updateByExampleSelective(orderStatus, example);

        return result == 1;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteOrder(String userId, String orderId) {

        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("userId",userId);
        int result = ordersMapper.updateByExampleSelective(orders, example);
        return result == 1;
    }

    private PagedGridResult setterGridPaged(List<?> list, int page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRecords(pageInfo.getTotal());
        pagedGridResult.setRows(list);
        return pagedGridResult;
    }
}
