package com.dhht.model;

import lombok.Data;

import java.util.Date;


@Data
public class Ems {
    private  String sender;  //寄件人信息
    private  String senderTelPhone;
    private  String senderPhone;
    private  String senderDistrictId;
    private  String senderAddress;

    private  String addressee;  //收件人信息
    private  String addresseeTelPhone;
    private  String addresseePhone;
    private  String addresseeDistrictId;
    private  String addresseeAddress;
    private  String Addressee;
    private  String AddresseeTelPhone;

    private  String goodsName; //备注信息
    private  String goodsType;
    private  String size_long;
    private  String size_wide;
    private  String size_high;
    private  String number;
    private  Boolean isInsured;
    private  String  weight;
    private  String remark;

    private Date time;


}
