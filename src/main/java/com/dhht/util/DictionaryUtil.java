package com.dhht.util;

public class DictionaryUtil {

    public final static String DOCUMENT_TYPE = "法人证件类型";
    public final static String DEPARTMENT_TYPE = "单位类型";

    public final static String IDCARD_TYPE_CODE = "111";
    public final static String PASSPORT_TYPE_CODE ="414";

    public final static String IDCARD_TYPE_NAME = "居民身份证";
    public final static String PASSPORT_TYPE_NAME = "普通护照";

    public final static String ENTERPRISE_CODE = "0";
    public final static String INDIVIDUAL_CODE = "1";
    public final static String GOVERNMENT_CODE = "01";
    public final static String BUSINESSUNIT_CODE ="02";
    public final static String INSTITUTION_CODE ="03";
    public final static String SOCIALGROUPS_CODE ="04";
    public final static String PRIVATEUNIT_CODE = "05";
    public final static String OTHER_CODE = "99";

    public final static String ENTERPRISE_NAME = "企业";
    public final static String INDIVIDUAL_NAME = "个体工商户";
    public final static String GOVERNMENT_NAME = "党政机关、人大、政协";
    public final static String BUSINESSUNIT_NAME ="企业单位";
    public final static String INSTITUTION_NAME="事业单位";
    public final static String SOCIALGROUPS_NAME="社会团体";
    public final static String PRIVATEUNIT_NAME= "民办非企业单位";
    public final static String OTHER_NAME = "其他";



    public static String getCodeName(String commentKey,Object valueKey){
        if(commentKey.contains(DEPARTMENT_TYPE)){
            switch (valueKey.toString()){
                case ENTERPRISE_CODE:
                    return ENTERPRISE_NAME;
                case INDIVIDUAL_CODE:
                    return INDIVIDUAL_NAME;
                case GOVERNMENT_CODE:
                    return GOVERNMENT_NAME;
                case BUSINESSUNIT_CODE:
                    return BUSINESSUNIT_NAME;
                case INSTITUTION_CODE:
                    return INSTITUTION_NAME;
                case SOCIALGROUPS_CODE:
                    return SOCIALGROUPS_NAME;
                case PRIVATEUNIT_CODE:
                    return PRIVATEUNIT_NAME;
                case OTHER_CODE:
                    return OTHER_NAME;
                    default:
                        break;
            }
        }
        if(commentKey.contains(DOCUMENT_TYPE)) {
            switch (valueKey.toString()) {
                case IDCARD_TYPE_CODE:
                    return IDCARD_TYPE_NAME;
                case PASSPORT_TYPE_CODE:
                    return PASSPORT_TYPE_NAME;
                default:
                    break;
            }
        }
        return valueKey.toString();
    }
}
