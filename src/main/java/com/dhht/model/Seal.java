package com.dhht.model;

import lombok.Data;

import java.sql.Date;

@Data
public class Seal extends SealKey {


    private String sealName;   //印章名称

    private String sealStatusCode;   //印章状态代码

    private String useDepartmentCode;  //印章使用单位编码

    private String useDepartmentName;  //印章使用单位名字

    private String recordDepartmentCode;    //印章备案单位编码

    private String recordDepartmentName;    //印章备案单位名字

    private String makeDepartmentCode;  //印章备案单位编码

    private String makeDepartmentName;  //印章备案单位名字

    private String sealTypeCode;    //印章类型编码

    private String materialsCode;   //印章章面材料代码

    private String mimeographDescription;   //印油说明

    private String sealShapeCode;   //印章图案编码

    private Double sealSize;    //章面尺寸

    private String sealCenterImage; //中心图案

    private String sealSpecification;   //规格说明

    private String sealMakeTypeCode;    //印章刻制类别代码

    private String sealRecordTypeCode;  //印章刻制类别代码

    private String remark;//备注

    private Date recordDate;    //备案日期

    private Boolean isRecord;   //是否备案

    private Date makeDate;  //制作日期

    private Boolean isMake; //是否制作

    private Date personalate;   //个人化日期

    private Boolean isPersonal; //是否个人化

    private Date deliverDate;   //交付日期

    private Boolean isDeliver;  //是否交付

    private Date logoutDate;    //注销日期

    private Boolean isLogout;   //是否注销

    private Date lossDate;  //挂失日期

    private Boolean isLoss; //是否挂失

    private String sealreason; //印章原因

    private SealImage sealImage;    //印章图像

    private SealOperationRecord sealOperationRecord;    //经办人等

    private SealStatus sealStatus;        //印章状态

    private RecordDepartment recordDepartment;       //备案单位




}