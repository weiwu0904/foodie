package com.weiwu.controller;

import com.weiwu.pojo.Items;
import com.weiwu.pojo.ItemsImg;
import com.weiwu.pojo.ItemsParam;
import com.weiwu.pojo.ItemsSpec;
import com.weiwu.pojo.vo.CommentLevelCountVO;
import com.weiwu.pojo.vo.ItemInfoVO;
import com.weiwu.service.ItemService;
import com.weiwu.utils.IMOOCJSONResult;
import com.weiwu.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品详情页
 */
@RestController
@RequestMapping("/items")
@Api(value = "商品展示",tags = {"商品相关信息展示"})
public class ItemsController extends BaseController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/info/{itemId}")
    @ApiOperation(value = "查询商品详情", notes = "查询商品详情",httpMethod = "GET")
    public IMOOCJSONResult carousel(@ApiParam(name = "itemId", value = "商品ID")
                                        @PathVariable String itemId) {

        if (itemId == null || itemId.equals("")) {
            return IMOOCJSONResult.errorMsg("商品ID不能为空");
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemsParam(itemId);

        ItemInfoVO infoVO = new ItemInfoVO();
        infoVO.setItem(item);
        infoVO.setItemImgList(itemImgList);
        infoVO.setItemSpecList(itemSpecList);
        infoVO.setItemParams(itemsParam);
        return IMOOCJSONResult.ok(infoVO);
    }

    @GetMapping("/commentLevel")
    @ApiOperation(value = "查询商品评价等级数量", notes = "查询商品评价等级数量",httpMethod = "GET")
    public IMOOCJSONResult comments(@RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg("商品ID不能为空");
        }
        CommentLevelCountVO levelCountVO = itemService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(levelCountVO);
    }

    @GetMapping("/comments")
    @ApiOperation(value = "查询商品评论列表", notes = "查询商品评论列表",httpMethod = "GET")
    public IMOOCJSONResult commentLevel(@RequestParam String itemId,
                                        @RequestParam Integer level,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg("商品ID不能为空");
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = itemService.queryPagedComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }
}
