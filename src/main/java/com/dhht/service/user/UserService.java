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
    int insert(User user);

    int update(User user);

    int delete(String id);

    PageInfo<User> find(User user,String realName,String roleId,String districtId,int pageNum, int pageSize);

    PageInfo<User> selectByDistrict(String id,int pageSum,int pageNum);

    int deleteByTelphone(String phone);

    User findByTelphone(String phone);

}


