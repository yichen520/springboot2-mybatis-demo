package com.dhht.model;

public class ExamineRecordDetail {
    private String id;

    private String examineRecordId;

    private String examineItem;

    private Boolean result;

    private String resultDestription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getExamineRecordId() {
        return examineRecordId;
    }

    public void setExamineRecordId(String examineRecordId) {
        this.examineRecordId = examineRecordId == null ? null : examineRecordId.trim();
    }

    public String getExamineItem() {
        return examineItem;
    }

    public void setExamineItem(String examineItem) {
        this.examineItem = examineItem == null ? null : examineItem.trim();
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getResultDestription() {
        return resultDestription;
    }

    public void setResultDestription(String resultDestription) {
        this.resultDestription = resultDestription == null ? null : resultDestription.trim();
    }
}