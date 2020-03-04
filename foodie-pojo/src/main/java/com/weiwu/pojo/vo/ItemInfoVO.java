package com.weiwu.pojo.vo;

import com.weiwu.pojo.Items;
import com.weiwu.pojo.ItemsImg;
import com.weiwu.pojo.ItemsParam;
import com.weiwu.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 * 商品详情VO
 */
@Data
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
