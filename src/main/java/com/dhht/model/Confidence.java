package com.dhht.model;

import lombok.Data;

@Data
public class Confidence {

    private String fileBURL;

    private int similarity;

    private Boolean isPass;

}
