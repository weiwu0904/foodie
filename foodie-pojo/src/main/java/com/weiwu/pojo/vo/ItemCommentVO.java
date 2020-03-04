package com.weiwu.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户评价信息VO
 */
@Data
public class ItemCommentVO {

    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;

    private String userFace;
    private String nickname;
}
