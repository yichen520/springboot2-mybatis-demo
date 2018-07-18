package com.dhht.dao;

import com.dhht.model.EmployeePunishRecord;

public interface EmployeePunishRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(EmployeePunishRecord record);

    int insertSelective(EmployeePunishRecord record);

    EmployeePunishRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EmployeePunishRecord record);

    int updateByPrimaryKey(EmployeePunishRecord record);
}