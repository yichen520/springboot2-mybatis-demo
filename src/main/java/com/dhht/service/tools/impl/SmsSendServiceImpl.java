package com.dhht.service.tools.impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.SMSCodeDao;
import com.dhht.model.SMSCode;
import com.dhht.service.tools.SmsSendService;
import com.dhht.sms.SmsSingleSender;
import com.dhht.sms.SmsSingleSenderResult;
import com.dhht.util.UUIDUtil;
import org.json.JSONException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 短信发送服务
 *
 * @author 赵兴龙
 * @date 2018.6.22
 */
@Service(value = "smsService")
public class SmsSendServiceImpl implements SmsSendService, InitializingBean {

    @Resource
    private SmsAppConfigInfo smsAppConfigInfo;

    private SmsSingleSender sender;

    @Autowired
    private SMSCodeDao smsCodeDao;

//    @Value("${sms.nationCode}")
//    private String nationCode ;
    /**
     * 通过模板发送短信给单个手机号<br />
     * 仅能发送至国内手机号
     * @param phoneNumber 手机号码
     * @param templateId 模板代号
     * @param params 模板中需要的查询，数量与模板中定义的一致
     * @return true：发送成功、false：发送失败
     */
    @Override
    public boolean sendSingleMsgByTemplate(String phoneNumber, int templateId, ArrayList<String> params) {
        try {
            SmsSingleSenderResult result = sender.sendWithParam(SmsSendService.NATION_CODE_CHINA, phoneNumber, templateId, params, "", "", "");
            if(result.result == 0) {
                return true;
            }
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 初始化短信发送接口
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        sender = new SmsSingleSender(smsAppConfigInfo.getAppId(), smsAppConfigInfo.getAppKey());
    }





}
