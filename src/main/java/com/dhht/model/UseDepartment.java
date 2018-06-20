package com.dhht.model;

/**
 * Created by imac_dhht on 2018/6/13.
 */
public class UseDepartment {
    private String id;
    private String code;   //印章使用单位编码
    private String name;//单位名称e
    private String nationName;//单位少数名族文字名称
    private String englishName;//单位英语名称
    private String englishAhhr;//单位英语缩写
    private Integer status;//单位使用单位类型代码
    private String queryPassword;//查询密码
    private String legalName;//法定代表人
    private String legalId;//证件号码
    private String legalIdType;//证件种类
    private String legalTelphone;//联系电话
    private String legalCountry;//国籍
    //    private String departmentAddress;//单位地址
    private String districtId;
    private String address;//省市区详细地址
    private String telphone;//联系电话
    private String postalCode;//邮政编码
    private String departmentStatus;//企业状态
    private String departmentCertificate;//使用单位数字证书
    private String departmentCertificateType;//使用单位数字证书类型
    private Boolean isDelete;
}
