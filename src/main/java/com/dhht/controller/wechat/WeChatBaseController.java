package com.dhht.controller.wechat;



import com.dhht.util.UUIDUtil;
import com.dhht.model.WeChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @ModelAttribute
    public  void init(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        if(httpServletRequest.getCookies()!=null){
            return;
        }
        if(httpServletRequest.getCookies()==null&&httpServletRequest.getRequestURI().contains("/weChat/login")){
            Cookie cookie=new Cookie("JSESSIONID",UUIDUtil.generate());
            //设置Maximum Age
            cookie.setMaxAge(3600);
            //设置cookie路径为当前项目路径
            cookie.setPath(httpServletRequest.getContextPath());
            //添加cookie
            httpServletResponse.addCookie(cookie);
        }

    }

    public WeChatUser currentUser(){
        WeChatUser user = (WeChatUser) request.getSession().getAttribute("user");
        return user;
    }
}
