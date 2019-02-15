package com.dhht.util;

import com.dhht.common.JsonObjectBO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;

public class ResultUtil {

    @Autowired
    private static HttpServletResponse response;
    public static final int isHave = 2;
    public static final int isSuccess = 1;
    public static final int isFail = 0;
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
    public static final int isNoTrue = 18;
    public static final int isNoProxy = 19;
    public static final int isNoName = 20;
    public static final int isNoChipSeal = 21;
    public static final int faceCompare = 22;
    public static final int isLoss = 23;
    public static final int isLogout = 24;
    public static final int isIncidenceId = 25;
    public static final int isSmallSealCode = 26;
    public static final int isNoLoginUser = 50;
    public static final int isCodeError = 51;
    public static final int isSendVerificationCode=27;
    public static final int isNoSealVerification=29;
    public static final int isNoSeal=53;
    public static final int isNoEms=28;
    public static final int isNoSession=54;
    public static final int isNoMatchUseDepartment=55;
    public static final int cancelOrederFail = 56;
    public static final int cancelOrderOk = 57;
    public static final int refundOrderOk = 58;
    public static final int orderError = 59;
    public static String sealType(String sealType){
        switch (sealType){
            case "01":
                return "单位专用印章";
            case "02":
                return "财务专用章";
            case "03":
                return "税务专用章";
            case "04":
                return "合同专用章";
            case "05":
                return "法定代表人名章";
            case "99":
                return "其他类型印章";
            default:
                return "其他类型印章";
        }
    }

    public static JsonObjectBO getResult(int type){
        switch (type){
            case 0:
                return JsonObjectBO.error("操作失败");
            case 1:
                return JsonObjectBO.ok("操作成功");
            case 2:
                return JsonObjectBO.error("用户已存在");
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
                return JsonObjectBO.error("该印章信息重复,法定章或公章已经存在");
            case 16:
                return JsonObjectBO.error("备案单位或制作单位不存在");
            case 17:
                return JsonObjectBO.error("从业人员不存在");
            case 18:
                return JsonObjectBO.error("原密码输入错误");
            case 19:
                return JsonObjectBO.error("无授权委托书");
            case 20:
                return JsonObjectBO.error("请输入姓名");
            case 21:
                return JsonObjectBO.error("该印章不是芯片章");
            case 22:
                return JsonObjectBO.error("出现未知错误");
            case 23:
                return JsonObjectBO.error("该印章已被挂失");
            case 24:
                return JsonObjectBO.error("该印章已被注销");
            case 25:
                return JsonObjectBO.error("案件编号不可以重复");
            case 26:
                return JsonObjectBO.error("印章流水号设置值小于已有流水号");
            case 27:
                return JsonObjectBO.ok("验证码已发送");
            case 28:
                return JsonObjectBO.error("无ems信息");
            case 29:
                return JsonObjectBO.error("未核验通过，请重新核验");
            case 50:
                return JsonObjectBO.error("会话失效,请重新登录");
            case 51:
                return JsonObjectBO.error("验证码错误,请重新输入");
            case 53:
                return JsonObjectBO.error("印章刻制原因选择错误,该企业还没有公章");
            case 54:
                response.setStatus(401);
                return JsonObjectBO.error("session失效,请重新登录");
            case 56:
                return JsonObjectBO.error("该订单无法取消，请联系客服");
            case 57:
                return JsonObjectBO.ok("订单已取消");
            case 58:
                return JsonObjectBO.ok("订单退款成功");
            case 59:
                return JsonObjectBO.error("操作发生异常");
                default:
                    return null;
        }
    }
}
