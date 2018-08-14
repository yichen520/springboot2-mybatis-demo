package com.dhht.util;

/**
 * createBy fyc 2018/8/14
 */
public class DictionaryUtil {
    private static final String recordDepartmentCode = "备案单位编号";
    private static final String recordDepartmentName = "备案单位名称";
    private static final String recordPrincipalName = "备案单位负责人姓名";
    private static final String recordDepartmentAddress = "备案单位行政区域划分";
    private static final String recordDepartmentAddressDetail = "备案单位详细地址";
    private static final String recordDepartmentTelphone = "备案单位负责人电话";
    private static final String recordDepartmentPostalCode = "备案单位邮政编码";

    private static final String employeeCode = "从业人员编号";
    private static final String employeeName = "从业人员姓名";
    private static final String employeeGender = "从业人员性别";
    private static final String employeeId = "从业人员身份证号码";
    private static final String employeeJob = "从业人员职务";
    private static final String employeeNation = "从业人员民族";
    private static final String employeeFamilyAddressDetail = "从业人员户口地址";
    private static final String employeeNowAddressDetail = "从业人员现居地址";
    private static final String employeeTelphone = "从业人员电话";
    private static final String employeeContactName ="从业人员紧急联系人姓名";
    private static final String employeeContactTelphone="从业人员紧急联系人姓名";


    public static String getDictionaryValue(int type,String key){
        switch (type){
            case 1401:
                switch (key){
                    case "departmentCode":
                        return recordDepartmentCode;
                    case "departmentName":
                        return recordDepartmentName;
                    case "principalName":
                        return recordPrincipalName;
                    case "departmentAddress":
                        return recordDepartmentAddress;
                    case "departmentAddressDetail":
                        return recordDepartmentAddressDetail;
                    case "telphone":
                        return recordDepartmentTelphone;
                    case "postalCode":
                        return recordDepartmentPostalCode;
                     default:
                         return "";
                }
            case 1001:
                switch (key){
                    case "employeeCode":
                        return employeeCode;
                    case "employeeName":
                        return employeeName;
                    case "employeeGender":
                        return employeeGender;
                    case "employeeId":
                        return employeeId;
                    case "employeeJob":
                        return employeeJob;
                    case "employeeNation":
                        return employeeNation;
                    case "familyAddressDetail":
                        return employeeFamilyAddressDetail;
                    case "nowAddressDetail":
                        return employeeNowAddressDetail;
                    case "telphone":
                        return employeeTelphone;
                    case "contactName":
                        return employeeContactName;
                    case "contactTelphone":
                        return employeeContactTelphone;
                }
            default:
                return null;
        }
    }
}



