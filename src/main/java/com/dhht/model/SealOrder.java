package com.dhht.model;

import lombok.Data;

/**
 * @author 徐正平
 * @Date 2019/1/19 9:53
 */
@Data
public class SealOrder {
    private Seal seal;
    private SealPayOrder sealPayOrder;
    private  SealAgent sealAgent;
    private Recipients recipients;
    private SealMaterial sealMaterial;
    private Boolean isEvaluate = false;

}
