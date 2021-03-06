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

    int delete(String id);

    PageInfo<User> find(User user,String realName,String roleId,String districtId,int pageNum, int pageSize);

    PageInfo<User> selectByDistrict(String id,int pageSum,int pageNum);

    int deleteByUserName(String roleId, String telphone);

    User findByUserName(String userName);

    int insert(String telphone,String roleId,String realName,String districtId);

    int update(String oldTelPhone,String telPhone,String roleId,String realName,String districtId);

    User findById(String Id);

    List<User> selectByEmployeeRole();


}


