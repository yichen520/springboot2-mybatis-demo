package com.dhht.model.pojo;

import dhht.idcard.trusted.identify.IdentifyResult;
import lombok.Data;

@Data
public class TrustedIdentityAuthenticationVO {
    private String certificateNo;

    private String name;

    private String fieldPhotoId;

    private Boolean isPass;

    private String message;


}
