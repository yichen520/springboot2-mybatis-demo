package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.District;
import com.dhht.model.Users;
import com.github.pagehelper.PageInfo;
import com.dhht.model.UserDomain;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface UserService {



    JsonObjectBO addUser(Users users);
    JsonObjectBO Update(Users users);
    JsonObjectBO deleteuser(String id);
    JsonObjectBO findAlluser(int pageNum, int pageSize);
    int changePwd(String id , String password);
    JsonObjectBO find(String realName,String roleId,String regionId,int pageNum, int pageSize);
    JsonObjectBO activeLocking(String loginTime);

    Users validate(Users users);

    PageInfo<Users> selectByDistrict(Integer id,int pageSum,int pageNum);
    }

   // List<District> getRegionsTrees(String regionId);

