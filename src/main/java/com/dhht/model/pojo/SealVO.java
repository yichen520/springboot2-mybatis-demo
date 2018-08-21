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

    private String moulageId;

    private SealOperationRecord sealOperationRecord;

}