package com.dhht.model;

import java.util.Date;

public class SealVerification {
    private String id;

    private String sealId;

    private Boolean isVerification;

    private String verifyTypeName;

    private String rejectReason;

    private String rejectRemark;

    private Date verificationDate;

    private Boolean isReuploadData;

    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSealId() {
        return sealId;
    }

    public void setSealId(String sealId) {
        this.sealId = sealId == null ? null : sealId.trim();
    }

    public Boolean getIsVerification() {
        return isVerification;
    }

    public void setIsVerification(Boolean isVerification) {
        this.isVerification = isVerification;
    }

    public String getVerifyTypeName() {
        return verifyTypeName;
    }

    public void setVerifyTypeName(String verifyTypeName) {
        this.verifyTypeName = verifyTypeName == null ? null : verifyTypeName.trim();
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
    }

    public String getRejectRemark() {
        return rejectRemark;
    }

    public void setRejectRemark(String rejectRemark) {
        this.rejectRemark = rejectRemark == null ? null : rejectRemark.trim();
    }

    public Date getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Date verificationDate) {
        this.verificationDate = verificationDate;
    }

    public Boolean getIsReuploadData() {
        return isReuploadData;
    }

    public void setIsReuploadData(Boolean isReuploadData) {
        this.isReuploadData = isReuploadData;
    }
}