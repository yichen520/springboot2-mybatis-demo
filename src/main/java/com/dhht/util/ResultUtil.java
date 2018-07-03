package com.dhht.util;

import com.dhht.common.JsonObjectBO;

public class ResultUtil {
    public static final String isHave = "用户已经存在";
    public static final String isSuccess = "操作成功";
    public static final String isfail = "操作失败";
    public static final String isException ="发生异常";
    public static final String isError = "发生错误";

    public static JsonObjectBO getResult(int type){
        switch (type){
            case 1:
                return JsonObjectBO.ok(isSuccess);
            case 2:
                return JsonObjectBO.error(isfail);
            case 3:
                return JsonObjectBO.error(isHave);
            case 4:
                return JsonObjectBO.exception(isException);
            case 5:
                return JsonObjectBO.error(isError);
                default:
                    return null;
        }
    }
}
