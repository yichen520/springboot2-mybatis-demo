package com.dhht.service.tools;

import com.dhht.common.JsonObjectBO;

import java.util.ArrayList;

/**
 * 短信发送服务接口
 *
 * @author 赵兴龙
 * @date 2018.6.22
 */
public interface SmsSendService {

    /**
     * 密码重置验证码模板ID
     */
    int TEMPLATE_CODE_RESET_PASSWORD = 63278;

    /**
     * 中国短信号码地区代号
     */
    String NATION_CODE_CHINA = "86";

    /**
     * 通过模板发送短信给单个手机号
     * @param phoneNumber 手机号码
     * @param templateId 模板代号
     * @param params 模板中需要的查询，数量与模板中定义的一致
     * @return true：发送成功、false：发送失败
     */
    boolean sendSingleMsgByTemplate(String phoneNumber, int templateId, ArrayList<String> params);

    void  sendPhoneMessage(String phone,ArrayList<String> params);

    JsonObjectBO sendMessage(String phone,String code);
}
