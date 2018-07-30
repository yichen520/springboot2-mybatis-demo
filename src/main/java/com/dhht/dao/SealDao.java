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

    List<Seal> selectUndelivered(Seal seal);

    SealOperationRecord SelectByCodeAndFlag(@Param("sealCode") String sealcode);

    SealOperationRecord SelectByCodeAndFlag03(@Param("sealCode")String sealcode,@Param("flag")String flag);

    int insertSealGetperson(SealGetPerson sealGetPerson);

    List<Seal> selectByMakeDepartmentCode(@Param("makeDepartmentCode") String makeDepartmentCode);

    //------------------------------统计模块-------------------------------------//
    int countAddSeal(@Param("makeDepartmentCode") String makeDepartmentCode,@Param("sealTypeCode")String sealTypeCode,@Param("startTime") String startTime,@Param("endTime") String endTime);

    int countLossSeal(@Param("makeDepartmentCode") String makeDepartmentCode,@Param("sealTypeCode")String sealTypeCode,@Param("startTime") String startTime,@Param("endTime") String endTime);

    int countLogoutSeal(@Param("makeDepartmentCode") String makeDepartmentCode,@Param("sealTypeCode")String sealTypeCode,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<String> selectLikeDistrictId(@Param("districtId")String districtId);

    List<Seal> selectByDistrictId(@Param("districtId")String districtId);

    int countAddSealByDistrictId(@Param("districtId") String districtId,@Param("sealTypeCode")String sealTypeCode,@Param("startTime") String startTime,@Param("endTime") String endTime);

    int countLossSealByDistrictId(@Param("districtId") String districtId,@Param("sealTypeCode")String sealTypeCode,@Param("startTime") String startTime,@Param("endTime") String endTime);

    int countLogoutSealByDistrictId(@Param("districtId") String districtId,@Param("sealTypeCode")String sealTypeCode,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<SealDistrict> countByDistrictId(List<DistrictMenus> districtIds);
}