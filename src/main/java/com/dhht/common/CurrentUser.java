package com.dhht.common;

import com.alibaba.fastjson.JSON;
import com.dhht.model.DistrictMenus;
import com.dhht.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CurrentUser {

    @Autowired
    private StringRedisTemplate template;

    @Value("${expireTime}")
    private long expireTime;

    public static User currentUser(HttpSession session){
        Object  obj = session.getAttribute("user");
        User account = (User) obj;
        return  account;
    }

}
