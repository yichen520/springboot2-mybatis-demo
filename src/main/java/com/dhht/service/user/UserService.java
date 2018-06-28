package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.SMSCode;
import com.dhht.model.User;
import com.github.pagehelper.PageInfo;
import com.dhht.model.UserDomain;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * Created by 崔杨 on 2018/4/19.
 */
public interface UserService  {
    JsonObjectBO insert(User user);

    JsonObjectBO update(User user);

    JsonObjectBO delete(String id);

    JsonObjectBO changePwd(String id);

    JsonObjectBO find(User user,String realName,String roleId,String districtId,int pageNum, int pageSize);

    PageInfo<User> selectByDistrict(String id,int pageSum,int pageNum);

    int deleteByTelphone(String phone);

//    JsonObjectBO deleteOther(String id);

    }


