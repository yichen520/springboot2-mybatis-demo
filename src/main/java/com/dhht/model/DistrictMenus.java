package com.dhht.model;

import java.util.List;

public class DistrictMenus {
    private Integer DistrictId;
    private String DistrictName;
    private String ParentId;
    private List<DistrictMenus> children;

    public DistrictMenus(Integer districtId, String districtName, String parentId) {
        DistrictId = districtId;
        DistrictName = districtName;
        ParentId = parentId;
    }

    public DistrictMenus() {
    }

    public Integer getDistrictId() {
        return DistrictId;
    }

    public void setDistrictId(Integer districtId) {
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
                ", child=" + children +
                '}';
    }
}
