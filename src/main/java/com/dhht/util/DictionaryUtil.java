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

    private static final String makeDepartmentCode = "制作单位编码";
    private static final String makeDepartmentName = "制作单位名称";
    private static final String makeDepartmentNationName = "制作单位少数民族名称";
    private static final String makeDepartmentEnglishName = "制作单位英文全称";
    private static final String makeDepartmentEnglishAhhr = "制作单位英文简称";
    private static final String makeDepartmentType = "制作单位类型";
    private static final String makeDepartmentLegalName = "制作单位法人姓名";
    private static final String makeDepartmentLegalId = "制作单位法人有效证件号";
    private static final String makeDepartmentLegalIdType = "制作单位法人有效证件类型";
    private static final String makeDepartmentLegalEnglishName = "制作单位法人英文名";
    private static final String makeDepartmentLegalEnglishSurName = "制作单位法人英文名缩写";
    private static final String makeDepartmentLegalTelphone = "制作单位法人电话";
    private static final String makeDepartmentAddress = "制作单位行政区划";
    private static final String makeDepartmentAddressDetail = "制作单位详细地址";
    private static final String makeDepartmentTelphone = "制作单位联系电话";
    private static final String makeDepartmentPostalCode = "制作单位邮政编码";

    private static final String useDepartmentCode = "使用单位编号";
    private static final String useDepartmentName = "使用单位名称";
    private static final String useDepartmentNationName = "使用单位少数民族名称";
    private static final String useDepartmentEnglishName = "使用单位英文全称";
    private static final String useDepartmentEnglishAhhr = "使用单位英文简称";
    private static final String useDepartmentType = "使用单位类型";
    private static final String useDepartmentLegalName = "使用单位法人姓名";
    private static final String useDepartmentLegalId = "使用单位法人有效证件号";
    private static final String useDepartmentLegalIdType = "使用单位法人有效证件类型";
    private static final String useDepartmentLegalTelphone ="使用单位法人电话";
    private static final String useDepartmentLegalCountry = "使用单位法人国籍";
    private static final String useDepartmentDistrictId = "使用单位行政区划";
    private static final String useDepartmentAddress = "使用单位详细地址";
    private static final String useDepartmentTelphone ="使用单位联系电话";
    private static final String useDepartmentPostalCode = "使用单位邮政编码";
    private static final String useDepartmentCertificateType = "使用单位数字证书类型";
    private static final String useDepartmentCertificate = "使用单位数字证书";
    private static final String useDepartmentFoundDate = "使用单位成立日期";
    private static final String useDepartmentStartDate = "使用单位开始日期";
    private static final String useDepartmentEndDate = "使用单位结束日期";
    private static final String useDepartmentRegistrationDepartment ="使用单位注册机构";
    private static final String useDepartmentManagementRange = "使用单位经营范围";
    private static final String useDepartmentRegisteredCapital = "使用单位注册资本";
    private static final String useDepartmentLegalEnglishSueName = "使用单位法人英语名字简写";
    private static final String useDepartmentLegalEnglishName = "使用单位法人英语全称";




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
                    default:
                        return "";
                }
            case 1301:
                switch (key){
                    case "departmentCode":
                        return makeDepartmentCode;
                    case "departmentName":
                        return makeDepartmentName;
                    case "departmentNationName":
                        return makeDepartmentNationName;
                    case "departmentEnglishName":
                        return makeDepartmentEnglishName;
                    case "departmentEnglishAhhr":
                        return makeDepartmentEnglishAhhr;
                    case "departmentType":
                        return makeDepartmentType;
                    case "legalName":
                        return makeDepartmentLegalName;
                    case "legalId":
                        return makeDepartmentLegalId;
                    case "legalIdType":
                        return makeDepartmentLegalIdType;
                    case "legalEnglishsurname":
                        return makeDepartmentLegalEnglishSurName;
                    case "legalEnglishname":
                        return makeDepartmentLegalEnglishName;
                    case "legalTelphone":
                        return makeDepartmentLegalTelphone;
                    case "departmentAddress":
                        return makeDepartmentAddress;
                    case "departmentAddressDetail":
                        return makeDepartmentAddressDetail;
                    case "telphone":
                        return makeDepartmentTelphone;
                    case "postalCode":
                        return makeDepartmentPostalCode;
                    default:
                        return "";
                }
            case 1201:
                switch (key){
                    case "departmentCode":
                        return useDepartmentCode;
                    case "departmentName":
                        return useDepartmentName;
                    case "nationName":
                        return useDepartmentNationName;
                    case "englishName":
                        return useDepartmentEnglishName;
                    case "englishAhhr":
                        return useDepartmentEnglishAhhr;
                    case "departmentType":
                        return useDepartmentType;
                    case "legalName":
                        return useDepartmentLegalName;
                    case "legalId":
                        return useDepartmentLegalId;
                    case "legalIdType":
                        return useDepartmentLegalIdType;
                    case "legalTelphone":
                        return useDepartmentLegalTelphone;
                    case "legalCountry":
                        return useDepartmentLegalCountry;
                    case "districtId":
                        return useDepartmentDistrictId;
                    case "address":
                        return useDepartmentAddress;
                    case "telphone":
                        return useDepartmentTelphone;
                    case "postalCode":
                        return useDepartmentPostalCode;
                    case "departmentCertificate":
                        return useDepartmentCertificate;
                    case "departmentCertificateType":
                        return useDepartmentCertificateType;
                    case "foundDate":
                        return useDepartmentFoundDate;
                    case "startDate":
                        return useDepartmentStartDate;
                    case "endDate":
                        return useDepartmentEndDate;
                    case "registrationDepartment":
                        return useDepartmentRegistrationDepartment;
                    case "managementRange":
                        return useDepartmentManagementRange;
                    case "registeredCapital":
                        return useDepartmentRegisteredCapital;
                    case "legalEnglishsurname":
                        return useDepartmentLegalEnglishSueName;
                    case "legalEnglishname":
                        return useDepartmentLegalEnglishName;
                    default:
                        return "";
                }
            default:
                return "";
        }
    }
}



