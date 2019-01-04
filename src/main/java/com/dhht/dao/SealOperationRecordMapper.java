package com.dhht.dao;

import com.dhht.model.SealOperationRecord;
import org.apache.ibatis.annotations.Param;
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

    List<SealOperationRecord> selectSealOperationRecord(@Param("id") String id, @Param("operateType") String operateType);

    List<SealOperationRecord> selectBySealId(String sealId);
}