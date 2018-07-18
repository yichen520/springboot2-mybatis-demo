package com.dhht.model;

import java.util.Date;

public class EmployeePunishRecord {
    private String id;

    private String makeDepartmentCode;

    private String makeDepartmentName;

    private String employeeCode;

    private String employeeName;

    private String employeeIdcardNo;

    private String punishReason;

    private String punishBasis;

    private String punishWay;

    private String punishAddress;

    private String recordDepartmentCode;

    private String recordDepartmentName;

    private String punisherName;

    private Date punishTime;

    private String districtId;

    private Float longitude;

    private Float latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMakeDepartmentCode() {
        return makeDepartmentCode;
    }

    public void setMakeDepartmentCode(String makeDepartmentCode) {
        this.makeDepartmentCode = makeDepartmentCode == null ? null : makeDepartmentCode.trim();
    }

    public String getMakeDepartmentName() {
        return makeDepartmentName;
    }

    public void setMakeDepartmentName(String makeDepartmentName) {
        this.makeDepartmentName = makeDepartmentName == null ? null : makeDepartmentName.trim();
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

    public String getEmployeeIdcardNo() {
        return employeeIdcardNo;
    }

    public void setEmployeeIdcardNo(String employeeIdcardNo) {
        this.employeeIdcardNo = employeeIdcardNo == null ? null : employeeIdcardNo.trim();
    }

    public String getPunishReason() {
        return punishReason;
    }

    public void setPunishReason(String punishReason) {
        this.punishReason = punishReason == null ? null : punishReason.trim();
    }

    public String getPunishBasis() {
        return punishBasis;
    }

    public void setPunishBasis(String punishBasis) {
        this.punishBasis = punishBasis == null ? null : punishBasis.trim();
    }

    public String getPunishWay() {
        return punishWay;
    }

    public void setPunishWay(String punishWay) {
        this.punishWay = punishWay == null ? null : punishWay.trim();
    }

    public String getPunishAddress() {
        return punishAddress;
    }

    public void setPunishAddress(String punishAddress) {
        this.punishAddress = punishAddress == null ? null : punishAddress.trim();
    }

    public String getRecordDepartmentCode() {
        return recordDepartmentCode;
    }

    public void setRecordDepartmentCode(String recordDepartmentCode) {
        this.recordDepartmentCode = recordDepartmentCode == null ? null : recordDepartmentCode.trim();
    }

    public String getRecordDepartmentName() {
        return recordDepartmentName;
    }

    public void setRecordDepartmentName(String recordDepartmentName) {
        this.recordDepartmentName = recordDepartmentName == null ? null : recordDepartmentName.trim();
    }

    public String getPunisherName() {
        return punisherName;
    }

    public void setPunisherName(String punisherName) {
        this.punisherName = punisherName == null ? null : punisherName.trim();
    }

    public Date getPunishTime() {
        return punishTime;
    }

    public void setPunishTime(Date punishTime) {
        this.punishTime = punishTime;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId == null ? null : districtId.trim();
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
}