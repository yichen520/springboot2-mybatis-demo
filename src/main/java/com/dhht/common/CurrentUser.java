package com.dhht.common;

import com.dhht.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CurrentUser {
    public static User currentUser(HttpSession session){
        Object  obj = session.getAttribute("user");
        User account = (User) obj;
        return  account;
    }

    public static boolean validatetoken(HttpServletRequest httpServletRequest){
        String sessiontoken="";
        sessiontoken= (String)httpServletRequest.getSession().getAttribute("token");

        String token = httpServletRequest.getHeader("token");
        if(sessiontoken!=null&&sessiontoken.equals(token)&&!sessiontoken.isEmpty()){
            return true;
        }else {
            return false;
        }
    }
}
