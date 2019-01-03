package com.dhht.model;


import lombok.Data;

@Data
public class Courier {
    private String id;

    private String recipientsId;

    private String sealId;

    private String courierNo;

    private String courierType;

    private String postal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRecipientsId() {
        return recipientsId;
    }

    public void setRecipientsId(String recipientsId) {
        this.recipientsId = recipientsId == null ? null : recipientsId.trim();
    }

    public String getSealId() {
        return sealId;
    }

    public void setSealId(String sealId) {
        this.sealId = sealId == null ? null : sealId.trim();
    }

    public String getCourierNo() {
        return courierNo;
    }

    public void setCourierNo(String courierNo) {
        this.courierNo = courierNo == null ? null : courierNo.trim();
    }

    public String getCourierType() {
        return courierType;
    }

    public void setCourierType(String courierType) {
        this.courierType = courierType == null ? null : courierType.trim();
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal == null ? null : postal.trim();
    }
}