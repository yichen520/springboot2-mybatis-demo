package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class SealOperationRecord {
    private String id;

    private String sealId;

    private String employeeId;

    private String employeeName;

    private String employeeCode;

    private String operateType;

    private Date operateTime;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id == null ? null : id.trim();
//    }
//
//    public String getSealId() {
//        return sealId;
//    }
//
//    public void setSealId(String sealId) {
//        this.sealId = sealId == null ? null : sealId.trim();
//    }
//
//    public String getEmployeeId() {
//        return employeeId;
//    }
//
//    public void setEmployeeId(String employeeId) {
//        this.employeeId = employeeId == null ? null : employeeId.trim();
//    }
//
//    public String getEmplyeeName() {
//        return employee_name;
//    }
//
//    public void setEmplyeeName(String employee_name) {
//        this.employee_name = employee_name == null ? null : employee_name.trim();
//    }
//
//    public String getEmployeeCode() {
//        return employeeCode;
//    }
//
//    public void setEmployeeCode(String employeeCode) {
//        this.employeeCode = employeeCode == null ? null : employeeCode.trim();
//    }
//
//    public String getOperateType() {
//        return operateType;
//    }
//
//    public void setOperateType(String operateType) {
//        this.operateType = operateType == null ? null : operateType.trim();
//    }
//
//    public Date getOperateTime() {
//        return operateTime;
//    }
//
//    public void setOperateTime(Date operateTime) {
//        this.operateTime = operateTime;
//    }
}