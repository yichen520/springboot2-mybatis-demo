package com.dhht.model.pojo;

import com.dhht.model.Suspicious;
import lombok.Data;

import java.util.Date;
@Data
public class SuspiciousPO {

    private String makeDepartmentCode;

    private String makeDepartmentName;

    private Suspicious suspicious;


    private String suspiciousType;

    private Date suspiciousTime;

    private String districtId;

    private String updateUser;


    private String startTime;

    private String endTime;

     private int pageSize;


     private int pageNum;
}
