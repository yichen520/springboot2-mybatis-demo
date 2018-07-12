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

}