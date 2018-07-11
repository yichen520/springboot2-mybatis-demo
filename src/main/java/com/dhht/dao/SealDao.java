package com.dhht.dao;

import com.dhht.model.*;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SealDao {


    int deleteByPrimaryKey(SealKey key);

    int insert(Seal record);

    Seal selectByPrimaryKey(@Param("id") String id);

    List<Seal> selectByCodeAndType(String useDepartmentCode);

    int updateByPrimaryKey(Seal record);

    int insertSealOperationRecord(SealOperationRecord sealOperationRecord);

    int insertSealMaterial(SealMaterial sealMaterial);

    List<Seal> selectByCodeAndName(Seal seal);

    SealOperationRecord SelectByCodeAndFlag(@Param("sealCode") String sealcode);

    SealOperationRecord SelectByCodeAndFlag03(@Param("sealCode")String sealcode,@Param("flag")String flag);

    int insertSealGetperson(SealGetPerson sealGetPerson);
}