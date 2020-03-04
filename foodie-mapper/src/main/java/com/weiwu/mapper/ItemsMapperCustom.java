package com.weiwu.mapper;

import com.weiwu.my.mapper.MyMapper;
import com.weiwu.pojo.Items;
import com.weiwu.pojo.vo.ItemCommentVO;
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
}