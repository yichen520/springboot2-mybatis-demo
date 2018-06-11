package com.dhht.controller;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.Users;
import com.dhht.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
        Users users = new Users();
//        users.setId();
//        .getTrees(users);
        return null;
    }

}