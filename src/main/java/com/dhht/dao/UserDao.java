package com.dhht.dao;


import com.dhht.model.UserDomain;

import java.util.List;

public interface UserDao {
    int validateUserLoginOne(UserDomain userDomain);
    int validateUserLoginTwo(UserDomain userDomain);
    int validateUserLoginThree(UserDomain userDomain);

}