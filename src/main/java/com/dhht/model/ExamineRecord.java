package com.dhht.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class ExamineRecord {
    private String id;

    private String examinerName;

    private String recordDepartmentCode;

    private String recordDepartmentName;

    private Date examineTime;

    private String examineAddress;

    private String examineResultDescription;

    private String disposeDescription;

    private String makeDepartmentCode;

    private String makeDepartmentName;

    private String remark;

    private String districtId;

    private String examineTypeId;

    private Float longitude;

    private Float latitude;

    private List<ExamineRecordDetail> examineRecordDetails;

}