package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
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

   @RequestMapping(value ="login", method = RequestMethod.POST)
   public Map<String,Object> login(HttpServletRequest request,@RequestBody UserDomain userDomain){
       JsonObjectBO jsonObjectBO = new JsonObjectBO();
       JSONObject jsonObject = new JSONObject();
       Map<String,Object> map=new HashMap<>();

       try {
           Users user1= new Users();
           user1.setPassword(userDomain.getPassword());
           user1.setUserName(userDomain.getUsername());
           Users user = userService.validate(user1);
           if(user==null){
               map.put("status", "error");
               map.put("currentAuthority", user.getRoleId());
               map.put("message","账号密码错误");
               return map;
           }
           if (user.getLocked()){
               map.put("status", "error");
               map.put("currentAuthority", "guest");
               map.put("message","该用户已被锁定，请联系管理员！");
               return map;
           }
           map.put("status", "ok");
           map.put("currentAuthority", user.getRoleId());
           map.put("message","登录成功");

           List<String> id = roleResourceDao.selectResourceByID(user.getRoleId());
           List<Menus> menus = resourceService.findMenusByRole(id);
           request.getSession().setAttribute("user", user);
           request.getSession().setAttribute("menus", menus);
           return map;

       } catch (Exception e) {
           jsonObjectBO.setMessage("登录失败");
           jsonObjectBO.setCode(-1);
           map.put("status", "error");
           map.put("currentAuthority", "guest");
           map.put("message","登录失败！");
           return map;
       }
   }
    /**
     * 获取目录
     */
    @RequestMapping(value ="menu")
    public JsonObjectBO menu(HttpSession session){
        Object  obj = session.getAttribute("menus");
        List<Menus> account = (List<Menus>) obj;
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menu",account);
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





//    @RequestMapping(value ="/checkLogin", method = RequestMethod.POST)
//    public JsonObjectBO login(HttpServletRequest request,@ModelAttribute("user")UserDomain user){
//
//        JsonObjectBO jsonObjectBO = new JsonObjectBO();
//        JSONObject jsonObject = new JSONObject();
//
//        String userAccount = StringUtil.stringNullHandle(user.getUsername());
//        String password = StringUtil.stringNullHandle(user.getPassword());
//
//        String validateCode = StringUtil.stringNullHandle(user.getValidateCode());
//        String role = StringUtil.stringNullHandle(user.getRole());
//
//        String sessionCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        if(!StringUtil.isNotNull(validateCode) || !StringUtil.isNotNull(sessionCode)){
//            jsonObjectBO.setCode(-1);
//            jsonObjectBO.setMessage("验证码错误");
//            return jsonObjectBO;
//        }
//
//        validateCode = validateCode.toLowerCase();
//        sessionCode = sessionCode.toLowerCase();
//        if(!validateCode.equals(sessionCode)){
//            jsonObjectBO.setCode(-1);
//            jsonObjectBO.setMessage("验证码错误");
//            return jsonObjectBO;
//        }
//
//        if(!StringUtil.isNotNull(userAccount) || !StringUtil.isNotNull(password)){
//            jsonObjectBO.setCode(-1);
//            jsonObjectBO.setMessage("验证码错误");
//            return jsonObjectBO;
//        }
//
//        return validate(userAccount,password,role,jsonObjectBO);
//    }

    //判断各角色是否登陆
    public JsonObjectBO validate(String userAccount,String password,String role,JsonObjectBO jsonObjectBO){
        if (role.equals("1")){
            if (userAccount.equals(admin_userAccount)&&password.equals(admin_password)){
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("管理员登陆成功");
                return jsonObjectBO;
            }else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("角色选择有误或账号密码错误");
                return jsonObjectBO;
            }
        }
        else {
            //password = MD5Util.toMd5(password);
            UserDomain user = new UserDomain("1","2");
            user.setUsername(userAccount);
            user.setPassword(password);
            if (role.equals("2")){
                int num = userService.validateUserLoginOne(user);
                if (num>0){
                    jsonObjectBO.setCode(2);
                    jsonObjectBO.setMessage("制作单位登陆成功");
                    return jsonObjectBO;
                }else {
                    jsonObjectBO.setCode(-1);
                    jsonObjectBO.setMessage("角色选择有误或账号密码错误");
                    return jsonObjectBO;
                }
            }else if (role.equals("3")){
                int num = userService.validateUserLoginTwo(user);
                if (num>0){
                    jsonObjectBO.setCode(3);
                    jsonObjectBO.setMessage("门店管理人员登陆成功");
                    return jsonObjectBO;
                }else {
                    jsonObjectBO.setCode(-1);
                    jsonObjectBO.setMessage("角色选择有误或账号密码错误");
                    return jsonObjectBO;
                }
            }else if (role.equals("4")){
                int num = userService.validateUserLoginThree(user);
                if (num>0){
                    jsonObjectBO.setCode(4);
                    jsonObjectBO.setMessage("从业人员登陆成功");
                    return jsonObjectBO;
                }else {
                    jsonObjectBO.setCode(-1);
                    jsonObjectBO.setMessage("角色选择有误或账号密码错误");
                    return jsonObjectBO;
                }
            }
            else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("角色选择有误或账号密码错误");
                return jsonObjectBO;
            }
        }
    }

}
