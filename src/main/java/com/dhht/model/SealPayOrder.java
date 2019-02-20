package com.dhht.model;


import lombok.Data;

import java.util.Date;
@Data
public class SealPayOrder {

    private String id;

    private String sealId;

    private Boolean isPay;

    private Boolean isRightPay;

    private String payWay;

    private String payAccout;

    private Date payDate;

    private Boolean expressWay;

    private String expressWayName;

    private String courierId;

    private String memo;

    private String refundStatus;

    private Boolean isRefund;

    private Boolean isEvaluation;

    private String payJsOrderId;

    private String weChatUserId;

    private String makeDepartmentId;

    private Date createTime;
}