package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeSimple {
    private String id;
    private String noticeTitle;
    private String sendRealname;
    private Date createTime;
}
