package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class ChipApply {
    private String id;

    private Integer chipNum;

    private Date getTime;

    private String address;

    private String addressDetail;

    private String makeDepartmentCode;

    private String makeDepartmentName;

    private Date applyTime;

    private String memo;

    private int applyFlag;

    private int ungrantnum;

}