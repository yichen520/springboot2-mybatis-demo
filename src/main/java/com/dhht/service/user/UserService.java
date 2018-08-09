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


    int delete(String id); //用户通过Id删除

    PageInfo<User> find(User user,String realName,String roleId,String districtId,int pageNum, int pageSize); //按照用户名称,角色,区域id进行查询

    PageInfo<User> selectByDistrict(String id,int pageSum,int pageNum);// 根据区域查找用户

    int deleteByUserName(String roleId, String telphone);  //根据用户账号删除用户,用于其他模块调用

    User findByUserName(String userName); //根据用户账号查找用户

    int insert(String telphone,String roleId,String realName,String districtId); //添加用户 输入项为手机号,角色id,用户名称,区域id

    int update(String oldTelPhone,String telPhone,String roleId,String realName,String districtId);  //更新用户  输入项为老手机号,新手机号码,角色id,用户名,区域id

    User findById(String Id);//更加id查找user

}


