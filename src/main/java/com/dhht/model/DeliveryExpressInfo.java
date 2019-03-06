package com.dhht.model;

import lombok.Data;

/**
 * @author 徐正平
 * @Date 2019/1/18 10:02
 */
@Data
public class DeliveryExpressInfo {

    private String sendName;
    private String sendTelphone;
    private  String senddistrictId;
    private  String senddistrictName;
    private String sendAddressDetail;

    private String receiveName;
    private String receivephone;
    private  String receivedistrictId;
    private  String receivedistrictName;
    private String receiveAddressDetail;

    private String agentName;
    private String agentphone;
    private  String agentidcard;
    private  String agentPhotoImage;
    private String agentidcardFrontImage;
    private String agentidcardReverseImage;
    private String agentproxyImage;

    private String loginTelphone;


}
