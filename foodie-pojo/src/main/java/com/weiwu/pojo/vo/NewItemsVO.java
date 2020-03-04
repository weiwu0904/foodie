package com.weiwu.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 首页最新推荐商品VO
 */
@Data
public class NewItemsVO {

    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;
    /**
     * 三级分类VO列表
     */
    List<SimpleItemVO> simpleItemList;
}
