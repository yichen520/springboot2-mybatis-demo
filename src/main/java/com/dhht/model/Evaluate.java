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

    private String makeDepartmentId;

    private String flag;

    private String makeDepartmentName;

    private Integer score;

    private Integer qualityScore;

    private Integer priceScore;

    private Integer speedScore;

    private Integer atttitudeScore;

    private String sealId;

    private String headImage;

}