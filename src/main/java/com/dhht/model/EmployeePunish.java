package com.dhht.model;

import lombok.Data;

import java.sql.Date;

/**
 * Created by imac_dhht on 2018/7/12.
 */
@Data
public class EmployeePunish {
    private String id;
    private String makedepartmentCode;
    private String punishEmployeeCode;
    private String punishEmployeeName;
    private String punishEmployeeId;
    private String detail;
    private String reason;
    private String result;
    private String officeCode;
    private String officeName;
    private String transactionName;
    private Date punishTime;
}
