package com.dhht.model;

import lombok.Data;

@Data
public class UserSimple {
    private String Id;
    private String userName;
    private String realName;
    private String roleId;
    private boolean isLocked;
    private String districtId;
    private String telphone;



}
