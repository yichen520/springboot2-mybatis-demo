package com.dhht.model;

import lombok.Data;

@Data
public class FaceCompareResult {

    private String fieldPhotoId;

    private String idCardPhotoId;

    private String idCardPhotoPath;

    private String fieldPhotoPath;

    private int similarity;

    private Boolean isPass;

}
