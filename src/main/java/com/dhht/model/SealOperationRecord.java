package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class SealOperationRecord {

    private String id;

    private String sealId;  //印章id

    private String employeeId;  //从业人员id

    private String emplyeeName;  //从业人员名字

    private String employeeCode;  //从业人员code

    private String operateType;   //操作类型

    private Date operateTime; //操作时间


}