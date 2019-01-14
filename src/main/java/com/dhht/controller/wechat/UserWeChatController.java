package com.dhht.controller.wechat;


import com.dhht.common.JsonObjectBO;
import com.dhht.model.WeChatUser;
import com.dhht.service.user.WeChatUserService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weChat/weChatUser")
public class UserWeChatController extends  WeChatBaseController{

    @Autowired
    private WeChatUserService weChatUserService;

    /**
     * 微信用户查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectWeChatUser" , method = RequestMethod.GET)
    public Map<String,Object> selectWeChatUser(HttpServletRequest httpServletRequest){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            String telphone = (String) httpServletRequest.getSession().getAttribute("mobilePhone");
            resultMap = weChatUserService.selectWeChatUser(telphone);
            return resultMap;
        }catch (Exception e){
            resultMap.put("status", "error");
            resultMap.put("message","登入异常");
            return resultMap;
        }
    }

    /**
     * 修改用户
     * @param
     * @return
     */
    @RequestMapping(value = "/updateWeChatUser" , method = RequestMethod.POST)
    public JsonObjectBO updateWeChatUser(@RequestBody WeChatUser weChatUser){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            String id = weChatUser.getId();
            int result = weChatUserService.updateWeChatUser(weChatUser,id);
            if(result==ResultUtil.isFail){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
            }else {
                jsonObjectBO.setCode(2);
                jsonObjectBO.setMessage("修改成功");
            }
            return jsonObjectBO;
        }catch (Exception e){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("修改失败");
            return jsonObjectBO;
        }
    }

}
