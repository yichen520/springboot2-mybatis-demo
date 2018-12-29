package com.dhht.model;

import lombok.Data;

@Data
public class SealCode {
    private String recordDepartmentCode;
    private String districtId;
    private String sealCode;
    private String districtName;

    public SealCode(){

    }

    public SealCode(String districtId, String sealCode, String districtName) {
        this.districtId = districtId;
        this.sealCode = sealCode;
        this.districtName = districtName;
    }
}
