package com.dhht.model;

import lombok.Data;

@Data
public class SyncEntity {
    private Object object;
    private String dataType;
    private String operateType;
}
