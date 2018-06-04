package com.dhht.dao;

import com.dhht.model.Makedepartment;

import java.util.List;

public interface MakedepartmentMapper {
    int deleteByPrimaryKey(String makedepartmentCode);

    int insert(Makedepartment record);

    int insertSelective(Makedepartment record);

    Makedepartment selectByPrimaryKey(String makedepartmentCode);

    int updateByPrimaryKeySelective(Makedepartment record);

    int updateByPrimaryKey(Makedepartment record);

    List<Makedepartment> findAllMake();
}