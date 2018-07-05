package com.dhht.model;

import com.sun.xml.internal.ws.spi.db.DatabindingException;
import lombok.Data;

import java.sql.Date;

@Data
public class Seal extends SealKey {


    private String sealName;

    private String sealStatusCode;

    private String useDepartmentCode;

    private String useDepartmentName;

    private String recordDepartmentCode;

    private String recordDepartmentName;

    private String makeDepartmentCode;

    private String makeDepartmentName;

    private String sealTypeCode;

    private String materialsCode;

    private String mimeographDescription;

    private String sealShapeCode;

    private Double sealSize;

    private String sealCenterImage;

    private String sealSpecification;

    private String sealMakeTypeCode;

    private String sealRecordTypeCode;

    private Date recordDate;

    private Boolean isRecord;

    private Date makeDate;

    private Boolean isMake;

    private Date personalate;

    private Boolean isPersonal;

    private Date deliverDate;

    private Boolean isDeliver;

    private Date logoutDate;

    private Boolean isLogout;

    private Date lossDate;

    private Boolean isLoss;

    private SealImage sealImage;

 

}