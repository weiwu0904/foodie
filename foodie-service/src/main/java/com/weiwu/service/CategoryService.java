package com.weiwu.service;

import com.weiwu.pojo.Carousel;
import com.weiwu.pojo.Category;
import com.weiwu.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有一级大分类
     * @return
     */
    List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类ID 查询子分类信息
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);
}
