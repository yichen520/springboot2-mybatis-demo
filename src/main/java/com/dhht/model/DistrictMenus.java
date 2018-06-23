package com.dhht.model;

import java.util.List;

public class DistrictMenus {
    private String DistrictId;
    private String DistrictName;
    private String ParentId;
    private List<DistrictMenus> children;


    public DistrictMenus() {
    }

    public DistrictMenus(String districtId, String districtName, String parentId) {
        DistrictId = districtId;
        DistrictName = districtName;
        ParentId = parentId;
    }

    public String getDistrictId() {
        return DistrictId;
    }

    public void setDistrictId(String districtId) {
        DistrictId = districtId;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public List<DistrictMenus> getChildren() {
        return children;
    }

    public void setChildren(List<DistrictMenus> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "DistrictMenus{" +
                "DistrictId=" + DistrictId +
                ", DistrictName='" + DistrictName + '\'' +
                ", ParentId='" + ParentId + '\'' +
                ", children=" + children +
                '}';
    }
}
