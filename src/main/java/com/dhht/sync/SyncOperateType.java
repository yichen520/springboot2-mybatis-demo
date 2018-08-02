/**
 * 
 */
package com.dhht.sync;

/**
 * @author zxl
 *
 */
public class SyncOperateType {
    public static final int SAVE = 1; //新增
    public static final int UPDATE = 2; //修改
    public static final int DELETE = 3; //删除






    public static final int RESET_PASSWORD = 4; //重置密码
    public static final int LOCK = 5; //锁定
    public static final int UNLOCK = 6; //解锁

    public static final int CHECK = 7; //查询物流
    public static final int IMPORT = 8; //导入
    public static final int REFRESH = 9; //刷新
    public static final int REALNAME = 10; //实名认证
    public static final int  AGREE= 11; //审核
    
    /**
     * 或者操作类型名
     * @param operatType
     * @return
     */
    public static String getOperateTypeName(int operatType) {
        switch (operatType) {
            case SAVE:
                return "新增";
            case UPDATE:
                return "更新";
            case DELETE:
                return "删除";
            case RESET_PASSWORD:
                return "重置密码";
            case LOCK:
                return "锁定";
            case UNLOCK:
                return "解锁";
            case CHECK :
                return "查询物流";
            case IMPORT :
                return "导入";
            case REFRESH :
                return "刷新";
            case REALNAME :
                return "实名认证";
            case AGREE :
                return "审核";
            default:
                return "";
        }
    }
}
