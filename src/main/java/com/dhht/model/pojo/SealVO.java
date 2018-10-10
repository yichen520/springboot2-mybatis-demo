package com.dhht.model.pojo;

import com.dhht.model.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SealVO  {

    private Seal seal;

    private String operationPhoto;  //照片

    private String PositiveIdCardScanner; //身份证正面

    private String  ReverseIdCardScanner; //身份证反面

    private String proxy;  //授权委托书

    private String moulageImageId;//印模图像

    private String micromoulageImageId;//印模图像

    private String moulageId;//印模二维数据

    private String lossBusinessLicense;//挂失营业执照

    private String LogoutBussinessLicense;//注销营业执照

    private String sealCard;

    private List<SealOperationRecord> sealOperationRecords;

//    private SealOperationRecord sealOperationRecord;

    private List<SealAgent> sealAgents;

    private UseDepartment useDepartment;

    private Makedepartment makeDepartment;

}