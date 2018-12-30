package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.service.user.WeChatUserService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fyc 2018/12/29
 */
@RestController
@RequestMapping("/weChat")
public class LoginController {
    @Autowired
    private WeChatUserService weChatUserService;

    /**
     * 微信用户登入
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/login")
    public Map<String,Object> login(@RequestBody Map map, HttpServletRequest httpServletRequest){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            String mobilePhone = (String) map.get("mobilePhone");
            String verificationCode = (String) map.get("verificationCode");
            resultMap = weChatUserService.isLogin(mobilePhone,verificationCode,httpServletRequest);
            return resultMap;
        }catch (Exception e){
            resultMap.put("status", "error");
            resultMap.put("currentAuthority", "guest");
            resultMap.put("message","登入异常");
            return resultMap;
        }
    }

    /**
     * 获取验证码
     * @param map
     * @return
     */
    @RequestMapping("/verificationCode")
    public JsonObjectBO sendVerificationCode(@RequestBody Map map){
        try {
            String mobilePhone = (String)map.get("mobilePhone");
            int result = weChatUserService.sendMessage(mobilePhone);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            return JsonObjectBO.exception("发送验证码异常");
        }
    }

    /**
     * 获取当前用户的手机号
     * @param httpSession
     * @return
     */
    @RequestMapping("/currentUserMobilePhone")
    public JsonObjectBO currentUserMobilePhone(HttpSession httpSession){
        try {
            String mobilePhone = (String) httpSession.getAttribute("mobilePhone");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobilePhone",mobilePhone);
            return JsonObjectBO.success("获取成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取当前用户失败");
        }
    }

    /**
     * 退出登入
     * @param request
     * @return
     */
    @RequestMapping(value ="/logout")
    public Map<String,Object> login(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        try {
            request.getSession().invalidate();
            map.put("status", "ok");
            map.put("message","退出登录成功");
            return map;
        } catch (Exception e) {
            map.put("status", "error");
            map.put("message","登出错误");
            return map;
        }
    }
}
