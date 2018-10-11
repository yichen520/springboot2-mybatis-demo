package com.dhht.dao;

import com.dhht.model.*;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SealDao {


    int deleteByPrimaryKey(SealKey key);

    int insert(Seal record);

    Seal selectByPrimaryKey(@Param("id") String id);

    List<Seal> selectByCodeAndType(String useDepartmentCode);

    List<SealOperationRecord> selectSealOperationRecord(@Param("id") String id,@Param("operateType") String operateType);

    int updateByPrimaryKey(Seal record);

    int insertSealOperationRecord(SealOperationRecord sealOperationRecord);

    int insertSealMaterial(SealMaterial sealMaterial);

//    int insertVerifySeal(String id,boolean isPass,String rejectReason,String rejectRemark);

    List<Seal> selectByCodeAndName(Seal seal);

    List<Seal> selectUndelivered(Seal seal);

    List<Seal> selectPersonal(Seal seal);

    List<Seal> selectIsMake(Seal seal);

    List<Seal> selectIsRecord(Seal seal);

    List<Seal> selectIsDeliver(Seal seal);

    List<Seal> selectIsLoss(Seal seal);

    List<Seal> selectIsLogout(Seal seal);

    List<Seal> selectWaitDeliver(Seal seal);

    SealOperationRecord SelectByCodeAndFlag(@Param("sealCode") String sealcode);

    SealOperationRecord SelectByCodeAndFlag03(@Param("id") String id, @Param("flag") String flag);


    List<Seal> selectByMakeDepartmentCode(@Param("makeDepartmentCode") String makeDepartmentCode);

    List<Seal> selectByMakeDepartmentCodeAndIsMake(@Param("makeDepartmentCode") String makeDepartmentCode);

    SealAgent selectSealAgentById(@Param("agentId") String agentId);

    SealOperationRecord selectOperationRecordByCode(@Param("id") String id);

    SealOperationRecord selectOperationRecordByCodeAndType(@Param("id") String id,@Param("type") String type);

    int insertFaceCompareRecord(FaceCompareRecord faceCompareRecord);

    int insertSealMateriallist(List<SealMaterial > sealMaterials);

    SealMaterial selectSealMaterial(@Param("sealCode") String sealCode,@Param("type") String type);

    Seal selectLastSeal();

    List<SealCode> selectRecordDepartmentCode();

    String selectSealCode(@Param("code") String code);

    //------------------------------统计模块-------------------------------------//
    int countAddSeal(@Param("makeDepartmentCode") String makeDepartmentCode, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int countLossSeal(@Param("makeDepartmentCode") String makeDepartmentCode, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int countLogoutSeal(@Param("makeDepartmentCode") String makeDepartmentCode, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<String> selectLikeDistrictId(@Param("districtId") String districtId);

    List<String> selectDistrictId(@Param("districtId") String districtId);

    List<Seal> selectByDistrictId(@Param("districtId") String districtId);

    int countAddSealByDistrictId(@Param("districtId") String districtId, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int countLossSealByDistrictId(@Param("districtId") String districtId, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int countLogoutSealByDistrictId(@Param("districtId") String districtId, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int countAddSealByLikeDistrictId(@Param("districtId") String districtId, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int countLossSealByLikeDistrictId(@Param("districtId") String districtId, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    int countLogoutSealByLikeDistrictId(@Param("districtId") String districtId, @Param("sealTypeCode") String sealTypeCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<SealDistrict> countByDistrictId(List<DistrictMenus> districtIds);

    int indexCountSum(@Param("districtId") String districtId);

    int indexCountAdd(@Param("districtId") String districtId);

    int indexCountDel(@Param("districtId") String districtId);

    List<IndexCount> indexCountSealByDepartment ();

    int indexCountPolyline(@Param("month")int month,@Param("districtId")String districtId);

    int indexCountPieChart(@Param("makeDepartmentCode") String makeDepartmentCode ,@Param("sealTypeCode") String sealTypeCode);

    int indexCountAllSealByMakeDepartment(@Param("makeDepartmentCode") String makeDepartmentCode);

    List<IndexCount> indexCountSealTypeByMakeDepartment(@Param("makeDepartmentCode") String makeDepartmentCode);
}