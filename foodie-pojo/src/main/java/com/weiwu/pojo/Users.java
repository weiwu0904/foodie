package com.weiwu.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;


@Data
public class Users {

    @Id
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String realname;
    private String face;
    private String mobile;
    private String email;

    private Integer sex;
    private Date birthday;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;
}