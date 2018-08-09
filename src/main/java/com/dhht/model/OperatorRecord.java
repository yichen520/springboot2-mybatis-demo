package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class OperatorRecord {
    private String id;

    private String operateUserId;

    private String operateUserRealname;

    private String operateEntityId;

    private String operateEntityName;

    private String operateEntityComment;

    private int operateType;

    private String operateTypeName;

    private Date operateTime;
}