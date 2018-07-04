package com.dhht.common;

import com.alibaba.fastjson.JSONObject;
import com.dhht.model.User;

import javax.servlet.http.HttpSession;

public class CurrentUser {
    public static User currentUser(HttpSession session){
        Object  obj = session.getAttribute("user");
        User account = (User) obj;
        return  account;
    }
}
