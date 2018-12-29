package com.dhht.model;

import java.sql.Date;

/**
 * Created by imac_dhht on 2018/7/6.
 */
public class SealStatus extends SealKey{

    private Date recordDate;    //备案日期

    private Boolean isRecord;   //是否备案

    private Date makeDate;  //制作日期

    private Boolean isMake; //是否制作

    private Date personalate;   //个人化日期

    private Boolean isPersonal; //是否个人化

    private Date deliverDate;   //交付日期

    private Boolean isDeliver;  //是否交付

    private Date logoutDate;    //注销日期

    private Boolean isLogout;   //是否注销

    private Date lossDate;  //挂失日期

    private Boolean isLoss; //是否挂失
}
