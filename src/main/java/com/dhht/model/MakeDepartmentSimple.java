package com.dhht.model;

import lombok.Data;

@Data
public class MakeDepartmentSimple {
    private String id;
    private String departmentCode;
    private String departmentName;
    private String departmentType;
    private String legalName;
    private String departmentAddressDetail;
    private String telphone;
    private String postalCode;
    private String departmentStatus;
    private int version;
    private String flag;
}
