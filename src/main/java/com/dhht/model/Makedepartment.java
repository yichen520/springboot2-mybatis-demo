package com.dhht.model;

import java.util.Date;

public class Makedepartment {
    private String makedepartmentCode;

    private String password;

    private String departmentName;

    private String departmentNationName;

    private String departmentEnglishName;

    private String departmentEnglishAhhr;

    private String sealMakedepartmentStatus;

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

    private Date logoutTime;

    private String departmentStatus;

    public String getMakedepartmentCode() {
        return makedepartmentCode;
    }

    public void setMakedepartmentCode(String makedepartmentCode) {
        this.makedepartmentCode = makedepartmentCode == null ? null : makedepartmentCode.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    public String getDepartmentNationName() {
        return departmentNationName;
    }

    public void setDepartmentNationName(String departmentNationName) {
        this.departmentNationName = departmentNationName == null ? null : departmentNationName.trim();
    }

    public String getDepartmentEnglishName() {
        return departmentEnglishName;
    }

    public void setDepartmentEnglishName(String departmentEnglishName) {
        this.departmentEnglishName = departmentEnglishName == null ? null : departmentEnglishName.trim();
    }

    public String getDepartmentEnglishAhhr() {
        return departmentEnglishAhhr;
    }

    public void setDepartmentEnglishAhhr(String departmentEnglishAhhr) {
        this.departmentEnglishAhhr = departmentEnglishAhhr == null ? null : departmentEnglishAhhr.trim();
    }

    public String getSealMakedepartmentStatus() {
        return sealMakedepartmentStatus;
    }

    public void setSealMakedepartmentStatus(String sealMakedepartmentStatus) {
        this.sealMakedepartmentStatus = sealMakedepartmentStatus == null ? null : sealMakedepartmentStatus.trim();
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName == null ? null : legalName.trim();
    }

    public String getLegalId() {
        return legalId;
    }

    public void setLegalId(String legalId) {
        this.legalId = legalId == null ? null : legalId.trim();
    }

    public String getLegalIdType() {
        return legalIdType;
    }

    public void setLegalIdType(String legalIdType) {
        this.legalIdType = legalIdType == null ? null : legalIdType.trim();
    }

    public String getLegalEnglishsurname() {
        return legalEnglishsurname;
    }

    public void setLegalEnglishsurname(String legalEnglishsurname) {
        this.legalEnglishsurname = legalEnglishsurname == null ? null : legalEnglishsurname.trim();
    }

    public String getLegalEnglishname() {
        return legalEnglishname;
    }

    public void setLegalEnglishname(String legalEnglishname) {
        this.legalEnglishname = legalEnglishname == null ? null : legalEnglishname.trim();
    }

    public String getLegalTelphone() {
        return legalTelphone;
    }

    public void setLegalTelphone(String legalTelphone) {
        this.legalTelphone = legalTelphone == null ? null : legalTelphone.trim();
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }

    public void setDepartmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress == null ? null : departmentAddress.trim();
    }

    public String getDepartmentAddressDetail() {
        return departmentAddressDetail;
    }

    public void setDepartmentAddressDetail(String departmentAddressDetail) {
        this.departmentAddressDetail = departmentAddressDetail == null ? null : departmentAddressDetail.trim();
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode == null ? null : postalCode.trim();
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getDepartmentStatus() {
        return departmentStatus;
    }

    public void setDepartmentStatus(String departmentStatus) {
        this.departmentStatus = departmentStatus == null ? null : departmentStatus.trim();
    }
}