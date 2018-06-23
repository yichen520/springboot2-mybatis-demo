package com.dhht.dao;

import com.dhht.model.UserDomain;
import com.dhht.model.User;
import com.dhht.model.UsersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper {
    int countByExample(UsersExample example);

    int deleteByExample(UsersExample example);

    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UsersExample example);

    User selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UsersExample example);

    int updateByExample(@Param("record") User record, @Param("example") UsersExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User validate(UserDomain users);
}