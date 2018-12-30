package com.dhht.service.user;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WeChatUserService {
    /**
     * 发送验证码
     * @param mobilePhone
     * @return
     */
    int sendMessage(String mobilePhone);

    /**
     * 微信用户登入
     * @param mobilePhone
     * @param verificationCode
     * @param httpServletRequest
     * @return
     */
    Map<String,Object> isLogin(String mobilePhone,String  verificationCode, HttpServletRequest httpServletRequest);
}
