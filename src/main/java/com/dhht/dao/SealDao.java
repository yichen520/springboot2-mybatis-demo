package com.dhht.dao;

import com.dhht.model.Seal;
import com.dhht.model.SealKey;
import java.util.List;

import com.dhht.model.SealMaterial;
import com.dhht.model.SealOperationRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SealDao {


    int deleteByPrimaryKey(SealKey key);

    int insert(Seal record);

    Seal selectByPrimaryKey(SealKey key);

    List<Seal> selectByCodeAndType(String useDepartmentCode);

    int updateByPrimaryKey(Seal record);

    int insertSealOperationRecord(SealOperationRecord sealOperationRecord);

    int insertSealMaterial(SealMaterial sealMaterial);

    List<Seal> selectByCodeAndName(Seal seal);
}