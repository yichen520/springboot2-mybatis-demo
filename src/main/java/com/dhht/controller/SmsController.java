package com.dhht.controller;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.SMSCodeDao;
import com.dhht.model.SMSCode;
import com.dhht.service.user.UserService;
import com.dhht.sms.SmsSingleSender;
import com.dhht.sms.SmsSingleSenderResult;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@RestController
public class SmsController {

    @Autowired
    private SMSCodeDao smsCodeDao;

    @Autowired
    private UserService userService;




    @RequestMapping(value ="sendMessage", method = RequestMethod.POST)
    public JsonObjectBO sendMessage(@RequestBody Map map) {
        try {
            if (map == null){
                return JsonObjectBO.error("没有填写手机号");
            }else {
                String phone = map.get("phone").toString();
                ArrayList<String> params = new ArrayList<String>();
                //生成6位随机数
                String code = "";
                for(int i=1;i<=6;i++){
                    int d=(int)(Math.random()*9+1);
                    code+=d;
                }
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

    //@Log("验证手机号")
    @RequestMapping(value ="checkPhone", method = RequestMethod.POST)
    public JsonObjectBO checkPhone(@RequestBody SMSCode smsCode){
        try {
            return userService.checkPhoneAndIDCard(smsCode);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("发送短信发生异常");
        }
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

        }

    }
}
