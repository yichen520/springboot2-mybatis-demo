package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class WeChatUser {
    private String id;

    private String name;

    private String gender;

    private String telphone;

    private String company;

    private String companyName;

    private String job;

    private Date createTime;

    private String image;

     private boolean isCompanyAccout;

}