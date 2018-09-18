package com.dhht.model.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class IncidencePO {

    private String serialCode;

    private String departmentCode;

    private String departmentName;

    private String incidenceId;

    private String incidenceType;

    private String incidenceCategory;

    private Date incidenceTime;

    private String districtId;

    private String startTime;

    private String endTime;

    private int pageSize;

    private int pageNum;


}