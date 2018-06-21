package com.dhht.model;

import java.util.Date;

public class Employee {
    private String employeeCode;

    private String employeeName;

    private String employeeId;

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

    public Employee() {
    }

    public String getEmployeeDepartmentCode() {
        return employeeDepartmentCode;
    }

    public void setEmployeeDepartmentCode(String employeeDepartmentCode) {
        this.employeeDepartmentCode = employeeDepartmentCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode == null ? null : employeeCode.trim();
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName == null ? null : employeeName.trim();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId == null ? null : employeeId.trim();
    }

    public String getEmployeeJob() {
        return employeeJob;
    }

    public void setEmployeeJob(String employeeJob) {
        this.employeeJob = employeeJob == null ? null : employeeJob.trim();
    }

    public String getEmployeeNation() {
        return employeeNation;
    }

    public void setEmployeeNation(String employeeNation) {
        this.employeeNation = employeeNation == null ? null : employeeNation.trim();
    }

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress == null ? null : familyAddress.trim();
    }

    public String getFamilyAddressDetail() {
        return familyAddressDetail;
    }

    public void setFamilyAddressDetail(String familyAddressDetail) {
        this.familyAddressDetail = familyAddressDetail == null ? null : familyAddressDetail.trim();
    }

    public String getNowAddress() {
        return nowAddress;
    }

    public void setNowAddress(String nowAddress) {
        this.nowAddress = nowAddress == null ? null : nowAddress.trim();
    }

    public String getNowAddressDetail() {
        return nowAddressDetail;
    }

    public void setNowAddressDetail(String nowAddressDetail) {
        this.nowAddressDetail = nowAddressDetail == null ? null : nowAddressDetail.trim();
    }

    public String getEmployeeImage() {
        return employeeImage;
    }

    public void setEmployeeImage(String employeeImage) {
        this.employeeImage = employeeImage == null ? null : employeeImage.trim();
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContactTelphone() {
        return contactTelphone;
    }

    public void setContactTelphone(String contactTelphone) {
        this.contactTelphone = contactTelphone == null ? null : contactTelphone.trim();
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode == null ? null : officeCode.trim();
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName == null ? null : officeName.trim();
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName == null ? null : registerName.trim();
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getLogoutOfficeCode() {
        return logoutOfficeCode;
    }

    public void setLogoutOfficeCode(String logoutOfficeCode) {
        this.logoutOfficeCode = logoutOfficeCode == null ? null : logoutOfficeCode.trim();
    }

    public String getLogoutOfficeName() {
        return logoutOfficeName;
    }

    public void setLogoutOfficeName(String logoutOfficeName) {
        this.logoutOfficeName = logoutOfficeName == null ? null : logoutOfficeName.trim();
    }

    public String getLogoutName() {
        return logoutName;
    }

    public void setLogoutName(String logoutName) {
        this.logoutName = logoutName == null ? null : logoutName.trim();
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }
}