package com.dhht.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Makedepartment {
    private String id;
    private String departmentCode;
    private String departmentName;
    private String departmentNationName;
    private String departmentEnglishName;
    private String departmentEnglishAhhr;
    private String departmentType;
    private String legalName;
    private String legalId;
    private String legalIdType;
    private String legalEnglishsurname;
    private String legalEnglishname;
    private String legalTelphone;
    private String departmentAddress;
    private String departmentAddressDetail;
    private String telphone;
    private String postalCode;
    private String departmentStatus;
    private int version;
    private String flag;
    private Date versionTime;
    private Date registerTime;

    private MakeDepartmentPunish makeDepartmentPunish;
    private List<Employee> employeeList;
    }