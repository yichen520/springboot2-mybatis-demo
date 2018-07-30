package com.dhht.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Notice {
    private String id;

    private String sendUsername;

    private String sendRealname;

    private String noticeTitle;

    private String noticeContent;

    private Date createTime;

    private String districtId;

    private String noticeFileUrls;

    private List<File> files;


}