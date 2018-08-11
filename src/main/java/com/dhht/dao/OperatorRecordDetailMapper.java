package com.dhht.dao;

import com.dhht.model.OperatorRecordDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorRecordDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(OperatorRecordDetail record);

    int insertSelective(OperatorRecordDetail record);

    OperatorRecordDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OperatorRecordDetail record);

    int updateByPrimaryKey(OperatorRecordDetail record);

    List<OperatorRecordDetail> selectByOperateId(@Param("id") String id);
}