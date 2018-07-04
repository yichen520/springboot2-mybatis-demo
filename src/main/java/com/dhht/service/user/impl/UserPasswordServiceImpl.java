package com.dhht.service.user.impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.SMSCodeDao;
import com.dhht.model.SMSCode;
import com.dhht.model.UseDepartment;
import com.dhht.service.tools.SmsSendService;
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
    @Autowired
    private SmsSendService smsSendService;

    @Value("${sms.template.insertUser}")
    private int userCode ;

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
                smsSendService.sendSingleMsgByTemplate(phone,userCode,params);
                return JsonObjectBO.ok("短信发送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("发生异常");
        }
    }
}
