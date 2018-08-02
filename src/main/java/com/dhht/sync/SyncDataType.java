/**
 * 
 */
package com.dhht.sync;

/**
 * @author zxl
 *
 */
public class SyncDataType {

	 public static final int EMPLOYEE  = 1001;//从业人员
    public static final int EMPLOYEE_PHOTO  = 102;//从业人员
	 public static final int SEAL  = 1101;//印章
	 public static final int USERDEPARTMENT = 3;//使用单位
	 public static final int MAKEDEPARTMENT = 4;//制作单位
	 public static final int RECORDDEPARTMENT = 5;//备案单位
	 public static final int EXAMINE = 6;//监督检查
	 public static final int PUNISHMAKEDEPARTMENT = 7;//制作单位惩罚
	 public static final int PUNISHEMPLOYEE = 8;//从业人员惩罚
    /**
     * 获取数据类型名
     * @param dataType
     * @return
     */
    public static String getDataTypeName(int dataType) {
        switch (dataType) {
            case EMPLOYEE : 
                return "从业人员";
            case SEAL:
                return "印章";
            case USERDEPARTMENT:
                return "使用单位";
            case MAKEDEPARTMENT:
                return "制作单位";
            case RECORDDEPARTMENT:
                return "备案单位";
            case EXAMINE:
                return "监督检查";
            case PUNISHMAKEDEPARTMENT:
                return "制作单位惩罚";
            case PUNISHEMPLOYEE:
                return "从业人员惩罚";
            default:
                return "";
        }
    }
}
