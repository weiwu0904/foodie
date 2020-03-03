package com.weiwu.enums;

import lombok.AllArgsConstructor;

/**
 * 性别，枚举
 */
@AllArgsConstructor
public enum Sex {

    WOMAN(0, "女"),
    MAN(1, "男"),
    SECRET(2, "保密");

    public final Integer type;
    public final String value;
}
