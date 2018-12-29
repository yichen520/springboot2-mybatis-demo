package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class AppReportLog {
    private String id;

    private Date createtime;

    private String deviceInfo;

    private String userId;

    private String reportlog;

    private String appVersion;

    private String systemVersion;

    private String remark;

}