package com.dhht.model;

import lombok.Data;

@Data
public class CompareResult {
    private String propertyName;
    private int propertyType;
    private Object oldValue;
    private Object newValue;
}
