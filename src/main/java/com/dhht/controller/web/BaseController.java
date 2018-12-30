package com.dhht.controller.web;


import com.dhht.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

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


    /**
     *
     */


}
