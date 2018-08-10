package com.dhht.dao;

import com.dhht.model.OperatorRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(OperatorRecord record);

    int insertSelective(OperatorRecord record);

    OperatorRecord selectByPrimaryKey(String id);

    List<OperatorRecord> selectOperatorRecordByFlag(@Param("flag") String flag);

    int updateByPrimaryKeySelective(OperatorRecord record);

    int updateByPrimaryKey(OperatorRecord record);
}