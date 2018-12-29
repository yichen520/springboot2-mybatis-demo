package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class MakeDepartmentAttachInfo {
    private String id;

    private String bankAccount;

    private String bankName;

    private String bankAddress;

    private String latitude;

    private String longitude;

    private String addressDetail;

    private String promiseTime;

    private String remarks;

    private Date updateTime;

    private Boolean isDelete;

    private String makeDepartmentFlag;


}