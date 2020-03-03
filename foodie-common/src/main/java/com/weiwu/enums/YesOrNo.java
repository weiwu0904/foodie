package com.weiwu.enums;

import lombok.AllArgsConstructor;

/**
 * 性别，枚举
 */
@AllArgsConstructor
public enum YesOrNo {

    NO(0, "否"),
    YES(1, "是");

    public final Integer type;
    public final String value;
}
