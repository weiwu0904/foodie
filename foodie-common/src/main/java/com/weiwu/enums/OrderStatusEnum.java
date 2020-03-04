package com.weiwu.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum OrderStatusEnum {

    WAIT_PAY(10, "待付款"),
    WAIT_DELIVER(20, "待发货"),
    WAIT_RECIVE(30, "待收货"),
    SUCCESS(40, "交易成功"),
    CLOSE(50, "交易关闭");

    public final Integer type;
    public final String value;
}
