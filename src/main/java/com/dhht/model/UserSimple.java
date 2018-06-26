package com.dhht.model;

import lombok.Data;

@Data
public class UserSimple {
    private String Id;
    private String UserName;
    private String RealName;
    private String RoleId;
    private boolean IsLocked;
    private String districtId;
    private String telphone;


}
