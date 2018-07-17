package com.dhht.model;

import java.util.List;

public class Examine {
    private String id;

    private String name;

    private String remark;

    private String districtId;

    private List<ExamineDetail> examineDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId == null ? null : districtId.trim();
    }

    public List<ExamineDetail> getExamineDetails() {
        return examineDetails;
    }

    public void setExamineDetails(List<ExamineDetail> examineDetails) {
        this.examineDetails = examineDetails;
    }
}