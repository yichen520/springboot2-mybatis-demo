package com.dhht.dao;

import com.dhht.model.OperatorRecord;

public interface OperatorRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(OperatorRecord record);

    int insertSelective(OperatorRecord record);

    OperatorRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OperatorRecord record);

    int updateByPrimaryKey(OperatorRecord record);
}