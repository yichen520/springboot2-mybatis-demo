package com.dhht.model;

import lombok.Data;

@Data
public class FaceCompareResult {

    private String fieldPhoto;

    private int similarity;

    private Boolean ispass;

}
