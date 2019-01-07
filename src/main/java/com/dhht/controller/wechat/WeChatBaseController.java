package com.dhht.controller.wechat;



import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WeChatBaseController {

    @Autowired
    public HttpServletRequest request;

    /**
     * @return 获取用户手机号
     */
    public  JsonObjectBO currentUserMobilePhone(){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String mobilePhone = (String) request.getSession().getAttribute("mobilePhone");
        if(mobilePhone==null){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("session过期");
            return jsonObjectBO;
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobilePhone",mobilePhone);
            return JsonObjectBO.success("查询成功",jsonObject);
        }

    }
}
