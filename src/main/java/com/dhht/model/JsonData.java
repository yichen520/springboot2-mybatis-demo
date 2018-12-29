package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class JsonData {
    private String id;

    private Integer dataType;

    private Integer operateType;

    private Integer dealResult;

    private Date createTime;

    private String failMessage;

    private String jsonData;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id == null ? null : id.trim();
//    }
//
//    public Integer getDataType() {
//        return dataType;
//    }
//
//    public void setDataType(Integer dataType) {
//        this.dataType = dataType;
//    }
//
//    public Integer getOperateType() {
//        return operateType;
//    }
//
//    public void setOperateType(Integer operateType) {
//        this.operateType = operateType;
//    }
//
//    public Integer getDealResult() {
//        return dealResult;
//    }
//
//    public void setDealResult(Integer dealResult) {
//        this.dealResult = dealResult;
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getFailMessage() {
//        return failMessage;
//    }
//
//    public void setFailMessage(String failMessage) {
//        this.failMessage = failMessage == null ? null : failMessage.trim();
//    }
//
//    public byte[] getJsonData() {
//        return jsonData;
//    }
//
//    public void setJsonData(byte[] jsonData) {
//        this.jsonData = jsonData;
//    }
}