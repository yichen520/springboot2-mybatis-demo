package com.dhht.dao;


import com.dhht.model.UserDomain;
import com.dhht.model.Users;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserDao {
    int validateUserLoginOne(UserDomain userDomain);
    int validateUserLoginTwo(UserDomain userDomain);
    int validateUserLoginThree(UserDomain userDomain);
    Users validate(UserDomain userDomain);
}