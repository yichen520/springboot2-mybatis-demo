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




    public void sendPhoneMessage(String phone,ArrayList<String> params){
        try {
            int appid = 1400047268;
            String appkey = "5e0e87a6bc2f28ddc221b7de8386ffe1";
            String nationCode = "86";// 国家码  123456为您申请绑定的验证码，请于2分钟内填写。如非本人操作，请忽略本短信。
            int tmplId = 63278;
            SmsSingleSender singleSender;// 初始化单发
            SmsSingleSenderResult singleSenderResult = new SmsSingleSenderResult();
            // 初始化单发
            singleSender = new SmsSingleSender(appid, appkey);
            //同步发送
            singleSenderResult = singleSender.sendWithParam(nationCode, phone, tmplId,params, "", "", "");
            System.out.println(singleSenderResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 自定义密码 短信发送
     * @param phone
     * @param code
     * @return
     */
    @Override
    public JsonObjectBO sendMessage(String phone,String code) {
        try {
            if (phone == null){
                return JsonObjectBO.error("没有填写手机号");
            }else {
                ArrayList<String> params = new ArrayList<String>();
                params.add(code);
                SMSCode smscode= smsCodeDao.getSms(phone);
                if(smscode==null){
                    smscode = new SMSCode();
                    smscode.setId(UUIDUtil.generate());
                    smscode.setLastTime(new Date().getTime());
                    smscode.setPhone(phone);
                    smscode.setSmscode(code);
                    smsCodeDao.save(smscode);
                }else{
                    smscode.setLastTime(new Date().getTime());
                    smscode.setSmscode(code);
                    smsCodeDao.update(smscode);
                }
                sendPhoneMessage(phone, params);
                return JsonObjectBO.ok("短信发送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("发生异常");
        }
    }
}
