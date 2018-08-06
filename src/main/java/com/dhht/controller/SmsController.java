package com.dhht.controller;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.SMSCodeDao;
import com.dhht.model.SMSCode;
import com.dhht.service.user.UserLoginService;
import com.dhht.service.user.UserService;
import com.dhht.sms.SmsSingleSender;
import com.dhht.sms.SmsSingleSenderResult;
import com.dhht.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private UserLoginService userLoginService;

    public static Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Log("验证手机号")
    @RequestMapping(value ="checkPhone", method = RequestMethod.POST)
    public JsonObjectBO checkPhone(@RequestBody SMSCode smsCode){
        try {
            return userLoginService.checkPhoneAndIDCard(smsCode);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("发送短信发生异常");
        }
    }


}
