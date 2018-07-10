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


    public static JsonObjectBO getResult(int type){
        switch (type){
            case 1:
                return JsonObjectBO.error("用户已存在");
            case 2:
                return JsonObjectBO.ok("操作成功");
            case 3:
                return JsonObjectBO.error("操作失败");
            case 4:
                return JsonObjectBO.exception("发生异常");
            case 5:
                return JsonObjectBO.error("发生错误");
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
                default:
                    return null;
        }
    }
}
