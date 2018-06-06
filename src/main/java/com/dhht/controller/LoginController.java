package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.ResponseBo;
import com.dhht.model.UserDomain;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.user.UserService;
import com.dhht.util.MD5Util;
import com.dhht.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import com.google.code.kaptcha.Constants;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    public static final String admin_userAccount = "admin";
    public static final String admin_password = "111111";

//    @RequestMapping(value ="/login", method = RequestMethod.POST)
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
            UserDomain user = new UserDomain();
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


//    @RequestMapping("/")
//    public String redirectIndex() {
//        return "redirect:/index";
//    }
//    @RequestMapping("/index")
//    public String index(Model model) {
//        // 登录成后，即可通过Subject获取登录的用户信息
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        model.addAttribute("user", user);
//        return "index";
//    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseBo login(@ModelAttribute("user")UserDomain user) {
        String username = StringUtil.stringNullHandle(user.getUsername());
        String password = StringUtil.stringNullHandle(user.getPassword());

//        String validateCode = StringUtil.stringNullHandle(user.getValidateCode());
        String role = StringUtil.stringNullHandle(user.getRole());
        // 密码MD5加密
        password = MD5Util.toMd5(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password,role);
        // 获取Subject对象
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return ResponseBo.ok();
        } catch (UnknownAccountException e) {
            return ResponseBo.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return ResponseBo.error(e.getMessage());
        } catch (LockedAccountException e) {
            return ResponseBo.error(e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseBo.error("认证失败！");
        }
    }

}
