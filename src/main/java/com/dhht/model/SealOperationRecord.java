package com.dhht.model;

import lombok.Data;

import java.sql.Date;

@Data
public class SealOperationRecord extends SealKey {

    private Date dateTime; //操作时间

    private String operatorTelphone; //操作人手机号

//    private String operationType; //操作类型
//
//    private String operationState; //操作状态

    private String emplyeeName; //从业人员姓名

    private String employeeId; //从业人员身份证

    private String operatorName; //经办人姓名

    private String operatorCertificateCode; //经办人身份证

    private String operatorCrtificateType; //身份类型

    private String  flag;//类型

}