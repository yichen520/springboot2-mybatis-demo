package com.dhht.model;

import com.dhht.common.JsonObjectBO;
import lombok.Data;

import java.util.Date;
@Data
public class SealPayOrder {
    private String id;

    private String sealId;

    private Boolean isPay;

    private String payWay;

    private String payAccout;

    private Date payDate;

    private Boolean expressWay;

    private String expressWayName;

    private String courierId;

    private String memo;



}