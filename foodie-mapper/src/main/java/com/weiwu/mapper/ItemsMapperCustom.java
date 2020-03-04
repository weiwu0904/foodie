package com.weiwu.mapper;

import com.weiwu.my.mapper.MyMapper;
import com.weiwu.pojo.Items;
import com.weiwu.pojo.vo.ItemCommentVO;
import com.weiwu.pojo.vo.SearchItemsVO;
import com.weiwu.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsMapperCustom {

    /**
     * 根据商品ID和评价等级查询Item详情页评价信息
     * @param itemId
     * @param level
     * @return
     */
    List<ItemCommentVO> queryItemComments(@Param("itemId") String itemId, @Param("level") Integer level);

    /**
     * 搜索商品
     * @param keywords 搜索关键字
     * @param sort     排序规则  K: 名称排序 C: 销量排序 P: 价格排序
     * @return
     */
    List<SearchItemsVO> searchItems(@Param("keywords") String keywords, @Param("sort") String sort);

    /**
     * 根据三级分类ID搜索商品
     * @param catId
     * @param sort
     * @return
     */
    List<SearchItemsVO> searchItemsByThirdCat(@Param("catId") Integer catId, @Param("sort") String sort);

    /**
     * 根据规格ID数组查询购物车商品信息
     * @param specIds
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(@Param("specIds") List<String> specIds);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     * @return
     */
    int decreaseItemSpecStock(@Param("specId") String specId,
                              @Param("buyCounts") Integer buyCounts);

}