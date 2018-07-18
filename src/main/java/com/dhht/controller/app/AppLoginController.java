package com.dhht.controller.app;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.UserDomain;
import com.dhht.service.user.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AppLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @Log("app登录")
   @RequestMapping(value ="app/login", method = RequestMethod.POST)
   public JsonObjectBO login(HttpServletRequest request,@RequestBody UserDomain userDomain){
          return   userLoginService.validateAppUser(request, userDomain);
   }

    @Log("退出登录")
    @RequestMapping(value ="app/logout")
    public JsonObjectBO loginout(HttpServletRequest request){
        try {
            request.getSession().invalidate();
            return JsonObjectBO.success("退出登录成功",null);
        } catch (Exception e) {
            return JsonObjectBO.exception(e.getMessage());
        }
    }

}
