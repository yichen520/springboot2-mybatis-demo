package com.dhht.model;

import java.util.Date;

public class ChipApply {
    private String id;

    private Integer chipNum;

    private Date getTime;

    private String address;

    private String makeDepartmentCode;

    private String makeDepartmentName;

    private Date applyTime;

    private String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getChipNum() {
        return chipNum;
    }

    public void setChipNum(Integer chipNum) {
        this.chipNum = chipNum;
    }

    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}