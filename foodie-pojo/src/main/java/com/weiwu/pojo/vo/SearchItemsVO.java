package com.weiwu.pojo.vo;

import lombok.Data;

/**
 * 搜索商品的列表的VO
 */
@Data
public class SearchItemsVO {

    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private int price;  // 几种规格中的最低价, 以分为单位的钱
}
