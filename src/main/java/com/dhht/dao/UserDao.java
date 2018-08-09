package com.dhht.dao;


import com.dhht.model.UserDomain;
import com.dhht.model.User;
import com.dhht.model.UserSimple;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    int validateUserLoginOne(UserDomain userDomain);
    int validateUserLoginTwo(UserDomain userDomain);
    int validateUserLoginThree(UserDomain userDomain);

    int addUser(User user);

    int update(User user);

    int delete(@Param("id") String id);

    List<User> findAllSuser();

    List<User> find(@Param("realName")String realName,@Param("districtId")String districtId, @Param("roleId")String roleId ,@Param("role") String role);


    User findByNo(@Param("id") String id);

    int findByPhone(@Param("phone") String phone);

    List<User> findUserByRegionId(String regionId);

    int changePwd(@Param("id") String id ,@Param("password") String password);

    //根据Code查找用户，表中字段为user_name
    User findByUserName(String userName);

    int updateUserDepartment(User user);
    //根据区域ID查询用户
    List<User> selectByDistrict(@Param("id") String ID);

    List<User> selectByDistrict1(@Param("id") String ID,@Param("role") String role);

    User findByTelphone(@Param("telphone") String telphone);

    User findById(@Param("id") String id);

    int updateLock(@Param("id") String id);

    int updateUnLock(@Param("id") String id);

    int updateErrorTimes(@Param("username") String username);

    int updateErrorTimesZero(@Param("username") String username);

   int updateUser(User users);

   //根据电话删除用户
    int deleteByTelphone(String telphone);

    //根据用户名删除用户
    int deleteByUserName(@Param("userName") String userName);

    List<User> findAll();

    List<UserSimple> getRoleUser(@Param("districtId")String districtId, @Param("roleId")String roleId);
}
