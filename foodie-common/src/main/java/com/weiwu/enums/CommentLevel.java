package com.weiwu.enums;

import lombok.AllArgsConstructor;

/**
 * 商品评价等级枚举
 */
@AllArgsConstructor
public enum CommentLevel {

    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    public final Integer type;
    public final String value;
}
