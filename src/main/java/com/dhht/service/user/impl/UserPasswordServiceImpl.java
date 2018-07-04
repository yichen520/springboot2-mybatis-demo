package com.dhht.service.user.impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.SMSCodeDao;
import com.dhht.model.SMSCode;
import com.dhht.model.UseDepartment;
import com.dhht.service.user.UserPasswordService;
import com.dhht.sms.SmsSingleSender;
import com.dhht.sms.SmsSingleSenderResult;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cuiyang on 2018/7/3.
 */
@Service(value = "userPasswordService")
@Transactional
public class UserPasswordServiceImpl implements UserPasswordService{

    @Autowired
    private SMSCodeDao smsCodeDao;

    @Value("${sms.appId}")
    private int appid ;

    @Value("${sms.appKey}")
    private String appkey ;

    @Value("${sms.tmplId}")
    private int tmplId ;

    @Value("${sms.nationCode}")
    private String nationCode ;




    @Override
    public void sendPhoneMessage(String phone,ArrayList<String> params){
        try {
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
    public JsonObjectBO sendMessage(String phone, String code) {
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
