package com.dhht.model;

import java.util.Date;

public class Incidence {
    private String id;

    private String serialCode;

    private String departmentCode;

    private String departmentName;

    private String incidenceId;

    private String incidenceType;

    private String incidenceCategory;

    private Date incidenceTime;

    private String incidenceDetail;

    private String districtId;

    private Date createTime;

    private String recorder;

    private String updateUser;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode == null ? null : serialCode.trim();
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode == null ? null : departmentCode.trim();
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    public String getIncidenceId() {
        return incidenceId;
    }

    public void setIncidenceId(String incidenceId) {
        this.incidenceId = incidenceId == null ? null : incidenceId.trim();
    }

    public String getIncidenceType() {
        return incidenceType;
    }

    public void setIncidenceType(String incidenceType) {
        this.incidenceType = incidenceType == null ? null : incidenceType.trim();
    }

    public String getIncidenceCategory() {
        return incidenceCategory;
    }

    public void setIncidenceCategory(String incidenceCategory) {
        this.incidenceCategory = incidenceCategory == null ? null : incidenceCategory.trim();
    }

    public Date getIncidenceTime() {
        return incidenceTime;
    }

    public void setIncidenceTime(Date incidenceTime) {
        this.incidenceTime = incidenceTime;
    }

    public String getIncidenceDetail() {
        return incidenceDetail;
    }

    public void setIncidenceDetail(String incidenceDetail) {
        this.incidenceDetail = incidenceDetail == null ? null : incidenceDetail.trim();
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId == null ? null : districtId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder == null ? null : recorder.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}