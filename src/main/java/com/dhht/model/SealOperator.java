package com.dhht.model;

import lombok.Data;

/**
 * Created by imac_dhht on 2018/7/17.
 */
@Data
public class SealOperator {
    private String id;

    private Seal seal;

    private SealGetPerson sealGetPerson;
//    private String id;
//
//    private String sealCode;
//
//    private String sealName;   //印章名称
//
//    private String sealStatusCode;   //印章状态代码
//
//    private String useDepartmentCode;  //印章使用单位编码
//
//    private String useDepartmentName;  //印章使用单位名字
//
//    private String recordDepartmentCode;    //印章备案单位编码
//
//    private String recordDepartmentName;    //印章备案单位名字
//
//    private String makeDepartmentCode;  //印章备案单位编码
//
//    private String makeDepartmentName;  //印章备案单位名字
//
//    private String sealTypeCode;    //印章类型编码
//
//    private String materialsCode;   //印章章面材料代码
//
//    private String mimeographDescription;   //印油说明
//
//    private String sealShapeCode;   //印章图案编码
//
//    private Double sealSize;    //章面尺寸
//
//    private String sealCenterImage; //中心图案
//
//    private String sealSpecification;   //规格说明
//
//    private String sealMakeTypeCode;    //印章刻制类别代码
//
//    private String sealRecordTypeCode;  //印章刻制类别代码
//
//    private String remark;//备注
//
//    private Date recordDate;    //备案日期
//
//    private Boolean isRecord;   //是否备案
//
//    private Date makeDate;  //制作日期
//
//    private Boolean isMake; //是否制作
//
//    private Date personalDate;   //个人化日期
//
//    private Boolean isPersonal; //是否个人化
//
//    private Date deliverDate;   //交付日期
//
//    private Boolean isDeliver;  //是否交付
//
//    private Date logoutDate;    //注销日期
//
//    private Boolean isLogout;   //是否注销
//
//    private Date lossDate;  //挂失日期
//
//    private Boolean isLoss; //是否挂失
//
//    private String sealReason; //印章原因

//    private Date dateTime; //操作时间
//
//    private String operatorTelphone; //操作人手机号
//
//    private String emplyeeName; //从业人员姓名
//
//    private String employeeId; //从业人员身份证
//
//    private String operatorName; //经办人姓名
//
//    private String operatorCertificateCode; //经办人身份证
//
//    private String operatorCrtificateType; //身份类型
//
//    private String  flag;//类型
    private SealOperationRecord sealOperationRecord;

    private String operatorPhoto; //经办人照片

//    private String idCardScanner; //身份证扫描件

    private String positiveIdCardScanner;//身份证正面扫描件

    private String reverseIdCardScanner;//身份证反面扫描件

    private String proxy; //委托书

    private Integer pageNum;

    private Integer pageSize;

    private String electronicSealURL; //电子印模数据

    private String sealScannerURL;//实物印章印模扫描件

    private String businessScanner;//营业执照扫描件




}
