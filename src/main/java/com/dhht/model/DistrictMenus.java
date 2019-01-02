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

    //另一个方法的参数
    private String id;
    private String label;



    public DistrictMenus() {
    }

    public DistrictMenus(String districtId, String districtName, int type, String parentId) {
        DistrictId = districtId;
        DistrictName = districtName;
        this.type = type;
        ParentId = parentId;
    }

    public DistrictMenus(String id, String label,String parentId) {
        this.DistrictId = id;
        this.id = id;
        this.label = label;
        this.ParentId = parentId;
    }
}
