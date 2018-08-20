package com.dhht.model;

import com.dhht.annotation.EntityComment;
import lombok.Data;

import java.util.Date;

@Data
public class Employee {
    private String id;

    @EntityComment(value = "从业人员编号")
    private String employeeCode;

    @EntityComment(value = "从业人员姓名")
    private String employeeName;

    @EntityComment(value = "从业人员身份证号码")
    private String employeeId;

    @EntityComment(value = "从业人员性别")
    private String employeeGender;

    @EntityComment(value = "从业人员岗位")
    private String employeeJob;

    @EntityComment(value = "从业人员所在制作单位编号")
    private String employeeDepartmentCode;

    @EntityComment(value = "从业人员民族")
    private String employeeNation;

    private String familyDistrictId;

    private String familyDistrictName;

    @EntityComment("从业人员户籍详细地址")
    private String familyAddressDetail;

    @EntityComment("从业人员现居详细地址")
    private String nowAddressDetail;

    private String nowDistrictId;

    private String nowDistrictName;

    @EntityComment("从业人员头像")
    private String employeeImage;

    @EntityComment("从业人员联系电话")
    private String telphone;

    @EntityComment("从业人员家庭联系人姓名")
    private String contactName;

    @EntityComment("从业人员人员家庭联系人电话")
    private String contactTelphone;

    private String officeCode;

    private String officeName;

    private String registerName;

    private Date registerTime;

    private String logoutOfficeCode;

    private String logoutOfficeName;

    private String logoutName;

    private int version;

    private Date versionTime;

    private String flag;

    private boolean deleteStatus;

    private String districtId;

    private String versionStatus;

    public Employee() {
    }


}