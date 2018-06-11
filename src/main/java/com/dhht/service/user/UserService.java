package com.dhht.service.user;

import com.dhht.model.Users;
import com.github.pagehelper.PageInfo;
import com.dhht.model.UserDomain;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface UserService {

    int validateUserLoginOne(UserDomain userDomain);
    int validateUserLoginTwo(UserDomain userDomain);
    int validateUserLoginThree(UserDomain userDomain);

    int addUser(Users users);
    int Update(Users users);
    int deleteSuser(String id);
    PageInfo<Users> findAllSuser(int pageNum, int pageSize);
    int changePwd(String id , String password);

    Users validate(Users users);
}
