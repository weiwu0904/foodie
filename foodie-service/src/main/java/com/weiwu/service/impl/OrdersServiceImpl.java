package com.weiwu.service.impl;

import com.weiwu.enums.OrderStatusEnum;
import com.weiwu.enums.YesOrNo;
import com.weiwu.mapper.OrderItemsMapper;
import com.weiwu.mapper.OrderStatusMapper;
import com.weiwu.mapper.OrdersMapper;
import com.weiwu.pojo.*;
import com.weiwu.pojo.bo.SubmitOrderBO;
import com.weiwu.pojo.vo.MerchantOrdersVO;
import com.weiwu.pojo.vo.OrderVO;
import com.weiwu.service.AddressService;
import com.weiwu.service.ItemService;
import com.weiwu.service.OrdersService;
import org.aspectj.weaver.ast.Or;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private Sid sid;

    @Resource
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVO createOrder(SubmitOrderBO orderBO) {

        String userId = orderBO.getUserId();
        String addressId = orderBO.getAddressId();
        Integer payMethod = orderBO.getPayMethod();
        String leftMsg = orderBO.getLeftMsg();
        String itemSpecIds = orderBO.getItemSpecIds();

        // 快递费用
        Integer postAmout = 0;

        // string类型的订单ID
        String orderId = sid.nextShort();

        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);

        //1. 新订单数据保存
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);

        // 地址信息
        newOrder.setReceiverName(userAddress.getReceiver());
        newOrder.setReceiverMobile(userAddress.getMobile());

        String addressDetail = userAddress.getProvince() + ""
                + userAddress.getCity() + ""
                + userAddress.getDistrict()
                + userAddress.getDetail();
        newOrder.setReceiverAddress(addressDetail);

        newOrder.setPostAmount(postAmout);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        //2. 根据itemIds 保存 订单 商品item表

        String[] itemSpecIdsArr = itemSpecIds.split(",");

        Integer totalAmoud = 0;

        // 优惠后的实际要支付的价格累计
        Integer realPayAmoud = 0;

        // 遍历商品的具体规格信息，插入 订单 item 数据
        for (String specId : itemSpecIdsArr) {

            //TODO 整合redis后，商品购买的数量重新从redis中获取
            Integer buyCounts = 1;

            //2.1 根据规格ID查询规格的具体信息，主要是价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(specId);

            totalAmoud += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmoud += itemsSpec.getPriceDiscount() * buyCounts;

            //2.2 根据商品id 获取商品信息以及商品图片
            Items item = itemService.queryItemById(itemsSpec.getItemId());
            String imgUrl = itemService.queryItemMainImgById(itemsSpec.getItemId());

            //2.3 循环保存子订单(就是订单里的每一件商品 )数据 到数据库
            OrderItems subItem = new OrderItems();
            subItem.setId(sid.nextShort());
            subItem.setOrderId(orderId);
            subItem.setItemId(item.getId());
            subItem.setItemImg(imgUrl);
            subItem.setItemName(item.getItemName());
            subItem.setItemSpecId(itemsSpec.getId());
            subItem.setItemSpecName(itemsSpec.getName());
            subItem.setPrice(itemsSpec.getPriceDiscount());
            subItem.setBuyCounts(buyCounts);

            orderItemsMapper.insert(subItem);

            //2.4 在用户提交订单中，规格表中要扣减少库存
            itemService.decreaseItemSpecStock(specId,buyCounts);
        }

        newOrder.setTotalAmount(totalAmoud);
        newOrder.setRealPayAmount(realPayAmoud);

        ordersMapper.insert(newOrder);

        //3. 保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        //4. 构建商户的订单，传递给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmoud);
        merchantOrdersVO.setPayMethod(payMethod + postAmout);
       // merchantOrdersVO.setReturnUrl();// 在controller里设置

        //5. 构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer orderStatus) {

        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }
}
