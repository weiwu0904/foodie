package com.weiwu.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weiwu.mapper.OrdersMapperCustom;
import com.weiwu.pojo.vo.MyOrdersVO;
import com.weiwu.service.center.MyOrderService;
import com.weiwu.utils.PagedGridResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MyOrderServiceImpl  implements MyOrderService {

    @Resource
    private OrdersMapperCustom ordersMapperCustom;

    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> myOrdersVOList = ordersMapperCustom.queryMyOrders(userId, orderStatus);
        return setterGridPaged(myOrdersVOList, page);
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
