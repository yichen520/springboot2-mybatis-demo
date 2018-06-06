package com.dhht.dao;

import com.dhht.model.Makedepartment;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MakedepartmentMapper {
    int deleteByPrimaryKey(String makedepartmentCode);

    int insert(Makedepartment record);

    int insertSelective(Makedepartment record);

    Makedepartment selectByPrimaryKey(String makedepartmentCode);

    int updateByPrimaryKeySelective(Makedepartment record);

    int updateByPrimaryKey(Makedepartment record);

    List<Makedepartment> findAllMake();

    int validateUserAccout(String code);
}