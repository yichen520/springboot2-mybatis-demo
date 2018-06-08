package com.dhht.model;

import lombok.Data;

@Data
public class Users {
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

    private String regionId;

    private District district;

    private Role role;
}