package com.dhht.dao;


import com.dhht.model.UserDepartment;


import java.util.List;

/**
 * Created by imac_dhht on 2018/6/12.
 */
public interface UserDepartmentDao {

    int deleteByPrimaryKey(String userdepartmentCode);

    int insert(UserDepartment record);

    UserDepartment selectByPrimaryKey(String userdepartmentCode);

//    int updateByPrimaryKeySelective(UserDepartment record);

    int updateByPrimaryKey(UserDepartment record);

    List<UserDepartment> findAllMake();
}
