package com.dhht.model;

import lombok.Data;

import java.sql.Date;

/**
 * Created by imac_dhht on 2018/7/12.
 */
@Data
public class MakeDepartmentPunish {
    private String id;
    private String makedepartmentCode;
    private String punishCode;
    private String punishDetail;
    private String punishReason;
    private String punishResult;
    private String officeCode;
    private String officeName;
    private String transactionName;
    private Date punishTime;
}
