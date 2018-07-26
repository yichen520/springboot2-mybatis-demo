package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class APKVersion extends APKVersionKey {
    private String versionName;

    private String apkurl;

    private Date createtime;

    private String description;

    private boolean forceInstall;

    private String userId;

    private String userName;


}