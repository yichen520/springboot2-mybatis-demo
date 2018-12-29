package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class ChipGrant {

    private String id;

    private String chipApplyId;

    private String chipCodeStart;

    private String chipCodeEnd;

    private String receiver;

    private String receiverTel;

    private Integer grantNum;

    private Date grantTime;

    private String granterId;

    private String granter;

    private String grantWay;

    private String memo;

    private Date recordTime;

    private String districtId;


}