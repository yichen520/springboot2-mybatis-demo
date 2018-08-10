package com.dhht.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OperatorRecord {
    private String id;

    private String operateUserId;

    private String operateUserRealname;

    private String operateEntityId;

    private String operateEntityName;

    private String operateEntityComment;

    private Integer operateType;

    private String operateTypeName;

    private Date operateTime;

    private String flag;

    private List<OperatorRecordDetail> operatorRecordDetails;

}