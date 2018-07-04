package com.dhht.model;

import lombok.Data;

import java.util.List;
@Data
public class DistrictMenus {
    private String DistrictId;
    private String DistrictName;
    private int type;
    private String ParentId;
    private List<DistrictMenus> children;
    private List<MakeDepartmentSimple> makeDepartmentSimples;


    public DistrictMenus() {
    }

    public DistrictMenus(String districtId, String districtName, int type, String parentId) {
        DistrictId = districtId;
        DistrictName = districtName;
        this.type = type;
        ParentId = parentId;
    }

}
