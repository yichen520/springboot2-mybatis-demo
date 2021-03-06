package com.dhht.model.pojo;

import com.dhht.model.Seal;
import lombok.Data;


import java.util.Date;
import java.util.List;

@Data
public class SealDTO {

    private String id;

    private Seal seal;

    private List<Seal> seals;

    private String useDepartmentCode;

    private String sealId;  //印章id

    private String employeeId;  //从业人员id

    private String employeeName;  //从业人员名字

    private String employeeCode;  //从业人员code

    private String operateType;   //操作类型

    private Date operateTime; //操作时间

    private String name;  //经办人姓名

    private String certificateType;  //证件类型

    private String certificateNo;  //证件号码

    private String telphone;  //联系电话

    private String agentPhotoId;//经办人照片

    private String idCardFrontId;//身份证正面

    private String idCardReverseId;//身份证反面

    private String proxyId;//授权委托书

    private String businessType; //类型  0-申请  1-领取

    private String faceCompareRecordId; //人证合一记录id

    private String businessLicenseId; // 营业执照

    private int confidence;//置信度

    private String fieldPhotoId;//现场照片

    private String idCardPhotoId;//身份证上的照片

    private String entryType;//录入方式  00-读卡  01-可信身份证录入

//    private boolean isChipseal;

    private Boolean isSame;

    private Integer pageNum;

    private Integer pageSize;
}
