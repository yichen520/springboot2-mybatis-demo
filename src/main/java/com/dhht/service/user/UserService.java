package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.SMSCode;
import com.dhht.model.User;
import com.github.pagehelper.PageInfo;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface UserService {



    JsonObjectBO addUser(User user);
    JsonObjectBO Update(User user);
    JsonObjectBO deleteuser(String id);
    JsonObjectBO changePwd(String id);
    JsonObjectBO find(String realName,String roleId,String regionId,int pageNum, int pageSize);
    JsonObjectBO activeLocking(String loginTime);
    JsonObjectBO activeUnlocking(String id);

    User validate(User user);

    PageInfo<User> selectByDistrict(Integer id, int pageSum, int pageNum);

    JsonObjectBO checkPhoneAndIDCard(SMSCode smsCode);
    }


