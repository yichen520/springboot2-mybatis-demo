package com.dhht.model;

import com.dhht.annotation.EntityComment;
import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;

import java.util.Date;

/**
 * Created by 崔杨 on 2018/6/13.
 */
@Data
public class UseDepartment {
    private String id;
    @EntityComment("使用单位编码")
    private String code;   //印章使用单位编码
    @EntityComment("使用单位名称")
    private String name;//单位名称
    @EntityComment("使用单位少数民族名称")
    private String nationName;//单位少数名族文字名称
    @EntityComment("使用单位英语全称")
    private String englishName;//单位英语名称
    @EntityComment("使用单位英语缩写")
    private String englishAhhr;//单位英语缩写
    @EntityComment("使用单位类型")
    private String departmentType;//单位使用单位类型代码
    @EntityComment("使用单位查询密码")
    private String queryPassword;//查询密码
    @EntityComment("使用单位法定代表人姓名")
    private String legalName;//法定代表人
    @EntityComment("使用单位法人证件号码")
    private String legalId;//证件号码
    @EntityComment("使用单位法人证件类型")
    private String legalIdType;//证件种类
    @EntityComment("使用单位法人联系电话")
    private String legalTelphone;//联系电话
    @EntityComment("使用单位法人英文名字缩写")
    private String legalEnglishsurname;
    @EntityComment("使用单位法人英文名")
    private String legalEnglishname;
    @EntityComment("使用单位法人国籍")
    private String legalCountry;//国籍
    //    private String departmentAddress;//单位地址
    @EntityComment("行政区域划分")
    private String districtId;
    @EntityComment("行政区域划分")
    private String districtName;
    @EntityComment("使用单位详细地址")
    private String address;//省市区详细地址
    @EntityComment("使用单位联系电话")
    private String telphone;//联系电话
    @EntityComment("使用单位邮政编码")
    private String postalCode;//邮政编码
    @EntityComment("使用单位状态")
    private String departmentStatus;//企业状态  0-正常 1-注销
    @EntityComment("使用单位数字证书")
    private String departmentCertificate;//使用单位数字证书
    @EntityComment("使用单位数字证书类型")
    private String departmentCertificateType;//使用单位数字证书类型
    @EntityComment("使用单位成立日期")
    private Date foundDate;
    @EntityComment("使用单位开始时间")
    private Date startDate;
    @EntityComment("使用单位结束时间")
    private Date endDate;
    @EntityComment("使用单位注册机关")
    private String registrationDepartment;
    @EntityComment("使用单位经营范围")
    private String managementRange;
    @EntityComment("使用单位注册资本")
    private String registeredCapital;
    @EntityComment(value = "使用单位营业执照扫面件",type = 2)
    private String businessLicenseUrl;
    @EntityComment(value = "使用单位特种行业许可证",type = 2)
    private String specialBusinessLicenceScanning;
    @EntityComment(value = "使用单位法人身份证正面扫面件",type = 2)
    private String idCardFrontId;
    @EntityComment(value = "使用单位法人身份证反面扫面件",type = 2)
    private String idCardReverseId;
    @EntityComment(value ="注册经办人姓名")
    private String  managerName;
    @EntityComment(value = "注册经办人手机")
    private String managerPhone;
    private Boolean isDelete;
    private Integer version;
    private String flag;
    private Date updateTime;
    private String foundDateFormat;

}
