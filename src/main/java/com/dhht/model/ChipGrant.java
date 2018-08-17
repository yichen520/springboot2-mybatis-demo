package com.dhht.model;

import java.util.Date;

public class ChipGrant {

    private String id;

    private String chipApplyId;

    private String chipCodeStart;

    private String chipCodeEnd;

    private String receiver;

    private Integer grantNum;

    private Date grantTime;

    private String granterId;

    private String granter;

    private String grantWay;

    private String memo;

    private Date recordTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getChipApplyId() {
        return chipApplyId;
    }

    public void setChipApplyId(String chipApplyId) {
        this.chipApplyId = chipApplyId == null ? null : chipApplyId.trim();
    }

    public String getChipCodeStart() {
        return chipCodeStart;
    }

    public void setChipCodeStart(String chipCodeStart) {
        this.chipCodeStart = chipCodeStart == null ? null : chipCodeStart.trim();
    }

    public String getChipCodeEnd() {
        return chipCodeEnd;
    }

    public void setChipCodeEnd(String chipCodeEnd) {
        this.chipCodeEnd = chipCodeEnd == null ? null : chipCodeEnd.trim();
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public Integer getGrantNum() {
        return grantNum;
    }

    public void setGrantNum(Integer grantNum) {
        this.grantNum = grantNum;
    }

    public Date getGrantTime() {
        return grantTime;
    }

    public void setGrantTime(Date grantTime) {
        this.grantTime = grantTime;
    }

    public String getGranterId() {
        return granterId;
    }

    public void setGranterId(String granterId) {
        this.granterId = granterId == null ? null : granterId.trim();
    }

    public String getGranter() {
        return granter;
    }

    public void setGranter(String granter) {
        this.granter = granter == null ? null : granter.trim();
    }

    public String getGrantWay() {
        return grantWay;
    }

    public void setGrantWay(String grantWay) {
        this.grantWay = grantWay == null ? null : grantWay.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }
}