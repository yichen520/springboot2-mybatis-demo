package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class SealOperationRecord {
    private String id;

    private String sealCode;

    private Date dateTime; //操作时间

    private String operatorTelphone; //操作人手机号

    private String emplyeeName; //从业人员姓名

    private String employeeId; //从业人员身份证

    private String operatorName; //经办人姓名

    private String operatorCertificateCode; //经办人身份证

    private String operatorCertificateType; //身份类型

    private String  flag;//类型

}