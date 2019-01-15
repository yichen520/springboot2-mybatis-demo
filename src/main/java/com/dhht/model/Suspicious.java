package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class Suspicious {
    private String id;

    private String makeDepartmentCode;

    private String makeDepartmentName;

    private String employeeCode;

    private String employeeName;

    private String employeeIdcard;

    private String suspiciousType;

    private Date suspiciousTime;

    private String suspiciousDetail;

    private String recorder;

    private Date createTime;

    private String districtId;

    private String updateUser;

    private Date updateTime;

    private String loginTelphone;


}