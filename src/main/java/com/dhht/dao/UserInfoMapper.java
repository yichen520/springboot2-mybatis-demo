package com.dhht.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer uid);
//
//    int insert(UserInfo record);
//
//    int insertSelective(
//            UserInfo record);
//
//    UserInfo selectByPrimaryKey(Integer uid);
//
//    int updateByPrimaryKeySelective(UserInfo record);
//
//    int updateByPrimaryKey(UserInfo record);
}