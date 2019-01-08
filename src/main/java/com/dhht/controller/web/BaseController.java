package com.dhht.controller.web;


import com.dhht.model.User;
import com.dhht.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xzp
 * @date 2018/10/23 10:43
 */
@Controller
public class BaseController {
    @Autowired
    public HttpServletRequest request;
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * @return 获取用户信息
     */
    public User currentUser(){
        User obj = (User)request.getSession().getAttribute("user");

        return  obj;
    }


    @ModelAttribute
    public  void init(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        Cookie cookie=new Cookie("JSESSIONID",UUIDUtil.generate());
        //设置Maximum Age
        cookie.setMaxAge(3600);
        //设置cookie路径为当前项目路径
        cookie.setPath(httpServletRequest.getContextPath());
        //添加cookie
        httpServletResponse.addCookie(cookie);
//        if(httpServletRequest.getCookies()==null){
//            httpServletResponse.setStatus(401);
//        }


    }


}
