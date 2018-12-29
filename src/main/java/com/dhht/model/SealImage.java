package com.dhht.model;

import lombok.Data;

@Data
public class SealImage {
    private String id;

    private String sealId;

    private Double sealImageWidth;

    private Double sealImageHeight;

    private Byte sealImageCompressStatus;

    private String url;

//    private byte[] sealImageData;


}