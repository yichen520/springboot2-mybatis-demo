package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class Evaluate {
    private String id;

    private String userId;

    private String userName;

    private String evaluateContent;

    private Date evaluateTime;

    private String flag;

    private String makeDepartmentName;

    private Integer score;

}