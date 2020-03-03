package com.weiwu.mapper;

import com.weiwu.my.mapper.MyMapper;
import com.weiwu.pojo.Category;
import com.weiwu.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryMapperCustom {
    /**
     * 查询二、三级分类的信息
     * @param rootCatid
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatid);
}