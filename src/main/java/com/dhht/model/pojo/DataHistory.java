package com.dhht.model.pojo;

import lombok.Data;

@Data
public class DataHistory {

    private  String attribute;
   private String beforeData;

    private String afterData;

    public DataHistory(String attribute, String beforeData, String afterData) {
        this.attribute = attribute;
        this.beforeData = beforeData;
        this.afterData = afterData;
    }

    public DataHistory() {
    }
}
