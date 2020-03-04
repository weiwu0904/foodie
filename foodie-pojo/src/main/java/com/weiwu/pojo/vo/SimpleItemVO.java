package com.weiwu.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 6个最新商品的简单数据类型
 */
@Data
public class SimpleItemVO {

    private String itemId;
    private String itemName;
    private String itemUrl;
    private Date createdTime;
}
