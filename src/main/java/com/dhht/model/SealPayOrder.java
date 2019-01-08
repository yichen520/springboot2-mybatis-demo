package com.dhht.model;

import java.util.Date;

public class SealPayOrder {
    private String id;

    private String sealId;

    private Boolean isPay;

    private String payWay;

    private String payAccout;

    private Date payDate;

    private Boolean expressWay;

    private String courierId;

    private String memo;

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

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay == null ? null : payWay.trim();
    }

    public String getPayAccout() {
        return payAccout;
    }

    public void setPayAccout(String payAccout) {
        this.payAccout = payAccout == null ? null : payAccout.trim();
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Boolean getExpressWay() {
        return expressWay;
    }

    public void setExpressWay(Boolean expressWay) {
        this.expressWay = expressWay;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId == null ? null : courierId.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}