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
    public static final int RECORD = 4; //备案
    public static final int UPLOAD = 5; //印模上传
    public static final int PERSONAL = 6; //个人化
    public static final int DELIVER = 7; //交付
    public static final int LOSS = 8; //挂失
    public static final int LOGOUT = 9; //注销








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
            case RECORD:
                return "备案";
            case UPLOAD:
                return "印模上传";
            case PERSONAL:
                return "个人化";
            case DELIVER :
                return "交付";
            case LOSS :
                return "挂失";
            case LOGOUT :
                return "注销";
            case REALNAME :
                return "实名认证";
            case AGREE :
                return "审核";
            default:
                return "";
        }
    }
}
