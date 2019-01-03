package com.dhht.dao;

import com.dhht.model.SealOperationRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SealOperationRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(SealOperationRecord record);

    int insertSelective(SealOperationRecord record);

    SealOperationRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SealOperationRecord record);

    int updateByPrimaryKey(SealOperationRecord record);

    List<SealOperationRecord> selectBySealId(String sealId);
}