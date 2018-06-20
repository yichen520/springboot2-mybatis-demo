package com.dhht.model;

public class UserSimple {
    private String Id;
    private String UserName;
    private String RealName;
    private String RoleId;
    private boolean IsLock;
    private String RegionId;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getRegionId() {
        return RegionId;
    }

    public void setRegionId(String regionId) {
        RegionId = regionId;
    }

    public boolean getIsLock() {
        return IsLock;
    }

    public void setIsLock(boolean islock) {
        IsLock = islock;
    }

    @Override
    public String toString() {
        return "UserSimple{" +
                "Id='" + Id + '\'' +
                ", UserName='" + UserName + '\'' +
                ", RealName='" + RealName + '\'' +
                ", RoleId='" + RoleId + '\'' +
                ", IsLock=" + IsLock +
                ", RegionId='" + RegionId + '\'' +
                '}';
    }
}
