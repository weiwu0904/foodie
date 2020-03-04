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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BaseController {

    public static final int COMMENT_PAGE_SIZE = 10;
}
