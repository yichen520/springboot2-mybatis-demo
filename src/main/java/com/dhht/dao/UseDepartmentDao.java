package com.dhht.dao;


import com.dhht.model.UseDepartment;


import java.util.List;

/**
 * Created by imac_dhht on 2018/6/12.
 */
public interface UseDepartmentDao {

    int deleteByPrimaryKey(String code);

    int insert(UseDepartment record);

    UseDepartment selectByPrimaryKey(String userdepartmentCode);

//    int updateByPrimaryKeySelective(UserDepartment record);

    int updateByPrimaryKey(UseDepartment record);

    List<UseDepartment> findAllMake();
}
