package com.dhht.controller.web;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.SMSCodeDao;
import com.dhht.model.SMSCode;
import com.dhht.model.User;
import com.dhht.service.punish.PunishService;
import com.dhht.service.user.UserLoginService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class SmsController extends BaseController{

    @Autowired
    private SMSCodeDao smsCodeDao;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private PunishService punishService;
    @Value("${sms.template.makedepartmentpunish}")
    private int makedepartmentpunish ;
    @Autowired
    private UserPasswordService userPasswordService;


    public static Logger logger = LoggerFactory.getLogger(SmsController.class);

    //验证码
    /**
     * 获取验证码
     */
    @Log("获取验证码")
    @RequestMapping(value = "seal/record/getCheckCode")
    public JsonObjectBO getCheckCode(HttpServletRequest request, @RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String  telphone = (String)map.get("telphone");

        try{
            if (userPasswordService.getCheckAgentCode(telphone)== ResultUtil.isSuccess){
                return JsonObjectBO.success("获取验证码成功",null);
            }else{
                return  JsonObjectBO.error("获取验证码失败");
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("获取验证码失败");
        }
    }



}
