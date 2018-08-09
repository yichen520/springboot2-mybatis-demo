package com.dhht.dao;

import com.dhht.model.OperatorRecordDetail;

public interface OperatorRecordDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(OperatorRecordDetail record);

    int insertSelective(OperatorRecordDetail record);

    OperatorRecordDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OperatorRecordDetail record);

    int updateByPrimaryKey(OperatorRecordDetail record);
}