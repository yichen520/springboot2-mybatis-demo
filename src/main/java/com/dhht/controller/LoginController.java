package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.AccessResult;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.RoleResourceDao;
import com.dhht.model.*;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.resource.ResourceService;
import com.dhht.service.user.UserService;
import com.dhht.util.MD5Util;
import com.dhht.util.StringUtil;
import org.apache.shiro.authc.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.code.kaptcha.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dhht.util.MenuUtil.genMenu;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleResourceDao roleResourceDao;
    public static final String admin_userAccount = "admin";
    public static final String admin_password = "111111";

    @Log("登录")
   @RequestMapping(value ="login", method = RequestMethod.POST)
   public Map<String,Object> login(HttpServletRequest request,@RequestBody UserDomain userDomain){

        return userService.validateUser(request,userDomain);

   }
    /**
     * 获取目录
     */
    @RequestMapping(value ="menu")
    public JsonObjectBO menu(HttpSession session){
        Object  obj = session.getAttribute("menus");
        List<Menus> account = (List<Menus>) obj;
        List<Map> menu = genMenu(account);
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menu",menu);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setMessage("获取目录成功");
        jsonObjectBO.setCode(1);
        return  jsonObjectBO;
    }

   @RequestMapping("currentUser")
   public JsonObjectBO currentUser(HttpSession session){
       Object  obj = session.getAttribute("user");
       Users account = (Users) obj;
       account.setPassword(null);
       JsonObjectBO jsonObjectBO = new JsonObjectBO();
       JSONObject jsonObject = new JSONObject();
       jsonObject.put("user",account);
       jsonObjectBO.setData(jsonObject);
       jsonObjectBO.setMessage("获取user成功");
       jsonObjectBO.setCode(1);
       return  jsonObjectBO;

   }

    @Log("退出登录")
    @RequestMapping(value ="logout")
    public Map<String,Object> login(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        try {
            request.getSession().invalidate();
            map.put("status", "ok");
            map.put("message","退出登录成功");
            return map;

        } catch (Exception e) {

            map.put("status", "error");
            map.put("message","登录失败！");
            return map;
        }
    }



}
