package com.dhht.dao;


import com.dhht.model.UserDomain;
import com.dhht.model.Users;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    int validateUserLoginOne(UserDomain userDomain);
    int validateUserLoginTwo(UserDomain userDomain);
    int validateUserLoginThree(UserDomain userDomain);

    int addUser(Users users);

    int update(Users users);

    int delete(@Param("id") String id);

    List<Users> findAllSuser();

    Users findByNo(@Param("id") String id);

    List<Users> findUserByRegionId(String regionId);

    int changePwd(@Param("id") String id ,@Param("password") String password);

}