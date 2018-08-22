package com.dhht.util;

import com.dhht.common.JsonObjectBO;

public class ResultUtil {
    public static final int isHave = 1;
    public static final int isSuccess = 2;
    public static final int isFail = 3;
    public static final int isException =4;
    public static final int isError = 5;
    public static final int isDistrict=6;
    public static final int isLock=7;
    public static final int isUnlock=8;
    public static final int isUploadFail=9;
    public static final int isWrongId = 10;
    public static final int isSend = 11;
    public static final int isNoSend = 12;
    public static final int isHaveCode = 13;
    public static final int noRecordDepartment = 14;
    public static final int isHaveSeal = 15;
    public static final int isNoDepartment = 16;
    public static final int isNoEmployee = 17;


    public static JsonObjectBO getResult(int type){
        switch (type){
            case 1:
                return JsonObjectBO.error("用户已存在");
            case 2:
                return JsonObjectBO.ok("操作成功");
            case 3:
                return JsonObjectBO.error("操作失败");
            case 4:
                return JsonObjectBO.exception("发生未知异常");
            case 5:
                return JsonObjectBO.error("发生未知错误");
            case 6:
                return JsonObjectBO.error("该区域只能添加一个备案单位");
            case 7:
                return JsonObjectBO.error("该用户已经锁定,不能重复锁定");
            case 8:
                return JsonObjectBO.error("该账号未加锁");
            case 9:
                return JsonObjectBO.error("文件上传失败");
            case 10:
                return JsonObjectBO.error("重复的身份证号");
            case 11:
                return JsonObjectBO.ok("用户创建成功");
            case 12:
                return JsonObjectBO.error("用户创建失败");
            case 13:
                return JsonObjectBO.error("已经使用的社会统一信用编码");
            case 14:
                return JsonObjectBO.error("该区域暂时没有备案单位，请联系管理员");
            case 15:
                return JsonObjectBO.error("法定专用章印章信息重复");
            case 16:
                return JsonObjectBO.error("备案单位或制作单位不存在");
            case 17:
                return JsonObjectBO.error("从业人员不存在");
                default:
                    return null;
        }
    }
}
