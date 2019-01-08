package com.dhht.controller.wechat;



import com.dhht.model.WeChatUser;
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

    public WeChatUser currentUser(){
        WeChatUser user = (WeChatUser) request.getSession().getAttribute("user");
        return user;
    }
}
