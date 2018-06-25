package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String id;

    private String userName;

    private String realName;

    private String password;

    private String roleId;

    private String objectId;

    private Boolean isLocked;

    private Integer loginErrorTimes;

    private Boolean isChangedPwd;

    private Boolean isDeleted;

    private String districtId;

    private String telphone;

    private Date loginTime;

    private District district;

    private Role role;

//    public String getTelphone() {
//        return telphone;
//    }
//
//    public void setTelphone(String telphone) {
//        this.telphone = telphone;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getRealName() {
//        return realName;
//    }
//
//    public void setRealName(String realName) {
//        this.realName = realName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(String roleId) {
//        this.roleId = roleId;
//    }
//
//    public String getObjectId() {
//        return objectId;
//    }
//
//    public void setObjectId(String objectId) {
//        this.objectId = objectId;
//    }
//
//    public Boolean getLocked() {
//        return isLocked;
//    }
//
//    public void setLocked(Boolean locked) {
//        isLocked = locked;
//    }
//
//    public Integer getLoginErrorTimes() {
//        return loginErrorTimes;
//    }
//
//    public void setLoginErrorTimes(Integer loginErrorTimes) {
//        this.loginErrorTimes = loginErrorTimes;
//    }
//
//    public Boolean getChangedPwd() {
//        return isChangedPwd;
//    }
//
//    public void setChangedPwd(Boolean changedPwd) {
//        isChangedPwd = changedPwd;
//    }
//
//    public Boolean getDeleted() {
//        return isDeleted;
//    }
//
//    public void setDeleted(Boolean deleted) {
//        isDeleted = deleted;
//    }
//
//
//    public District getDistrict() {
//        return district;
//    }
//
//    public void setDistrict(District district) {
//        this.district = district;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }

}