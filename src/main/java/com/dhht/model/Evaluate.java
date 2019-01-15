package com.dhht.model;

import java.util.Date;

public class Evaluate {
    private String id;

    private String userId;

    private String userName;

    private String evaluateContent;

    private Date evaluateTime;

    private String makeDepartmentId;

    private String makeDepartmentName;

    private Integer score;

    private Integer qualityScore;

    private Integer priceScore;

    private Integer speedScore;

    private Integer atttitudeScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent == null ? null : evaluateContent.trim();
    }

    public Date getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Date evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getMakeDepartmentId() {
        return makeDepartmentId;
    }

    public void setMakeDepartmentId(String makeDepartmentId) {
        this.makeDepartmentId = makeDepartmentId == null ? null : makeDepartmentId.trim();
    }

    public String getMakeDepartmentName() {
        return makeDepartmentName;
    }

    public void setMakeDepartmentName(String makeDepartmentName) {
        this.makeDepartmentName = makeDepartmentName == null ? null : makeDepartmentName.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(Integer qualityScore) {
        this.qualityScore = qualityScore;
    }

    public Integer getPriceScore() {
        return priceScore;
    }

    public void setPriceScore(Integer priceScore) {
        this.priceScore = priceScore;
    }

    public Integer getSpeedScore() {
        return speedScore;
    }

    public void setSpeedScore(Integer speedScore) {
        this.speedScore = speedScore;
    }

    public Integer getAtttitudeScore() {
        return atttitudeScore;
    }

    public void setAtttitudeScore(Integer atttitudeScore) {
        this.atttitudeScore = atttitudeScore;
    }
}