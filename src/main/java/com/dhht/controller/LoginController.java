package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.RoleResourceDao;
import com.dhht.model.*;
import com.dhht.service.resource.ResourceService;
import com.dhht.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

       JsonObjectBO jsonObjectBO = new JsonObjectBO();
       Map<String,Object> map=new HashMap<>();

       try {
           User user1= new User();
           user1.setPassword(userDomain.getPassword());
           user1.setUserName(userDomain.getUsername());
           User user = userService.validate(user1);
           if(user==null){
               map.put("status", "error");
               map.put("currentAuthority", user.getRoleId());
               map.put("message","账号密码错误");
               return map;
           }
           if (user.getIsLocked()){
               map.put("status", "error");
               map.put("currentAuthority", "guest");
               map.put("message","该用户已被锁定，请联系管理员！");
               return map;
           }
           map.put("status", "ok");
           map.put("currentAuthority", user.getRoleId());
           map.put("message","登录成功");

           List<String> id = roleResourceDao.selectMenuResourceByID(user.getRoleId());
           List<String> resourceId = roleResourceDao.selectResourceByID(user.getRoleId());
           List<Menus> menus = resourceService.findMenusByRole(id);
           List<Resource> resources = resourceService.findResourceByRole(resourceId);
           request.getSession().setAttribute("user", user);
           request.getSession().setAttribute("menus", menus);
           request.getSession().setAttribute("resources", resources);
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
       User account = (User) obj;
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
