package com.dhht.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Notify {
    private String id;

    private String sendUsername;

    private String sendRealname;

    private String notifyTitle;

    private String notifyContent;

    private Date createTime;

    private Boolean isRecall;

    private Date recallTime;

    private String districtId;

    private String notifyFileUrls;

    private String recallResult;

    private String notifyReadCount;

    private List<String> notifyUser;

    private List<String> fileIds;


}