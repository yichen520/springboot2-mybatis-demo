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

    List<Users> find(@Param("realName")String realName,@Param("roleId")String roleId,@Param("regionId")String regionId);

    Users findByNo(@Param("id") String id);

    int findByPhone(@Param("phone") String phone);

    List<Users> findUserByRegionId(String regionId);

    int changePwd(@Param("id") String id ,@Param("password") String password);

    //根据Code查找用户，表中字段为user_name
    Users findByUserName(String userName);

    int updateUserDepartment(Users users);
    //根据区域ID查询用户
    List<Users> selectByDistrict(@Param("id") String ID);

    Users findByTelphone(@Param("telphone") String telphone);

    Users findById(@Param("id") String id);

    int updateErrorTimes(@Param("username") String username);
    int updateErrorTimesZero(@Param("username") String username);

   int updateUser(Users users);


}
