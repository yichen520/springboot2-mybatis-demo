package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class RecordDepartment {

    private String id;

    private String departmentCode;

    private String departmentName;

    private String principalName;

    private String principalId;

    private String departmentAddress;

    private String departmentAddressDetail;

    private String telphone;

    private String postalCode;

    private Boolean isDelete;

    private int version;

    private String flag;

    private Date updateTime;


}
