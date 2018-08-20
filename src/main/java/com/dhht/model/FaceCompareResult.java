package com.dhht.model;

import lombok.Data;

@Data
public class FaceCompareResult {

    private String fieldPhotoId;

    private int similarity;

    private Boolean isPass;

}
