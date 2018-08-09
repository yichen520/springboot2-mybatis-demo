package com.dhht.model.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class RecordDepartmentHistoryVO {
    private String departmentCode;

    private String departmentName;

    private String principalName;

    private String departmentAddressDetail;

    private String telphone;

    private String postalCode;

    private Date updateTime;

    private String operator;


}
