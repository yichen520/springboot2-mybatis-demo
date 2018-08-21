package com.dhht.model;

import lombok.Data;

@Data
public class CompareResult {
    private String propertyName;
    private Object oldValue;
    private Object newValue;
}
