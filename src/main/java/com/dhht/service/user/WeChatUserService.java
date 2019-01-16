package com.dhht.service.user;

import com.dhht.model.WeChatUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WeChatUserService {
    /**
     * 发送验证码
     * @param mobilePhone
     * @return
     */
    int sendMessage(String mobilePhone,int param);

    /**
     * 微信用户登入
     * @param mobilePhone
     * @param verificationCode
     * @param httpServletRequest
     * @return
     */
    Map<String,Object> isLogin(String mobilePhone,String  verificationCode, HttpServletRequest httpServletRequest);


    /**
     * 用户修改
     * @param weChatUser
     * @param id
     * @return
     */
    int updateWeChatUser(WeChatUser weChatUser,String id);


    /**
     * 用户查询
     * @param telphone
     * @return
     */
    Map<String,Object>  selectWeChatUser(String telphone);
}
