package com.weiwu.controller;

import com.weiwu.enums.YesOrNo;
import com.weiwu.pojo.Carousel;
import com.weiwu.pojo.Category;
import com.weiwu.pojo.vo.CategoryVO;
import com.weiwu.pojo.vo.NewItemsVO;
import com.weiwu.service.CarouselService;
import com.weiwu.service.CategoryService;
import com.weiwu.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/index")
@Api(value = "首页",tags = {"首页展示的相关接口"})
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/carousel")
    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表",httpMethod = "GET")
    public IMOOCJSONResult carousel() {
        List<Carousel> list = carouselService.queryAll(YesOrNo.YES.type);
        return IMOOCJSONResult.ok(list);
    }

    @GetMapping("/cats")
    @ApiOperation(value = "用于获取商品一级分类", notes = "用于获取商品一级分类",httpMethod = "GET")
    public IMOOCJSONResult cats() {
        List<Category> list = categoryService.queryAllRootLevelCat();
        return IMOOCJSONResult.ok(list);
    }


    // 根据商品的父级分类获取子分类
    @GetMapping("/subCat/{rootCatId}")
    @ApiOperation(value = "用于获取商品子分类", notes = "用于获取商品子分类",httpMethod = "GET")
    public IMOOCJSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类ID",required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        return IMOOCJSONResult.ok(list);
    }

    // 根据商品的父级分类获取子分类
    @GetMapping("/sixNewItems/{rootCatId}")
    @ApiOperation(value = "查询每个一级分类最新6条商品数据", notes = "查询每个一级分类最新6条商品数据",httpMethod = "GET")
    public IMOOCJSONResult sixNewItems(@PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        // 页面是懒加载，其实这个list每次只有1个元素
        List<NewItemsVO> list = categoryService.getSixNewItemLazy(rootCatId);
        return IMOOCJSONResult.ok(list);
    }
}
