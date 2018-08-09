package com.dhht.model;

public class OperatorRecordDetail {
    private String id;

    private String entityOperateRecordId;

    private String propertyName;

    private String propertyComment;

    private String oldValue;

    private String newValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEntityOperateRecordId() {
        return entityOperateRecordId;
    }

    public void setEntityOperateRecordId(String entityOperateRecordId) {
        this.entityOperateRecordId = entityOperateRecordId == null ? null : entityOperateRecordId.trim();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName == null ? null : propertyName.trim();
    }

    public String getPropertyComment() {
        return propertyComment;
    }

    public void setPropertyComment(String propertyComment) {
        this.propertyComment = propertyComment == null ? null : propertyComment.trim();
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue == null ? null : oldValue.trim();
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }
}