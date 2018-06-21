package com.dhht.model;

public class SealKey {
    private String id;

    private String sealCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSealCode() {
        return sealCode;
    }

    public void setSealCode(String sealCode) {
        this.sealCode = sealCode == null ? null : sealCode.trim();
    }
}