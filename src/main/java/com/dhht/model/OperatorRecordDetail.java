package com.dhht.model;

import lombok.Data;

@Data
public class OperatorRecordDetail {
    private String id;

    private String entityOperateRecordId;

    private int propertyType;

    private String propertyName;

    private String oldValue;

    private String newValue;


}