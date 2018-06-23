package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.District;
import com.dhht.model.SMSCode;
import com.dhht.model.Users;
import com.github.pagehelper.PageInfo;
import com.dhht.model.UserDomain;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface UserService {



    JsonObjectBO addUser(Users users);
    JsonObjectBO Update(Users users);
    JsonObjectBO deleteuser(String id);
    int changePwd(String id , String password);
    JsonObjectBO find(String realName,String roleId,String regionId,int pageNum, int pageSize);
    JsonObjectBO activeLocking(String loginTime);

    Users validate(Users users);

    PageInfo<Users> selectByDistrict(String id,int pageSum,int pageNum);

    JsonObjectBO checkPhoneAndIDCard(SMSCode smsCode);
    Map<String,Object> validateUser(HttpServletRequest request,UserDomain userDomain);
    }


