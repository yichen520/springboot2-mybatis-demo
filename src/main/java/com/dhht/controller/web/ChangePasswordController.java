package com.dhht.controller.web;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.User;
import com.dhht.service.user.UserPasswordService;
import com.dhht.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ChangePasswordController {
    private static Logger logger = LoggerFactory.getLogger(ChangePasswordController.class);

    @Autowired
    private UserPasswordService userPasswordService;

    @Log("密码修改")
    @RequestMapping(value = "/changePwd")
    public JsonObjectBO changePwd(HttpServletRequest httpServletRequest, @RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        String username =  user.getUserName();
        String oldPassword = (String)map.get("oldPassword");
        String newPassword = (String)map.get("newPassword");

        try {
            int result =  userPasswordService.changePwd(username,oldPassword,newPassword);
            if(result== ResultUtil.isSuccess){
                jsonObjectBO.setMessage("密码修改成功");
                jsonObjectBO.setCode(1);
            }else if(result==ResultUtil.isFail){
                jsonObjectBO.setMessage("密码修改成功");
                jsonObjectBO.setCode(-1);
            }else{
                jsonObjectBO.setMessage("原密码错误");
                jsonObjectBO.setCode(-1);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            jsonObjectBO.setMessage("出现未知错误");
            jsonObjectBO.setCode(-1);
        }
        return jsonObjectBO;
    }

}
