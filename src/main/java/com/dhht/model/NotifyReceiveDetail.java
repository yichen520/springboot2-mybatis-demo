package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class NotifyReceiveDetail {
    private String id;

    private String notifyId;

    private String receiveUserId;

    private String receiveUserName;

    private Boolean isRead;

    private Date readTime;


}