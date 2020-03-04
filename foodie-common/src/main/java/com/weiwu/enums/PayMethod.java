package com.weiwu.enums;

import lombok.AllArgsConstructor;

/**
 * 性别，枚举
 */
@AllArgsConstructor
public enum PayMethod {

    WECHAT(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;
}
