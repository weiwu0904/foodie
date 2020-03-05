package com.weiwu.pojo.vo;

import lombok.Data;

@Data
public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
}