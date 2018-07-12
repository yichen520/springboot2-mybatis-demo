package com.dhht.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class OfficeCheck {
    private String makedepartmentCode;

    private String id;

    private String checkName;

    private String officeCode;

    private String officeName;

    private Date checkTime;

    private String checkAddress;

    private String handleResult;

    private String makedepartmentName;

    private String district;

    private List<PunishLog> punishLogs;

}