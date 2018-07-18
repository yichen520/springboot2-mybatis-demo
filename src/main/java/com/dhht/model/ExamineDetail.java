package com.dhht.model;

public class ExamineDetail {
    private String id;

    private String examineTypeId;

    private String examineItem;

    private Integer orderValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getExamineTypeId() {
        return examineTypeId;
    }

    public void setExamineTypeId(String examineTypeId) {
        this.examineTypeId = examineTypeId == null ? null : examineTypeId.trim();
    }

    public String getExamineItem() {
        return examineItem;
    }

    public void setExamineItem(String examineItem) {
        this.examineItem = examineItem == null ? null : examineItem.trim();
    }

    public Integer getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Integer orderValue) {
        this.orderValue = orderValue;
    }
}