package com.dhht.controller.wechat;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WeChatBaseController {

    @Autowired
    public HttpServletRequest request;

    /**
     * @return 获取用户手机号
     */
    public  String currentUserMobilePhone(){
        String mobilePhone = (String) request.getSession().getAttribute("mobilePhone");
        return mobilePhone;
    }
}
