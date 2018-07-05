package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.SMSCode;
import com.dhht.model.User;
import com.dhht.model.UserDomain;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * Created by 崔杨 on 2018/4/19.
 */
public interface UserLoginService {

    JsonObjectBO checkPhoneAndIDCard(SMSCode smsCode);

    Map<String,Object> validateUser(HttpServletRequest request,UserDomain userDomain);

    User validate(User user);


}


