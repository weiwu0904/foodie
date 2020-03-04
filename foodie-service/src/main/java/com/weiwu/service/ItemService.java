package com.weiwu.service;

import com.weiwu.pojo.Items;
import com.weiwu.pojo.ItemsImg;
import com.weiwu.pojo.ItemsParam;
import com.weiwu.pojo.ItemsSpec;
import com.weiwu.pojo.vo.CommentLevelCountVO;
import com.weiwu.pojo.vo.ItemCommentVO;
import com.weiwu.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品ID查询详情
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);


    /**
     * 根据商品ID查询商品图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品ID查询商品规格列表
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品ID查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemsParam(String itemId);


    /**
     * 根据商品ID查询评价等级数量
     * @param itemId
     */
    CommentLevelCountVO queryCommentCounts(String itemId);

    /**
     * 查询商品评价列表
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level,
                                                      Integer page, Integer pageSize);

}
