package com.dhht.model;

import com.dhht.annotation.EntityComment;
import lombok.Data;

import java.util.Date;

@Data
public class RecordDepartment {

    private String id;

    @EntityComment("备案单位编号")
    private String departmentCode;

    @EntityComment("备案单位名称")
    private String departmentName;

    @EntityComment("负责人姓名")
    private String principalName;

    @EntityComment("负责人身份证号码")
    private String principalId;

    @EntityComment("行政区域划分")
    private String departmentAddress;

    @EntityComment("备案单位详细地址")
    private String departmentAddressDetail;

    @EntityComment("备案单位联系电话")
    private String telphone;

    @EntityComment("备案单位邮编")
    private String postalCode;

    private Boolean isDelete;

    private int version;

    private String flag;

    private Date updateTime;

    private String operator;


}
