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
     * 通过模板发送短信给单个手机号
     * @param phoneNumber 手机号码
     * @param templateId 模板代号
     * @param params 模板中需要的查询，数量与模板中定义的一致
     * @return true：发送成功、false：发送失败
     */
    boolean sendSingleMsgByTemplate(String phoneNumber, int templateId, ArrayList<String> params);


}
