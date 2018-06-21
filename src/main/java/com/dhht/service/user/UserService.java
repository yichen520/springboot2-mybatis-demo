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

    int validateUserLoginOne(UserDomain userDomain);
    int validateUserLoginTwo(UserDomain userDomain);
    int validateUserLoginThree(UserDomain userDomain);

    JsonObjectBO addUser(Users users);
    JsonObjectBO Update(Users users);
    JsonObjectBO deleteuser(String id);
    JsonObjectBO findAlluser(int pageNum, int pageSize);
    int changePwd(String id , String password);

    Users validate(Users users);

    PageInfo<Users> selectByDistrict(Integer id,int pageSum,int pageNum);
    }

   // List<District> getRegionsTrees(String regionId);

