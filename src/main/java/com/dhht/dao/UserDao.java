package com.dhht.dao;


import com.dhht.model.UserDomain;
import com.dhht.model.User;
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

    List<User> find(@Param("realName")String realName, @Param("roleId")String roleId, @Param("regionId")String regionId);

    User findByNo(@Param("id") String id);

    int findByPhone(@Param("phone") String phone);

    List<User> findUserByRegionId(String regionId);

    int changePwd(@Param("id") String id ,@Param("password") String password);

    //根据Code查找用户，表中字段为user_name
    User findByUserName(String userName);

    int updateUserDepartment(User user);
    //根据区域ID查询用户
    List<User> selectByDistrict(@Param("id") String ID);

    User findByTelphone(@Param("telphone") String telphone);

    User findById(@Param("id") String id);

}
