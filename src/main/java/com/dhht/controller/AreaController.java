package com.dhht.controller;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.User;
import com.dhht.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/8/16.
 */
@RestController
@RequestMapping(value = "/area")
public class AreaController {

    @Autowired
    private UserService userService;



    @RequestMapping("/getTree")
    public JsonObjectBO getTrees(){
        //获取当前登录用户
        User user = new User();
        return null;
    }

}