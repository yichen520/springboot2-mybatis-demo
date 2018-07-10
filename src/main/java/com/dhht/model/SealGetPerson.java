package com.dhht.model;

import lombok.Data;

import java.sql.Date;

/**
 * Created by imac_dhht on 2018/7/6.
 */
@Data
public class SealGetPerson extends SealKey {

    private String getpersonName;

    private String  getpersonType;

    private String getpersonId;

    private String getpersonTelphone;

    private Date getDate;

    private Boolean isSame;
}
