package com.weiwu.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于展示商品评价数的VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLevelCountVO {

    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
