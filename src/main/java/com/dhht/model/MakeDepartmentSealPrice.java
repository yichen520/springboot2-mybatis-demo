package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class MakeDepartmentSealPrice {
    private String id;

    private String sealType;

    private String sealMaterial;

    private String sealPrice;

    private String makeDepartmentFlag;

    private String remarks;

    private Date updateTime;

    private Boolean isDelete;

    public MakeDepartmentSealPrice(String sealType, String makeDepartmentFlag) {
        this.sealType = sealType;
        this.makeDepartmentFlag = makeDepartmentFlag;
    }
    public MakeDepartmentSealPrice() {
    }
}