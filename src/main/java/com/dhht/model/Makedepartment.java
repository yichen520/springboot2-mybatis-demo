package com.dhht.model;

import com.dhht.annotation.EntityComment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Makedepartment {
    private String id;
    @EntityComment("制作单位编号")
    private String departmentCode;
    @EntityComment("制作单位名称")
    private String departmentName;
    @EntityComment("制作单位少数民族名称")
    private String departmentNationName;
    @EntityComment("制作单位英文名字全称")
    private String departmentEnglishName;
    @EntityComment("制作单位英文名字简称")
    private String departmentEnglishAhhr;
    @EntityComment("制作单位类型")
    private String departmentType;
    @EntityComment("制作单位法人名字")
    private String legalName;
    @EntityComment("制作单位法人证件号码")
    private String legalId;
    @EntityComment("制作单位法人证件类型")
    private String legalIdType;
    @EntityComment("制作单位法人英文名字缩写")
    private String legalEnglishsurname;
    @EntityComment("制作单位法人英文名字全称")
    private String legalEnglishname;
    @EntityComment("制作单位法人手机号码")
    private String legalTelphone;
    @EntityComment("行政区域划分")
    private String departmentAddress;
    @EntityComment("制作单位详细地址")
    private String departmentAddressDetail;
    @EntityComment("制作单位联系电话")
    private String telphone;
    @EntityComment("制作单位邮政编码")
    private String postalCode;
    @EntityComment(value = "制作单位法人身份证正面扫描件",type = 2 )
    private String idCardFrontId;
    @EntityComment(value = "制作单位法人身份证反面扫描件",type = 2)
    private String idCardReverseId;
    @EntityComment(value = "制作单位营业执照扫面件",type = 2)
    private String businessLicenseUrl;
    private String specialLicenseUrl;
    private String legalDocumentUrl;
    private String departmentStatus;
    private int version;
    private String flag;
    private Date versionTime;
    private Date registerTime;
    private MakeDepartmentPunish makeDepartmentPunish;
    private List<Employee> employeeList;
    }