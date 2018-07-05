package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class Employee {
    private String id;

    private String employeeCode;

    private String employeeName;

    private String employeeId;

    private String employeeGender;

    private String employeeJob;

    private String employeeDepartmentCode;

    private String employeeNation;

    private String familyAddress;

    private String familyAddressDetail;

    private String nowAddress;

    private String nowAddressDetail;

    private String employeeImage;

    private String telphone;

    private String contactName;

    private String contactTelphone;

    private String officeCode;

    private String officeName;

    private String registerName;

    private Date registerTime;

    private String logoutOfficeCode;

    private String logoutOfficeName;

    private String logoutName;

    private Date logoutTime;

    private Date effectiveTime;

    private int version;

    private Date versionTime;

    private String flag;

    private boolean isDelete;

    public Employee() {
    }


}