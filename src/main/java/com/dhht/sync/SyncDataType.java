/**
 * 
 */
package com.dhht.sync;

/**
 * @author zxl
 *
 */
public class SyncDataType {
    public static final int IMAGE = 0;//图片
    public static final int SYNC_RESULT = 1001;//二维码识别结果
    
	 public static final int EMPLOYEE  = 1;//从业人员
	 public static final int PROJECT  = 2;//项目
	 public static final int PROJECTRECORD = 3;//备案记录
	 public static final int COMPANY = 4;//企业信息
	 public static final int YINGJIANWUFANG = 5;//硬件物防
	 public static final int JIFANG = 6;//技防
	 public static final int PROJECTFILE = 7;//表格照片
	 public static final int JOURNALYINGYE = 8;//营业日志
	 public static final int JOURNALINSPECT = 9;//巡检记录
	 public static final int PROJECTCONDITION = 10;//项目材料
    /**
     * 获取数据类型名
     * @param dataType
     * @return
     */
    public static String getDataTypeName(int dataType) {
        switch (dataType) {
            case EMPLOYEE : 
                return "从业人员";
            case PROJECT: 
                return "项目";
            case PROJECTRECORD: 
                return "备案记录";
            case COMPANY: 
                return "企业信息";  
            case YINGJIANWUFANG: 
                return "硬件物防"; 
            case JIFANG: 
                return "技防"; 
            case PROJECTFILE: 
                return "图片材料";   
            case JOURNALYINGYE: 
                return "营业日志";   
            case JOURNALINSPECT: 
                return "巡检记录";   
            case PROJECTCONDITION: 
                return "项目材料";   
            default:
                return "";
        }
    }
}
